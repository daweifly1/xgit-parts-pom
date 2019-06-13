/**
 *
 */
package cn.com.xgit.gw.security.common;

import cn.com.xgit.gw.security.common.beans.CommonUserDetails;
import cn.com.xgit.parts.auth.feign.AuthClient;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 依赖rbac
 */
//@Component
public class CommonUserDetailsService implements UserDetailsService {

    @Autowired
    AuthClient authClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResultMessage<SysAccountVO> ur = authClient.queryAccountByLoginName(username, null);
        if (null == ur) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        if (null != ur.getData()) {
            CommonUserDetails commonUserDetails = new CommonUserDetails();
            commonUserDetails.setId(ur.getData().getId());
            commonUserDetails.setUsername(ur.getData().getUsername());
            if (!CollectionUtils.isEmpty(ur.getData().getRoles())) {
                Set<Long> roleIds = new HashSet<>();
                for (SysRoleVO r : ur.getData().getRoles()) {
                    roleIds.add(r.getId());
                }
                commonUserDetails.setRoleIds(roleIds);
            }
        }
        throw new UsernameNotFoundException("用户名查询异常");
    }

}
