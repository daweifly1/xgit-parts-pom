package cn.com.xgit.parts.auth.module.account.controller;

import cn.com.xgit.parts.auth.exception.code.ErrorCode;
import cn.com.xgit.parts.auth.module.account.entity.SysAccount;
import cn.com.xgit.parts.auth.module.account.service.SysAccountService;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.common.base.BasicController;
import cn.com.xgit.parts.common.result.ResultMessage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 账户信息的管理操作
 */
@Slf4j
@RestController
@RequestMapping("/sysAccount")
public class SysAccountController extends BasicController {
    @Autowired
    private SysAccountService sysAccountService;


    @GetMapping("/list")
    @ApiOperation("查询用户列表")
    public ResultMessage<IPage<SysAccount>> list(SysAccount condition) {
        return ResultMessage.success(sysAccountService.page(getPagination(), new QueryWrapper<>(condition)));
    }

    @PutMapping
    public ResultMessage<String> account(@RequestBody SysAccount accountVO) {

        if (sysAccountService.updateByVO(accountVO)) {
            return ResultMessage.success();
        }
        return actionErrorResult("保存失败");
    }

    @DeleteMapping("/removeByUserId")
    public ResultMessage removeAccountByUserId(@RequestParam("userId") Long userId) {
        sysAccountService.removeAccountByUserId(userId);
        return ResultMessage.success();
    }


    @PostMapping("/password")
    public ResultMessage<ErrorCode> password(@RequestParam("userId") Long userId, @RequestParam("password") String password) {
        ErrorCode ret = sysAccountService.updatePassword(userId, password);
        return ResultMessage(ret);
    }

    @PostMapping("/resetPassword")
    public ResultMessage resetPassword(@RequestBody List<Long> userIds) {
        ErrorCode ret = sysAccountService.resetPassword(userIds);
        return ResultMessage(ret);
    }


    @GetMapping("/checkLoginName")
    public ResultMessage checkLoginName(String loginName) {
        ErrorCode ret = sysAccountService.checkLoginName(loginName);
        return ResultMessage(ret);
    }

    @GetMapping("/queryAccountByLoginName")
    public ResultMessage<SysAccountVO> queryAccountByLoginName(String loginName) {
        SysAccountVO ret = sysAccountService.queryAccountByLoginName(loginName);
        return ResultMessage(ret);
    }
}
