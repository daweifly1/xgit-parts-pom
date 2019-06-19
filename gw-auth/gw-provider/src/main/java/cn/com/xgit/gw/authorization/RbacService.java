/**
 *
 */
package cn.com.xgit.gw.authorization;

import cn.com.xgit.gw.api.beans.CommonUserDetails;
import cn.com.xgit.parts.auth.feign.AuthClient;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.auth.module.role.param.AuthRolePlatformParam;
import cn.com.xgit.parts.common.result.ResultMessage;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Component("rbacService")
public class RbacService {

    //当前访问的平台的id在请求中标志
    public static final String PLATFORM_ID = "platformId";

    @Autowired
    private AuthClient authClient;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (null != principal && principal instanceof CommonUserDetails) {

            SysUserLoginInfoVO baseUser = ((CommonUserDetails) authentication.getPrincipal()).getBaseUser();
            if (null == baseUser || null == baseUser.getId() || StringUtils.isBlank(baseUser.getUsername())) {
                return false;
            }
            //TODO若是超级管理员直接放过(内置用户名)
            if ("admin".equals(baseUser.getUsername())) {
                return true;
            }
            if (CollectionUtils.isEmpty(baseUser.getRoleIds())) {
                return false;
            }
            //鉴权时候，若能根据当前平台过滤掉一部分非当前平台的角色url
            Set<String> urls = getUrlsByRoles(baseUser.getRoleIds(), getPlatformId(request));
            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 尝试从请求中获取平台id
     * @param request
     * @return
     */
    private Long getPlatformId(HttpServletRequest request) {
        String platformId = request.getHeader(PLATFORM_ID);
        if (StringUtils.isNotBlank(platformId)) {
            return parseLong(platformId);
        }
        platformId = request.getParameter(PLATFORM_ID);
        if (StringUtils.isNotBlank(platformId)) {
            return parseLong(platformId);
        }
        return null;
    }

    private Long parseLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return null;
        }
    }

    private Set<String> getUrlsByRoles(Set<Long> roleIds, Long platformId) {
        List<Long> roleList = new ArrayList<>(roleIds);
        Collections.sort(roleList, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                //此处上层业务保证notnull
                return o1.compareTo(o2);
            }
        });
        AuthRolePlatformParam param = new AuthRolePlatformParam();
        param.setPlatformId(platformId);
        param.setRoleIdList(roleList);
        ResultMessage<Set<String>> r = authClient.queryUrlsByRoleIds(param);
        if (null != r && !CollectionUtils.isEmpty(r.getData())) {
            return r.getData();
        }
        return Sets.newHashSet();
    }


}
