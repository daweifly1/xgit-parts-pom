package cn.com.xgit.parts.auth.feign;

import cn.com.xgit.parts.auth.feign.fallback.AuthClientFallBack;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.menu.param.SysAuthsParam;
import cn.com.xgit.parts.auth.module.role.param.AuthRolePlatformParam;
import cn.com.xgit.parts.common.result.ResultMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "${auth.feign.name:parts-auth-server}", fallback = AuthClientFallBack.class)
public interface AuthClient {


    /**
     * 根据平台、用户查询用户的信息（指定平台时候仅返回平台下的角色，否则不返回角色信息）
     *
     * @param loginName
     * @param platformId
     * @return
     */
    @RequestMapping(value = {"/sysAccount/queryAccountByLoginName"}, method = {RequestMethod.GET})
    ResultMessage<SysAccountVO> queryAccountByLoginName(@RequestParam(value = "loginName") String loginName, @RequestParam(value = "platformId", required = false) Long platformId);

    /**
     * 添加用户信息，若不指定密码（默认123456），若不指定角色无角色（角色赋值仅需要id即可）
     *
     * @param userRegistVO
     * @return
     */
    @PostMapping("/sysAccount/addUser")
    ResultMessage<String> addUser(@RequestBody UserRegistVO userRegistVO);


    /**
     * 根据平台、用户、查询用户所有的权限码（feigin ?）
     *
     * @param sysAuthsParam
     * @return
     */
    @RequestMapping(value = "/menu/getAuthIds", method = RequestMethod.POST)
    ResultMessage<List<Long>> getAuthIds(@RequestBody SysAuthsParam sysAuthsParam);


    /**
     * 根据角色查询角色具有的url集合（网关鉴权时候使用）
     *
     * @param param
     * @return
     */
    @PostMapping("/menu/queryUrlsByRoleIds")
    ResultMessage<Set<String>> queryUrlsByRoleIds(@RequestBody AuthRolePlatformParam param);

    /**
     * 绑定社交账号
     *
     * @param accountId
     * @param socialAccount
     * @param type
     * @return
     */
    @PutMapping("/social/bindSocialAccount")
    ResultMessage<String> bindSocialAccount(@RequestParam(value = "accountId") Long accountId, @RequestParam(value = "socialAccount") String socialAccount, @RequestParam(value = "type") String type);

    /**
     * 根据社交账号查询用户信息
     *
     * @param socialAccount
     * @param type
     * @return
     */
    @GetMapping("/social/queryAccountBySocail")
    ResultMessage<SysUserLoginInfoVO> queryAccountBySocail(@RequestParam(value = "socialAccount") String socialAccount, @RequestParam(value = "type") String type);
}
