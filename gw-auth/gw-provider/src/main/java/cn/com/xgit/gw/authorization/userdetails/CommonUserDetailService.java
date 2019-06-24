package cn.com.xgit.gw.authorization.userdetails;

import cn.com.xgit.gw.api.beans.CommonUserDetails;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import org.springframework.stereotype.Service;

@Service
public class CommonUserDetailService extends BaseUserDetailService {

    @Override
    protected SysUserLoginInfoVO getUser(String username) {
        CommonUserDetails u = (CommonUserDetails) loadUserByUsername(username);
        return u.getBaseUser();
    }
}
