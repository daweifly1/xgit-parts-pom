package cn.com.xgit.parts.auth.feign;

import cn.com.xgit.parts.auth.feign.fallback.AuthClientFallBack;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${auth.feign.name:fast-auth-server-cdw}", fallback = AuthClientFallBack.class)
public interface AuthClient {

    @RequestMapping(
            value = {"/scepter/getAuthCodes"},
            method = {RequestMethod.GET}
    )
    ResultMessage<List<String>> getAuthCodes(@RequestParam(value = "userId", required = false) String userId, @RequestHeader("x-user-id") String selfUserId);

    @RequestMapping(
            value = {"/sysUserAuths/checkAuthCodes"},
            method = {RequestMethod.GET}
    )
    ResultMessage<Boolean> checkAuthCodes(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "url", required = false) String url);


    @RequestMapping(
            value = {"/profile/detail"},
            method = {RequestMethod.GET}
    )
    ResultMessage<SysAccountVO> querySysAccountVO(@RequestParam("userId") String userId);

    @RequestMapping(
            value = {"/auth/addUser"},
            method = {RequestMethod.POST}
    )
    ResultMessage<String> addUser(@RequestParam(value = "userId", required = false) String userId, @RequestBody UserRegistVO userRegistVO);

    @RequestMapping(value = {"/sysAccount/queryAccountByLoginName"}, method = {RequestMethod.GET})
    ResultMessage<SysAccountVO> queryAccountByLoginName(String loginName);

}
