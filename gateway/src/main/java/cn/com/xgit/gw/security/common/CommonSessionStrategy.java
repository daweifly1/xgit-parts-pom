/**
 *
 */
package cn.com.xgit.gw.security.common;

import cn.com.xgit.gw.CustomsSecurityProperties;
import cn.com.xgit.parts.common.result.ResultMessage;
import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * session失效处理器
 */
public class CommonSessionStrategy implements InvalidSessionStrategy, SessionInformationExpiredStrategy {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CustomsSecurityProperties securityProperties;
    /**
     * 重定向策略
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    /**
     * 跳转前是否创建新的session
     */
    private boolean createNewSession = true;

    private ObjectMapper objectMapper = new ObjectMapper();

    public CommonSessionStrategy(CustomsSecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }


    protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (createNewSession) {
            request.getSession();
        }

        String sourceUrl = request.getRequestURI();
        String targetUrl;

        if (StringUtils.endsWithIgnoreCase(sourceUrl, ".html")) {
            if (StringUtils.equals(sourceUrl, securityProperties.getSignInPage())
                    || StringUtils.equals(sourceUrl, securityProperties.getSignOutPage())) {
                targetUrl = sourceUrl;
            } else {
                targetUrl = securityProperties.getDestinationUrl();
            }
            logger.info("跳转到:" + targetUrl);
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
//            Object result = buildResponseContent(request);
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write(objectMapper.writeValueAsString(result));
            response.getWriter().write(objectMapper.writeValueAsString(
                    FastJsonUtil.toJSONString(ResultMessage.error("请重新登录"))));
        }

    }

    /**
     * @param request
     * @return
     */
    protected Object buildResponseContent(HttpServletRequest request) {
        String message = "session已失效";
        if (isConcurrency()) {
            message = message + "，有可能是并发登录导致的";
        }
        return ResultMessage.error(message);
    }

    /**
     * session失效是否是并发导致的
     *
     * @return
     */
    protected boolean isConcurrency() {
        return false;
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        onSessionInvalid(httpServletRequest, httpServletResponse);
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        onSessionInvalid(event.getRequest(), event.getResponse());
    }
}
