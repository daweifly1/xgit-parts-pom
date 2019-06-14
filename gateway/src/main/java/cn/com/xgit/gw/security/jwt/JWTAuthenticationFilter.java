package cn.com.xgit.gw.security.jwt;

import cn.com.xgit.gw.util.http.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证拦截器，访问过程中校验jwt是否正确，距离超时时间一半时刷新token(保证正常访问不会掉线)
 */
@Slf4j
@Component
public class JWTAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private TokenAuthenticationHandler tokenAuthenticationHandler;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        HttpServletResponse resp = (HttpServletResponse) response;

        String token = req.getHeader(JWTConsts.HEADER_STRING);
//        if (StringUtils.isBlank(token)) {
//            token = (String) req.getSession().getAttribute(HEADER_STRING);
//        }
        if (StringUtils.isBlank(token)) {
            token = CookieUtil.getCookieValueByName(req, JWTConsts.HEADER_STRING);
        }
        if (StringUtils.isNotBlank(token) && token.startsWith(JWTConsts.TOKEN_PREFIX.trim())) {
            tokenAuthenticationHandler.doRefreshToken(resp, token.replace(JWTConsts.TOKEN_PREFIX.trim(), "").trim(), false);
        }
        filterChain.doFilter(request, response);
    }

}
