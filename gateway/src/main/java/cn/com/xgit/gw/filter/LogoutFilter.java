package cn.com.xgit.gw.filter;

import cn.com.xgit.gw.security.CustomsSecurityProperties;
import cn.com.xgit.gw.security.jwt.TokenAuthenticationHandler;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogoutFilter extends ZuulFilter {
    @Autowired
    private CustomsSecurityProperties securityProperties;

    @Autowired
    private TokenAuthenticationHandler tokenAuthenticationHandler;

    @Override
    public String filterType() {
        return "post";
    }

    public int filterOrder() {
        return 901;
    }


    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestUri = ctx.getRequest().getRequestURI();
        for (String url : securityProperties.getLoginOutUrls()) {
            if (requestUri.matches(url)) {
                return true;
            }
        }
        return false;
    }

    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        tokenAuthenticationHandler.rmAfterLoginOut(ctx.getResponse(), ctx.getRequest());
        return null;
    }

}
