package cn.com.xgit.gw.http;

import cn.com.xgit.gw.http.consts.ZuulRequestHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class HttpUtil {
    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            String[] ips = ip.split(",");
            if (ips.length > 0)
                ip = ips[0];
        }
        return ip;
    }

    public static boolean isAjax(HttpServletRequest request) {
        if (request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        }
        return false;
    }

    public static HttpServletRequest getHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes != null ? ((ServletRequestAttributes) requestAttributes).getRequest() : null;
    }

    public static String getRequestHeader(String header) {
        HttpServletRequest request = getHttpRequest();
        return request != null ? request.getHeader(header) : null;
    }

    public static Long getPlatformId() {
        String spId = getRequestHeader(ZuulRequestHeader.SYSTEM);
        return getLongParam(spId);

    }

    private static Long getLongParam(String spId) {
        if (StringUtils.isBlank(spId)) {
            return null;
        }
        try {
            return Long.parseLong(spId.trim());
        } catch (Exception e) {
            log.warn("", e);
        }
        return null;
    }

    public static Long getStoreId() {
        String spId = getRequestHeader(ZuulRequestHeader.STORE_ID);
        return getLongParam(spId);
    }

    public static Long getShopId() {
        String spId = getRequestHeader(ZuulRequestHeader.SHOP_ID);
        return getLongParam(spId);
    }
}
