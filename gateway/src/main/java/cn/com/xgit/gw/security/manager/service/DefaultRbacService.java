/**
 *
 */
package cn.com.xgit.gw.security.manager.service;

import cn.com.xgit.gw.security.common.beans.CommonUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

@Deprecated
public class DefaultRbacService {

    //当前访问的平台的id在请求中标志
    public static final String PLATFORM_ID = "platformId";

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (null != principal && principal instanceof CommonUserDetails) {
            return true;
        }
        return false;
    }

}
