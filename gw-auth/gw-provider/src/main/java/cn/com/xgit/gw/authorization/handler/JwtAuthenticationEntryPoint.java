package cn.com.xgit.gw.authorization.handler;

import cn.com.xgit.gw.authorization.module.CustomsSecurityProperties;
import cn.com.xgit.parts.common.result.ResultMessage;
import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Autowired
    private CustomsSecurityProperties customsSecurityProperties;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (StringUtils.isNoneBlank(customsSecurityProperties.getNoLoginRedirectUrl())) {
            String oUrl = request.getRequestURI();
            String url = StringUtils.isBlank(oUrl) ? customsSecurityProperties.getNoLoginRedirectUrl() : customsSecurityProperties.getNoLoginRedirectUrl() + "oUrl=" + oUrl;
            response.sendRedirect(url);
            return;
        }
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String body = FastJsonUtil.toJSONString(ResultMessage.error(403, authException.getMessage()));
        printWriter.write(body);
        printWriter.flush();
    }
}