package cn.com.xgit.gw.security.userdetails;

import cn.com.xgit.gw.api.beans.CommonUserDetails;
import cn.com.xgit.gw.enums.SystemEnum;
import cn.com.xgit.gw.http.HttpUtil;
import cn.com.xgit.parts.auth.feign.AuthClient;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Slf4j
public abstract class BaseUserDetailService implements UserDetailsService {

    @Autowired
    private AuthClient authClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserLoginInfoVO baseUser = null;
        Long platformId = HttpUtil.getPlatformId();
        ResultMessage<SysUserLoginInfoVO> rsp = authClient.queryAccountByUserNameOrMobi(username, platformId, false);
        if (null != rsp && null != rsp.getData()) {
            baseUser = rsp.getData();
        } else {
            throw new UsernameNotFoundException("找不到该用户，用户名：" + username);
        }
        if (null != platformId && SystemEnum.SHOP.getLongCode() == platformId.longValue()) {
            Long storeId = HttpUtil.getStoreId();
            Long shopId = HttpUtil.getShopId();
            //TODO 角色仅仅返回当前用户当前 总店或者分店的角色
//            Set<Long> curRoleIds = authClient.queryCurrentRoles(baseUser.getId(), storeId, shopId);
            Set<Long> curRoleIds = new HashSet<>();
            baseUser.setCurRoleIds(curRoleIds);
            baseUser.setStoreId(storeId);
            baseUser.setShopId(shopId);
        } else {
            baseUser.setCurRoleIds(baseUser.getRoleIds());
        }
        baseUser.setPlatformId(platformId);
        GrantedAuthority au = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "USER";
            }
        };
        String password = baseUser.getPassword();
        // 返回带有用户权限信息的User
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(baseUser.getUsername(),
                password, true, true, true, true, Arrays.asList(au));
        baseUser.setPassword(null);
        return new CommonUserDetails(baseUser, user);
    }

    protected abstract SysUserLoginInfoVO getUser(String phone);

}
