package cn.com.xgit.gw.security.userdetails;

import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import org.springframework.stereotype.Service;

@Service
public class QrUserDetailService extends BaseUserDetailService {


    @Override
    protected SysUserLoginInfoVO getUser(String qrCode) {
        return null;
    }
}
