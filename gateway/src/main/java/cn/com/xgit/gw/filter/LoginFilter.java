package cn.com.xgit.gw.filter;

import cn.com.xgit.gw.security.CustomsSecurityProperties;
import cn.com.xgit.gw.security.common.beans.CommonUserDetails;
import cn.com.xgit.gw.security.jwt.TokenAuthenticationHandler;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

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
        ResultMessage<SysUserLoginInfoVO> actionResult = (ResultMessage<SysUserLoginInfoVO>) objectMapper.readValue(inputStream, ResultMessage.class);
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
                //登陆成功
                CommonUserDetails commonUserDetails = new CommonUserDetails();
                commonUserDetails.setId(sysUserLoginInfoVO.getId());
                commonUserDetails.setUsername(sysUserLoginInfoVO.getUsername());
                if (null != commonUserDetails) {
                    tokenAuthenticationHandler.saveAfterLogin(commonUserDetails, ctx.getResponse());
                }
            }
        } catch (Exception exc) {
            log.error("failed to process things", exc);
        }
        return null;
    }

    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestUri = ctx.getRequest().getRequestURI();
        for (String url : securityProperties.getLoginUrls()) {
            if (requestUri.matches(url)) {
                return true;
            }
        }
        return false;
    }
}
