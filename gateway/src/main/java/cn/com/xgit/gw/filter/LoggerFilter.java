package cn.com.xgit.gw.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fp295 on 2018/6/26.
 * 网关日志
 */
@Component
public class LoggerFilter extends ZuulFilter {

//    @Autowired
//    private LogSendService logSendService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
//        logSendService.send(RequestContext.getCurrentContext());
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url = request.getRequestURI();
        String method = request.getMethod();
        ctx.addZuulRequestHeader("username", "dd");
        ctx.addZuulRequestHeader("x-user-id", "230485564632072192");
        ctx.addZuulRequestHeader("original_requestURL",request.getRequestURL().toString());
        return null;
    }
}
