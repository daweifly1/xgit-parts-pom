package cn.com.xgit.gw.social.github.controller;

import cn.com.xgit.gw.security.common.beans.CommonUserDetails;
import cn.com.xgit.gw.security.jwt.TokenAuthenticationHandler;
import cn.com.xgit.gw.social.enums.OAuthTypes;
import cn.com.xgit.gw.social.github.GitHubProperties;
import cn.com.xgit.gw.social.github.service.GitHubService;
import cn.com.xgit.parts.auth.feign.AuthClient;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * github社交登录后回调，根据回调判断是绑定还是登录
 */
@Controller
public class GitHubCallBackController {

    @Autowired
    private GitHubProperties gitHubProperties;

    @Autowired
    private TokenAuthenticationHandler tokenAuthenticationHandler;

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private AuthClient authClient;

    @RequestMapping("/connect/githubPage")
    public String githubPage(Model model) {
        String url = "https://github.com/login/oauth/authorize?client_id=" + gitHubProperties.getAppId() + "&state=" + UUID.randomUUID();
        model.addAttribute("githubUrl", url);
        if (null != SecurityContextHolder.getContext().getAuthentication()) {
            //如果用户已经登录，跳转到绑定github账号页面
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (null != principal && principal instanceof CommonUserDetails) {
                if (StringUtils.isNoneBlank(gitHubProperties.getBindGitHubUrl())) {
                    return "redirect:" + gitHubProperties.getBindGitHubUrl();
                }
                return "github/bindGithub";
            }
        }
        return "github/loginGithub";
    }


    @RequestMapping("/connect/github")
    public String callback(String code, String state, Model model, HttpServletResponse response) {
        Map<String, String> gitHubLoginInfo = gitHubService.queryGitHubLoginInfo(code, state);
        if (MapUtils.isEmpty(gitHubLoginInfo) || StringUtils.isBlank(gitHubLoginInfo.get("login"))) {
            return "503";
        }
        if (null != SecurityContextHolder.getContext().getAuthentication()) {
            //如果已经登录则绑定用户
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (null != principal && principal instanceof CommonUserDetails) {
                CommonUserDetails commonUserDetails = (CommonUserDetails) principal;
                if (null == commonUserDetails || null == commonUserDetails.getId() || org.apache.commons.lang.StringUtils.isBlank(commonUserDetails.getUsername())) {
                    return "503";
                }
                ResultMessage<String> rsp = authClient.bindSocialAccount(commonUserDetails.getId(), gitHubLoginInfo.get("login"), OAuthTypes.GITHUB.getCode());
                if (null != rsp && ResultMessage.OK == rsp.getStatus()) {
                    if (StringUtils.isNoneBlank(gitHubProperties.getBindGitHubSuccessUrl())) {
                        return "redirect:" + gitHubProperties.getBindGitHubSuccessUrl();
                    }
                    return "github/bindGithubSuc";
                }

                if (StringUtils.isNoneBlank(gitHubProperties.getBindGitHubFaileUrl())) {
                    return "redirect:" + gitHubProperties.getBindGitHubFaileUrl();
                }
                if (null != rsp) {
                    model.addAttribute("error", rsp.getMessage());
                }
                return "github/bindGithubFail";
            }
        }
        //如果未登录，查询是否有绑定信息，有绑定进行登录，无则跳转到登录页面；
        ResultMessage<SysUserLoginInfoVO> rsp = authClient.queryAccountBySocail(gitHubLoginInfo.get("login"), OAuthTypes.GITHUB.getCode());
        if (null != rsp && null != rsp.getData()) {
            SysUserLoginInfoVO sysUserLoginInfoVO = rsp.getData();
            //登陆成功
            CommonUserDetails commonUserDetails = new CommonUserDetails();
            commonUserDetails.setId(sysUserLoginInfoVO.getId());
            commonUserDetails.setUsername(sysUserLoginInfoVO.getUsername());
            commonUserDetails.setRoleIds(sysUserLoginInfoVO.getRoleIds());
            if (null != commonUserDetails) {
                tokenAuthenticationHandler.saveAfterLogin(commonUserDetails, response);
            }
            if (StringUtils.isNoneBlank(gitHubProperties.getLoginGitHubSuccessUrl())) {
                return "redirect:" + gitHubProperties.getLoginGitHubSuccessUrl();
            }
            return "redirect:/actuator/info";
        }
        //登录失败显示登录失败原因
        if (StringUtils.isNoneBlank(gitHubProperties.getLoginGitHubFailUrl())) {
            return "redirect:" + gitHubProperties.getLoginGitHubFailUrl() + "?error=" + (null != rsp ? rsp.getMessage() : "");
        }
        model.addAttribute("error", (null != rsp ? rsp.getMessage() : ""));
        return "github/loginError";
    }
}
