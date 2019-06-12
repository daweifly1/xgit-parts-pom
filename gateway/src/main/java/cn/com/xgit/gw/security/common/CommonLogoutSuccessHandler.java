/**
 *
 */
package cn.com.xgit.gw.security.common;

import cn.com.xgit.parts.common.result.ResultMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认的退出成功处理器，如果设置了，则跳到配置的地址上，
 * 如果没配置，则返回json格式的响应。
 */
@Component
public class CommonLogoutSuccessHandler implements LogoutSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CustomsSecurityProperties securityProperties;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        logger.info("退出成功");

        if (StringUtils.isBlank(securityProperties.getSignOutSuccessUrl())) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResultMessage.success("退出成功")));
        } else {
            response.sendRedirect(securityProperties.getSignOutSuccessUrl());
        }

    }

}
