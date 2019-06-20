package cn.com.xgit.gw.authorization.filter.jwt;

import cn.com.xgit.gw.authorization.util.http.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private TokenAuthenticationHandler tokenAuthenticationHandler;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //优先从请求header中取
        String token = request.getHeader(JWTConsts.HEADER_STRING);
//        if (StringUtils.isBlank(token)) {
//            token = (String) req.getSession().getAttribute(HEADER_STRING);
//        }
        if (org.apache.commons.lang.StringUtils.isBlank(token)) {
            token = CookieUtil.getCookieValueByName(request, JWTConsts.HEADER_STRING);
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(token) && token.startsWith(JWTConsts.TOKEN_PREFIX.trim())) {
            tokenAuthenticationHandler.doRefreshToken(response, token.replace(JWTConsts.TOKEN_PREFIX.trim(), "").trim(), false);
        }
        filterChain.doFilter(request, response);
    }
}
