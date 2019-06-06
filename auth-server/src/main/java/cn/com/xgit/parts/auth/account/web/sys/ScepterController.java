package cn.com.xgit.parts.auth.account.web.sys;

import cn.com.xgit.parts.auth.account.facade.sys.UserAuthFacade;
import cn.com.xgit.parts.auth.module.base.BasicController;
import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import com.xgit.bj.core.rsp.ResultMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/scepter"})
public class ScepterController extends BasicController {

    @Autowired
    private UserAuthFacade userAuthFacade;

    @RequestMapping(value = {"/getAuthCodes"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage<List<String>> getAuthCodes(@RequestParam(value = "userId", required = false) String userId, @RequestHeader("x-user-id") String selfUserId) {
        if (StringUtils.isBlank(userId)) {
            return ResultMessage(userAuthFacade.authCodesByUserId(selfUserId));
        }
        return ResultMessage(userAuthFacade.authCodesByUserId(userId));
    }

    @RequestMapping(value = {"roles"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage<List<SysRoleVO>> roles(@RequestHeader("x-user-id") String userId) {
        List<SysRoleVO> ret = userAuthFacade.queryAllRoles(userId);
        return ResultMessage(ret);
    }
}
