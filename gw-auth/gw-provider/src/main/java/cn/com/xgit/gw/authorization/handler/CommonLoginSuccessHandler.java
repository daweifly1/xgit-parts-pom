package cn.com.xgit.gw.authorization.handler;

import cn.com.xgit.gw.api.beans.CommonUserDetails;
import cn.com.xgit.gw.authorization.filter.jwt.TokenAuthenticationHandler;
import cn.com.xgit.gw.authorization.module.CustomsSecurityProperties;
import cn.com.xgit.parts.common.result.ResultMessage;
import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CommonLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private TokenAuthenticationHandler tokenAuthenticationHandler;

    @Autowired
    private CustomsSecurityProperties customsSecurityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        if (null != SecurityContextHolder.getContext() && null != SecurityContextHolder.getContext().getAuthentication()) {
            CommonUserDetails principal = (CommonUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            tokenAuthenticationHandler.saveAfterLogin(principal, response);

            if (StringUtils.isNotBlank(customsSecurityProperties.getLoginSuccRedirectUrl())) {
                String oUrl = request.getParameter("oUrl");

                response.setStatus(302);
                if (StringUtils.isNotBlank(oUrl)) {
                    response.sendRedirect(oUrl);
                    return;
                }
                response.sendRedirect(customsSecurityProperties.getLoginSuccRedirectUrl());
                return;

            }
            response.setContentType("application/json; charset=utf-8");
            String body = FastJsonUtil.toJSONString(ResultMessage.success(200, "登录成功"));
            printWriter.write(body);
            printWriter.flush();
        } else {
            response.setContentType("application/json; charset=utf-8");
            String body = FastJsonUtil.toJSONString(ResultMessage.error("登录失败"));
            printWriter.write(body);
            printWriter.flush();
        }
    }


}