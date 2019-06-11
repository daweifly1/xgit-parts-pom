package cn.com.xgit.parts.auth.feign;

import cn.com.xgit.parts.auth.feign.fallback.AuthClientFallBack;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.menu.param.SysAuthsParam;
import cn.com.xgit.parts.common.result.ResultMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${auth.feign.name:auth-server-parts}", fallback = AuthClientFallBack.class)
public interface AuthClient {

    @RequestMapping(value = "/menu/getAuthIds", method = RequestMethod.POST)
    ResultMessage<List<Long>> getAuthIds(@RequestBody SysAuthsParam sysAuthsParam);

    @RequestMapping(
            value = {"/sysUserAuths/checkAuthCodes"},
            method = {RequestMethod.GET}
    )
    ResultMessage<Boolean> checkAuthCodes(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "url", required = false) String url);


    @RequestMapping(
            value = {"/sysAccount/addUser"},
            method = {RequestMethod.POST}
    )
    ResultMessage<String> addUser(@RequestBody UserRegistVO userRegistVO);


    @RequestMapping(value = {"/sysAccount/queryAccountByLoginName"}, method = {RequestMethod.GET})
    ResultMessage<SysAccountVO> queryAccountByLoginName(String loginName);

}
