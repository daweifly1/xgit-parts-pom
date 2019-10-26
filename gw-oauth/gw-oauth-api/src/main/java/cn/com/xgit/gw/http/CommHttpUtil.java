package cn.com.xgit.gw.http;

import cn.com.xgit.gw.http.consts.ZuulRequestHeader;
import cn.com.xgit.gw.http.exceptions.GwException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 提供给zuul下游使用
 */
@Slf4j
public class CommHttpUtil {

    /**
     * 得到进入网关的客户ip
     *
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String userIp = request.getHeader(ZuulRequestHeader.REMOTE_IP);
        return userIp;
    }

    /**
     * 用户id
     *
     * @param request
     * @return
     */
    public static Long getUserId(HttpServletRequest request) {
        String userId = request.getHeader(ZuulRequestHeader.USER_ID);
        if (StringUtils.isNotBlank(userId)) {
            try {
                return Long.parseLong(userId);
            } catch (Exception e) {
                throw new GwException(1, "获取用户id异常");
            }
        }
        return null;
    }

    public static String getUserName(HttpServletRequest request) {
        return request.getHeader(ZuulRequestHeader.USER_NAME);
    }

    public static boolean isAjax(HttpServletRequest request) {
        if (request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        }
        return false;
    }

    public static Long getStoreId() {
        String spId = getRequestHeader(ZuulRequestHeader.STORE_ID);
        return getLongParam(spId);
    }

    public static Long getShopId() {
        String spId = getRequestHeader(ZuulRequestHeader.SHOP_ID);
        return getLongParam(spId);
    }

    public static String getRequestHeader(String header) {
        HttpServletRequest request = getHttpRequest();
        return request != null ? request.getHeader(header) : null;
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

    public static Long getDataId() {
        Long shopId = getShopId();
        if (null != shopId) {
            return shopId;
        }
        return getStoreId();
    }

    public static HttpServletRequest getHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes != null ? ((ServletRequestAttributes) requestAttributes).getRequest() : null;
    }


}
