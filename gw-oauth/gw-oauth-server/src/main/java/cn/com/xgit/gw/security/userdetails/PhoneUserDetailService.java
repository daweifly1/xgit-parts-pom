package cn.com.xgit.gw.security.userdetails;

import cn.com.xgit.parts.auth.feign.AuthClient;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by fp295 on 2018/11/25.
 */
@Service
@Data
public class PhoneUserDetailService extends BaseUserDetailService {

    @Autowired
    private AuthClient authClient;

    @Override
    protected SysUserLoginInfoVO getUser(String phone) {
        SysUserLoginInfoVO baseUser = null;
        ResultMessage<SysUserLoginInfoVO> rsp = authClient.queryAccountByUserNameOrMobi(phone, null, true);
        if (null != rsp && null != rsp.getData()) {
            baseUser = rsp.getData();
        } else {
            throw new UsernameNotFoundException("找不到该用户，手机号码：" + phone);
        }
        // 返回带有用户权限信息的User
        return baseUser;
    }
}
