package cn.com.xgit.gw.http;

import javax.servlet.http.HttpServletRequest;

/**
 * http 公共方法
 */
public class CommHttpParam {

    public static boolean isAjax(HttpServletRequest request) {
        if (request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        }
        return false;
    }


}
