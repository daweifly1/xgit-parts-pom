/**
 *
 */
package cn.com.xgit.gw.module.service;

import cn.com.xgit.gw.api.CommonUserDetails;
import cn.com.xgit.gw.http.HttpUtil;
import cn.com.xgit.gw.module.CustomsSecurityProperties;
import cn.com.xgit.gw.module.beans.RequestUrlSet;
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
import java.util.*;

@Component("rbacService")
public class RbacService {

    //当前访问的平台的id在请求中标志
    public static final String PLATFORM_ID = "platformId";

    @Autowired
    private AuthClient authClient;

    @Autowired
    private CustomsSecurityProperties customsSecurityProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (null != principal && principal instanceof CommonUserDetails) {

            SysUserLoginInfoVO baseUser = ((CommonUserDetails) authentication.getPrincipal()).getBaseUser();
            if (null == baseUser || null == baseUser.getId() || StringUtils.isBlank(baseUser.getUsername())) {
                return false;
            }
            //若是超级管理员直接放过(内置用户名)
            String[] sus = customsSecurityProperties.getSuperAdmins();
            if (null != sus && sus.length > 0) {
                for (String superAdmin : sus) {
                    if (StringUtils.isNotBlank(superAdmin) && superAdmin.equals(baseUser.getUsername())) {
                        return true;
                    }
                }
            }
            //认证后不需要鉴权的url
            if (null != customsSecurityProperties.getPermitAccessUrls() && customsSecurityProperties.getPermitAccessUrls().length > 0) {
                int exLength = customsSecurityProperties.getPermitAccessUrls().length;
                Set<String> set = new HashSet<>(exLength);
                set.addAll(Arrays.asList(customsSecurityProperties.getPermitAccessUrls()));
                if (set.contains(request.getRequestURI())) {
                    return true;
                }
            }
            if (CollectionUtils.isEmpty(baseUser.getRoleIds())) {
                return false;
            }
            RequestUrlSet rSet = getRequestUrlSet(baseUser.getRoleIds(), HttpUtil.getPlatformId(), HttpUtil.getDataId());
            if (rSet.getUrls().contains(request.getRequestURI())) {
                return true;
            }
            for (String url : rSet.getMatchUrls()) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 角色对应的权限集合可以考虑cach
     * @param roleIds
     * @param platformId
     * @return
     */
    private RequestUrlSet getRequestUrlSet(Set<Long> roleIds, Long platformId, Long dataId) {
        RequestUrlSet result = new RequestUrlSet();
        Set<String> set = getUrlsByRoles(roleIds, platformId, dataId);
        result.setMatchUrls(new HashSet<>(set.size()));
        result.setUrls(new HashSet<>(set.size()));
        for (String s : set) {
            if (s.contains("*")) {
                result.getMatchUrls().add(s);
            } else {
                result.getUrls().add(s);
            }
        }
        return result;
    }

    private Set<String> getUrlsByRoles(Set<Long> roleIds, Long platformId, Long dataId) {
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
        param.setDataId(dataId);
        ResultMessage<Set<String>> r = authClient.queryUrlsByRoleIds(param);
        if (null != r && !CollectionUtils.isEmpty(r.getData())) {
            return r.getData();
        }
        return Sets.newHashSet();
    }
}
