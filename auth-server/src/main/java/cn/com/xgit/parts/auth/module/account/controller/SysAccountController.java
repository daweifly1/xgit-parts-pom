package cn.com.xgit.parts.auth.module.account.controller;

import cn.com.xgit.parts.auth.common.base.BasicController;
import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.exception.code.ErrorCode;
import cn.com.xgit.parts.auth.module.account.entity.SysAccount;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.module.account.service.SysAccountService;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
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

@Slf4j
@RestController
@RequestMapping("/sysAccount")
@Api(value = "账户信息的管理操作Controller")
public class SysAccountController extends BasicController {
    @Autowired
    private SysAccountService sysAccountService;

    @GetMapping("/list")
    @ApiOperation("查询用户列表")
    public ResultMessage<IPage<SysAccount>> list(SysAccount condition) {
        return ResultMessage.success(sysAccountService.page(getPagination(), new QueryWrapper<>(condition)));
    }

    @PutMapping("add")
    @ApiOperation("查询用户列表")
    public ResultMessage<String> account(@RequestBody SysAccount accountVO) {
        if (sysAccountService.updateByVO(accountVO)) {
            return ResultMessage.success();
        }
        return actionErrorResult("保存失败");
    }

    @DeleteMapping("/removeByUserId")
    @ApiOperation("删除用户")
    public ResultMessage removeAccountByUserId(@RequestParam("userId") Long userId) {
        sysAccountService.removeAccountByUserId(userId);
        return ResultMessage.success();
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
    public ResultMessage<SysAccountVO> queryAccountByLoginName(@RequestParam(value = "platformId") String loginName, @RequestParam(value = "platformId", required = false) Long platformId) {
        SysAccountVO ret = sysAccountService.queryAccountByIdOrLoginName(null, loginName, platformId);
        return ResultMessage(ret);
    }

    @GetMapping("/queryAccountById")
    public ResultMessage<SysAccountVO> queryAccountById(@RequestParam(value = "userId") Long userId, @RequestParam(value = "platformId") Long platformId) {
        SysAccountVO ret = sysAccountService.queryAccountByIdOrLoginName(userId, null, platformId);
        return ResultMessage(ret);
    }

    @RequestMapping(value = {"/addUser"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("用户添加用户")
    public ResultMessage<String> addUser(@RequestBody UserRegistVO userRegistVO) {
        if (null == userRegistVO || null == userRegistVO.getSysAccountVO() || null == userRegistVO.getUserLoginVO()
                || StringUtils.isBlank(userRegistVO.getUserLoginVO().getUsername())) {
            throw new AuthException("用户信息不能为空");
        }
        try {
            Long userId = getUserId();
            if (null != userId) {
                userRegistVO.getSysAccountVO().setCreatedBy(userId);
            }
            sysAccountService.addRegistUser(userRegistVO);
            return ResultMessage.success();
        } catch (AuthException e) {
            log.info("", e);
            return actionErrorResult(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info("", e);
            return actionErrorResult("用户添加用户失败：" + e.getMessage());
        }
    }
}
