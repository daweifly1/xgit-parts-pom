package cn.com.xgit.gw.security.userdetails;

import cn.com.xgit.gw.api.beans.CommonUserDetails;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CommonUserDetailService extends BaseUserDetailService implements Serializable {

    @Override
    protected SysUserLoginInfoVO getUser(String username) {
        CommonUserDetails u = (CommonUserDetails) loadUserByUsername(username);
        return u.getBaseUser();
    }
}
