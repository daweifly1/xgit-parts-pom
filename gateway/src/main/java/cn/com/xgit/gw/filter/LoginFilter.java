package cn.com.xgit.gw.filter;

import cn.com.xgit.parts.common.result.ResultMessage;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xiaojun.infra.ActionResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.InputStream;

@Slf4j
@Service
public class LoginFilter extends ZuulFilter {
    @Value("${fast.auth.login.url}")
    String[] loginUrls;

    public String filterType() {
        return "post";
    }

    public int filterOrder() {
        return 900;
    }


    protected String postUserLogin(RequestContext requestContext) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        InputStream inputStream = requestContext.getResponseDataStream();
        ResultMessage<String> actionResult = (ActionResult) objectMapper.readValue(inputStream, ActionResult.class);
        inputStream.close();
        String userId = "";
        if (actionResult.getStatus() != 0) {
            log.error(actionResult.getMessage());
        } else {
            userId = (String) actionResult.getValue();
            actionResult.setValue(null);
        }
        requestContext.setResponseBody(objectMapper.writeValueAsString(actionResult));
        return userId;
    }

    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpSession httpSession = ctx.getRequest().getSession();
        try {
            String userId = postUserLogin(ctx);
            if (StringUtils.isNotBlank(userId)) {
                httpSession.setAttribute("uid", userId);
            }
        } catch (Exception exc) {
            log.error("failed to process things", exc);
        }
        return null;
    }

    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestUri = ctx.getRequest().getRequestURI();
        for (String url : this.loginUrls) {
            if (requestUri.matches(url)) {
                return true;
            }
        }
        return false;
    }
}
