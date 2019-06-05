package cn.com.xgit.parts.auth.account.web.sys;

import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.service.sys.SysAccountService;
import cn.com.xgit.parts.auth.account.web.BasicController;
import com.github.pagehelper.PageInfo;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import com.xgit.bj.core.rsp.ResultMessage;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * SysAuths Controller 实现类
 */
@Slf4j
@RestController
@RequestMapping("/sysAccount")
public class SysAccountController extends BasicController {
    @Autowired
    private SysAccountService sysAccountService;


    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("查询用户列表")
    public ResultMessage<PageInfo<SysAccountVO>> list(@RequestBody SearchCommonVO<SysAccountVO> condition, @RequestHeader("x-user-id") String userId) {
        PageCommonVO<SysAccountVO> pageCommonVO = sysAccountService.list(condition);
        return ResultMessage(pageCommonVO.getPageInfo());
    }

    @RequestMapping(method = {org.springframework.web.bind.annotation.RequestMethod.PUT})
    public ResultMessage<ErrorCode> account(@RequestBody SysAccountVO accountVO) {
        ErrorCode ret = sysAccountService.save(accountVO);
        return ResultMessage(ret);
    }

    @RequestMapping(value = {"/removeByUserId"}, method = {org.springframework.web.bind.annotation.RequestMethod.DELETE})
    public ResultMessage<ErrorCode> removeAccountByUserId(@RequestParam("userId") String userId) {
        int r = sysAccountService.batchDelete(Arrays.asList(userId));
        if (r > 0) {
            return ResultMessage(ErrorCode.Success);
        }
        return actionErrorResult("delete error");
    }


    @RequestMapping(value = {"/password"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResultMessage<ErrorCode> password(@RequestParam("userId") String userId, @RequestParam("password") String password) {
        ErrorCode ret = sysAccountService.updatePassword(userId, password);
        return ResultMessage(ret);
    }

    @RequestMapping(value = {"/resetPassword"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResultMessage resetPassword(@RequestBody List<String> userIds) {
        ErrorCode ret = sysAccountService.resetPassword(userIds);
        return ResultMessage(ret);
    }


    @RequestMapping(value = {"/checkLoginName"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage checkLoginName(String loginName) {
        ErrorCode ret = sysAccountService.checkLoginName(loginName);
        return ResultMessage(ret);
    }

    @RequestMapping(value = {"/queryAccountByLoginName"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage<SysAccountVO> queryAccountByLoginName(String loginName) {
        SysAccountVO ret = sysAccountService.queryAccountByLoginName(loginName);
        return ResultMessage(ret);
    }
}
