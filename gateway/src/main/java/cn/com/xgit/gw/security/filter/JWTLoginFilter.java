//package com.xiaojun.auth.filter;
//
//import cn.com.xgit.gw.security.common.beans.CommonUserDetails;
//import cn.com.xgit.gw.security.jwt.JWTConsts;
//import cn.com.xgit.gw.security.jwt.TokenAuthenticationHandler;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
//
//
//    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "loginName";
//
//    private AuthenticationSuccessHandler successHandler;
//
//    public JWTLoginFilter() {
//    }
//
//    public JWTLoginFilter(AuthenticationManager authManager) {
//        setAuthenticationManager(authManager);
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//
//        return super.attemptAuthentication(request, response);
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
//        TokenAuthenticationHandler tokenAuthenticationHandler = new TokenAuthenticationHandler();
//        Object obj = auth.getPrincipal();
//        if (obj != null) {
//            CommonUserDetails userDetails = (CommonUserDetails) obj;
//            String token = tokenAuthenticationHandler.generateToken(userDetails);
//            res.addHeader(JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + " " + token);
//        }
//
//        if (successHandler != null) {
//            successHandler.onAuthenticationSuccess(req, res, auth);
//        }
//    }
//
//    public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
//        this.successHandler = successHandler;
//    }
//
//
//    protected String obtainUsername(HttpServletRequest request) {
//        return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
//    }
//}
