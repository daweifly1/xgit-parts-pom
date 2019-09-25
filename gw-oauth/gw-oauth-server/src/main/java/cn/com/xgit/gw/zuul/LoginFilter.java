package cn.com.xgit.gw.zuul;

import cn.com.xgit.gw.api.beans.CommonUserDetails;
import cn.com.xgit.gw.enums.SystemEnum;
import cn.com.xgit.gw.http.HttpUtil;
import cn.com.xgit.gw.module.CustomsSecurityProperties;
import cn.com.xgit.gw.security.filter.jwt.TokenAuthenticationHandler;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 当有较复杂 逻辑登录校验时候，可以自定义登录
 * security 认证排除，登录成功后拦截返回对象生成token
 */
@Slf4j
@Service
public class LoginFilter extends ZuulFilter {
    @Autowired
    private CustomsSecurityProperties securityProperties;

    @Autowired
    private TokenAuthenticationHandler tokenAuthenticationHandler;

    public String filterType() {
        return "post";
    }

    public int filterOrder() {
        return 900;
    }


    protected SysUserLoginInfoVO postUserLogin(RequestContext requestContext) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        InputStream inputStream = requestContext.getResponseDataStream();
//        ResultMessage<SysUserLoginInfoVO> actionResult = (ResultMessage<SysUserLoginInfoVO>) objectMapper.readValue(inputStream, ResultMessage.class);

        String json = FastJsonUtil.convertStream2Json(inputStream);
        ResultMessage<SysUserLoginInfoVO> actionResult = JSON.parseObject(json, new TypeReference<ResultMessage<SysUserLoginInfoVO>>() {
        });

        inputStream.close();
        SysUserLoginInfoVO sysUserLoginInfoVO = null;
        if (actionResult.getStatus() != 0) {
            log.error(actionResult.getMessage());
        } else {
            sysUserLoginInfoVO = actionResult.getData();
            actionResult.setData(null);
        }
        requestContext.setResponseBody(objectMapper.writeValueAsString(actionResult));
        return sysUserLoginInfoVO;
    }

    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            SysUserLoginInfoVO sysUserLoginInfoVO = postUserLogin(ctx);
            if (null != sysUserLoginInfoVO) {
                Long platformId = HttpUtil.getPlatformId();
                if (null != platformId && SystemEnum.SHOP.getLongCode() == platformId.longValue()) {
                    Long storeId = HttpUtil.getStoreId();
                    Long shopId = HttpUtil.getShopId();
                    //TODO 角色仅仅返回当前用户当前 总店或者分店的角色
//            Set<Long> curRoleIds = authClient.queryCurrentRoles(baseUser.getId(), storeId, shopId);
                    Set<Long> curRoleIds = new HashSet<>();
                    sysUserLoginInfoVO.setCurRoleIds(curRoleIds);
                    sysUserLoginInfoVO.setStoreId(storeId);
                    sysUserLoginInfoVO.setShopId(shopId);
                } else {
                    sysUserLoginInfoVO.setCurRoleIds(sysUserLoginInfoVO.getRoleIds());
                }
                sysUserLoginInfoVO.setPlatformId(platformId);

                GrantedAuthority au = new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return "USER";
                    }
                };
                User user = new User(sysUserLoginInfoVO.getUsername(),
                        sysUserLoginInfoVO.getPassword(), true, true, true, true, Arrays.asList(au));
                sysUserLoginInfoVO.setPassword(null);
                //登陆成功
                CommonUserDetails commonUserDetails = new CommonUserDetails(sysUserLoginInfoVO, user);
                tokenAuthenticationHandler.saveAfterLogin(commonUserDetails, ctx.getResponse());
            } else {
                HttpServletResponse response = ctx.getResponse();
                response.setStatus(401);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter printWriter = response.getWriter();
                String body = FastJsonUtil.toJSONString(ResultMessage.error("登陆失败"));
                printWriter.write(body);
                printWriter.flush();
            }
        } catch (Exception exc) {
            log.error("failed to process things", exc);
        }
        return null;
    }

    public boolean shouldFilter() {
        if (StringUtils.isBlank(securityProperties.getCustomSignInPage())) {
            return false;
        }
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestUri = ctx.getRequest().getRequestURI();
        if (requestUri.matches(securityProperties.getCustomSignInPage())) {
            return true;
        }
        return false;
    }
}
