package cn.com.xgit.gw.common.http;

import cn.com.xgit.gw.security.filter.JWTConsts;
import cn.com.xgit.gw.security.filter.TokenAuthenticationHandler;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class TockenUtil {

    /**
     * 从cookie中获取用户信息
     *
     * @param req
     * @return
     */
    public static SysAccountVO getSysUserDetailFromRequest(HttpServletRequest req) {
        String token = req.getHeader(JWTConsts.HEADER_STRING);
        if (StringUtils.isBlank(token)) {
            token = (String) req.getSession().getAttribute(JWTConsts.HEADER_STRING);
        }
        if (StringUtils.isBlank(token)) {
            token = (String) CookieUtil.getCookieValueByName(req, JWTConsts.HEADER_STRING);
        }
        if (StringUtils.isNotBlank(token)) {
            SysAccountVO su = getSysUserDetail(token);
            return su;
        }
        return null;
    }

    public static SysAccountVO getSysUserDetail(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        token = token.replace(JWTConsts.TOKEN_PREFIX.trim(), "");
        TokenAuthenticationHandler tokenAuthenticationHandler = new TokenAuthenticationHandler();
        String token2 = tokenAuthenticationHandler.getSubjectFromToken(token);
        SysAccountVO su = FastJsonUtil.parse(token2, SysAccountVO.class);
        return su;
    }
}
