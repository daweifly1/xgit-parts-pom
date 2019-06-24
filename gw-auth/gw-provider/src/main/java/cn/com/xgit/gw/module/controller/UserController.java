package cn.com.xgit.gw.module.controller;

import cn.com.xgit.gw.api.beans.CommonUserDetails;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    /**
     * 资源服务器提供的受保护接口
     *
     * @param principal
     * @return
     */
    @RequestMapping("/api/user")
    public SysUserLoginInfoVO user(Authentication principal) {
        if (null != principal) {
            CommonUserDetails baseUser = ((CommonUserDetails) principal.getPrincipal());
            return baseUser.getBaseUser();
        }
        return null;
    }

}
