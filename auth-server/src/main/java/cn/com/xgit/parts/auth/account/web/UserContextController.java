package cn.com.xgit.parts.auth.account.web;


import cn.com.xgit.parts.auth.account.facade.sys.UserAuthFacade;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.base.BasicController;
import com.xgit.bj.core.rsp.ResultMessage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/usrCtx"})
public class UserContextController
        extends BasicController {
    @Autowired
    UserAuthFacade userAuthFacade;

    @RequestMapping(value = {"/workspaceId"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ApiOperation("查询用户所在工作空间Id")
    public ResultMessage getWorkspaceId(@RequestParam("userId") String userId) {
        SysAccountVO user = userAuthFacade.queryLoginUser(userId);
        if (null != user) {
            return ResultMessage(user.getOrgId());
        } else {
            return ResultMessage(null);
        }

    }
}
