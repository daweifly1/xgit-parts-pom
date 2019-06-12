package cn.com.xgit.gw.security.filter;

import cn.com.xgit.gw.common.http.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JWTAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        HttpServletResponse resp = (HttpServletResponse) response;

        String token = req.getHeader(JWTConsts.HEADER_STRING);
//        if (StringUtils.isBlank(token)) {
//            token = (String) req.getSession().getAttribute(HEADER_STRING);
//        }
        if (StringUtils.isBlank(token)) {
            token = (String) CookieUtil.getCookieValueByName(req, JWTConsts.HEADER_STRING);
        }
        if (StringUtils.isNotBlank(token) && token.startsWith(JWTConsts.TOKEN_PREFIX.trim())) {
            TokenAuthenticationHandler tokenAuthenticationHandler = new TokenAuthenticationHandler();
            tokenAuthenticationHandler.doRefreshToken(resp, token.replace(JWTConsts.TOKEN_PREFIX.trim(), "").trim(), false);
        }
        filterChain.doFilter(request, response);
    }

}
