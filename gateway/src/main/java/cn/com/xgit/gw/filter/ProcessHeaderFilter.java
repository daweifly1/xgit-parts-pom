package cn.com.xgit.gw.filter;

import cn.com.xgit.gw.http.consts.ZuulRequestHeader;
import cn.com.xgit.gw.security.common.beans.CommonUserDetails;
import cn.com.xgit.gw.util.http.HttpUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 将认证后的用户信息传递给下游
 */
@Component
public class ProcessHeaderFilter extends ZuulFilter {

//    @Autowired
//    private LogSendService logSendService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (null != principal && principal instanceof CommonUserDetails) {
            CommonUserDetails commonUserDetails = (CommonUserDetails) principal;
            if (null == commonUserDetails || null == commonUserDetails.getId() || StringUtils.isBlank(commonUserDetails.getUsername())) {
                return false;
            }
            ctx.addZuulRequestHeader(ZuulRequestHeader.USER_ID, commonUserDetails.getId() + "");
            ctx.addZuulRequestHeader(ZuulRequestHeader.USER_NAME, commonUserDetails.getUsername());
            ctx.addZuulRequestHeader(ZuulRequestHeader.REMOTE_IP, HttpUtil.getIpAddress(request));
        }
        return null;
    }
}
