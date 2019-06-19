package cn.com.xgit.gw.http;

import cn.com.xgit.gw.http.consts.ZuulRequestHeader;
import cn.com.xgit.gw.http.exceptions.GwException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 提供给zuul下游使用
 */
public class CommHttpParam {

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

}
