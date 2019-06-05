package cn.com.xgit.parts.auth.account.web.sys;

import cn.com.xgit.parts.auth.account.facade.sys.UserAuthFacade;
import cn.com.xgit.parts.auth.account.web.BasicController;
import com.xgit.bj.core.rsp.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SysAuths Controller 实现类
 */
@Slf4j
@RestController
@RequestMapping("/sysUserAuths")
public class SysUserAuthsController extends BasicController {
    @Autowired
    private UserAuthFacade userAuthFacade;

    @RequestMapping(value = {"/checkAuthCodes"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage<Boolean> checkAuthCodes(@RequestParam(value = "userId", required = true) String userId, @RequestParam(value = "url", required = false) String url) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(url)) {
            return new ResultMessage<>(false);
        }
        return ResultMessage(userAuthFacade.checkAuthCodes(userId, url));
    }


}
