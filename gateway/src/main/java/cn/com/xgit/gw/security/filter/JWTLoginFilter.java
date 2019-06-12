package com.xiaojun.auth.filter;

import cn.com.xgit.gw.security.filter.TokenAuthenticationHandler;
import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "loginName";

    private AuthenticationSuccessHandler successHandler;

    public JWTLoginFilter() {
    }

    public JWTLoginFilter(AuthenticationManager authManager) {
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        TokenAuthenticationHandler tokenAuthenticationHandler = new TokenAuthenticationHandler();
        Object obj = auth.getPrincipal();
        if (obj != null) {
            UserDetails userDetails = (UserDetails) obj;
            String token = tokenAuthenticationHandler.generateToken(FastJsonUtil.toJSONString(userDetails));
            res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
        }

        if (successHandler != null) {
            successHandler.onAuthenticationSuccess(req, res, auth);
        }
    }

    public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }


    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
    }
}
