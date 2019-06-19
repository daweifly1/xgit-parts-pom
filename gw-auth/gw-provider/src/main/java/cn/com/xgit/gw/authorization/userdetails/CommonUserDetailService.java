package cn.com.xgit.gw.authorization.userdetails;

import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import org.springframework.stereotype.Service;

@Service
public class CommonUserDetailService extends BaseUserDetailService {

    @Override
    protected SysUserLoginInfoVO getUser(String username) {
        return null;
    }
}
