package cn.com.xgit.parts.auth.module.account.controller;

import cn.com.xgit.parts.auth.common.base.BasicController;
import cn.com.xgit.parts.auth.module.account.entity.SysAccountSocial;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.auth.module.account.service.SysAccountSocialService;
import cn.com.xgit.parts.auth.module.login.facade.UserInfoFacade;
import cn.com.xgit.parts.common.result.ResultMessage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 社交登录方式接口
 */
@Slf4j
@RestController
@RequestMapping("/social")
@Api("社交登录方式接口相关操作Controller ")
public class SocialController extends BasicController {
    @Autowired
    private UserInfoFacade userInfoFacade;

    @Autowired
    private SysAccountSocialService sysAccountSocialService;

    @PutMapping("/bindSocialAccount")
    @ApiOperation("社交登录绑定")
    ResultMessage<String> bindSocialAccount(@RequestParam(value = "accountId") Long accountId, @RequestParam(value = "socialAccount") String socialAccount, @RequestParam(value = "type") String type) {
        SysAccountSocial sysAccountSocial = new SysAccountSocial();
        sysAccountSocial.setSocialAccount(socialAccount);
        sysAccountSocial.setAccountId(accountId);
        sysAccountSocial.setType(type);
        return sysAccountSocialService.bindSocialAccount(sysAccountSocial);
    }

    @GetMapping("/queryAccountBySocail")
    @ApiOperation("社交登录查询，仅接口调用")
    ResultMessage<SysUserLoginInfoVO> queryAccountBySocail(@RequestParam(value = "socialAccount") String socialAccount, @RequestParam(value = "type") String type) {
        SysAccountSocial sysAccountSocial = new SysAccountSocial();
        sysAccountSocial.setSocialAccount(socialAccount);
        sysAccountSocial.setType(type);
        return sysAccountSocialService.queryAccountBySocail(sysAccountSocial);
    }

    @DeleteMapping("/release")
    @ApiOperation("社交登录解绑")
    ResultMessage release(@RequestParam(value = "accountId", required = false) Long accountId, @RequestParam(value = "type") String type) {
        //解绑
        SysAccountSocial sysAccountSocial = new SysAccountSocial();
        sysAccountSocial.setAccountId(accountId);
        sysAccountSocial.setType(type);
        if (null == accountId) {
            sysAccountSocial.setAccountId(getUserId());
        }
        if (null != sysAccountSocial.getAccountId()) {
            sysAccountSocialService.remove(new QueryWrapper<>(sysAccountSocial));
            return ResultMessage.success();
        }
        return ResultMessage.error("参数异常");
    }
}
