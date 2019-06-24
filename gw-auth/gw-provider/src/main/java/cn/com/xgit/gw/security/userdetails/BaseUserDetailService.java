package cn.com.xgit.gw.security.userdetails;

import cn.com.xgit.gw.api.beans.CommonUserDetails;
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


@Slf4j
public abstract class BaseUserDetailService implements UserDetailsService {

    @Autowired
    private AuthClient authClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserLoginInfoVO baseUser = null;
        ResultMessage<SysUserLoginInfoVO> rsp = authClient.queryAccountByUserNameOrMobi(username, null, false);
        if (null != rsp && null != rsp.getData()) {
            baseUser = rsp.getData();
        } else {
            throw new UsernameNotFoundException("找不到该用户，用户名：" + username);
        }
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
