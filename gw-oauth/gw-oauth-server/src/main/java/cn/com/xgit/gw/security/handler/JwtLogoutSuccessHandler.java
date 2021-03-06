package cn.com.xgit.gw.security.handler;

import cn.com.xgit.gw.http.HttpUtil;
import cn.com.xgit.gw.module.CustomsSecurityProperties;
import cn.com.xgit.gw.security.filter.jwt.TokenAuthenticationHandler;
import cn.com.xgit.parts.common.result.ResultMessage;
import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private TokenAuthenticationHandler tokenAuthenticationHandler;
    @Autowired
    private CustomsSecurityProperties securityProperties;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        tokenAuthenticationHandler.rmAfterLoginOut(response, request);
        if (!HttpUtil.isAjax(request) && StringUtils.isNotBlank(securityProperties.getSignOutSuccessUrl())) {
            String url = securityProperties.getSignOutSuccessUrl();
            response.sendRedirect(url);
            return;
        }
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String body = FastJsonUtil.toJSONString(ResultMessage.success(200, "退出成功"));
        printWriter.write(body);
        printWriter.flush();
    }
}
