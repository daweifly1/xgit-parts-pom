package cn.com.xgit.parts.auth.account.web;

import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.service.ProfileService;
import cn.com.xgit.parts.auth.account.service.ThirdAuthService;
import cn.com.xgit.parts.auth.account.web.wechat.WxMpServiceFactory;
import cn.com.xgit.parts.auth.account.web.wechat.WxUserFastWrapper;
import cn.com.xgit.parts.auth.VO.ProfileVO;
import cn.com.xgit.parts.auth.account.web.wechat.WechatService;
import com.xgit.bj.core.Ref;
import com.xgit.bj.core.rsp.ResultMessage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping({"/thirdAuth"})
public class ThirdAuthController {
    private static final String authMap = "/thirdAuth/webAuth/";
    @Autowired
    private WxMpServiceFactory wxMpServiceFactory;
    @Autowired
    private WechatService wechatService;
    @Autowired
    private ThirdAuthService thirdAuthService;
    @Autowired
    private ProfileService profileService;
    @Value("${ufast.wechat.authHost:http://192.168.1.149:9008}")
    private String authHost;
    @Value("${ufast.wechat.loginUrl:http://192.168.1.149:9008}")
    private String loginUrl;
    @Value("${ufast.wechat.registerUrl:http://192.168.1.149:9008}")
    private String registerUrl;
    @Value("${ufast.wechat.defaultRole:1}")
    private String defaultRole;

    @ApiOperation("授权后获取用户信息")
    @RequestMapping(value = {"/webAuth/{type}/{appId}"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public ResultMessage webAuth(@PathVariable String type, @PathVariable String appId, @RequestParam String code)
            throws Exception {
        if ("wechat".equals(type)) {
            return wechatWebAuth(appId, code);
        }
        throw new AuthException(ErrorCode.ThirdAuthTypeError.getCode(), ErrorCode.ThirdAuthTypeError.getDesc());
    }

    @ApiOperation("获取第三方用户信息，正常情况应该在gateway从session取得直接返回")
    @RequestMapping(value = {"/getUserInfo"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public ResultMessage getUserInfo(@RequestParam String type, @RequestParam String appId) {
        if (StringUtils.isBlank(appId)) {
            return new ResultMessage(ErrorCode.IllegalArument.getCode(), ErrorCode.ThirdAuthTypeError.getDesc());
        }
        log.debug("appId: " + appId);
        if ("wechat".equals(type)) {
            String redirectUrl = this.authHost + "/thirdAuth/webAuth/" + "wechat" + "/" + appId;
            String authUrl = this.wxMpServiceFactory.getService(appId).oauth2buildAuthorizationUrl(redirectUrl, "snsapi_userinfo", null);
            log.debug("authUrl: " + authUrl);

            return new ResultMessage(ErrorCode.NeedLogin.getCode(), ErrorCode.NeedLogin.getDesc(), authUrl);
        }
        throw new AuthException(ErrorCode.ThirdAuthTypeError.getCode(), ErrorCode.ThirdAuthTypeError.getDesc());
    }

    @ApiOperation("第三方绑定已有账号")
    @RequestMapping(value = {"/bindAccount"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public ResultMessage bindExistingAccount(@RequestHeader("x-user-id") String userId, @RequestBody WxMpUser wxMpUser, @RequestParam String type, @RequestParam String appId) {
        if (this.thirdAuthService.isUserIdAlreadyBind(userId, type)) {
            return new ResultMessage(ErrorCode.userIdAlreadyBind.getCode(), ErrorCode.userIdAlreadyBind.getDesc());
        }
        if (this.thirdAuthService.isThirdIdAlreadyBind(wxMpUser.getOpenId())) {
            return new ResultMessage(ErrorCode.thirdIdAlreadyBind.getCode(), ErrorCode.thirdIdAlreadyBind.getDesc());
        }
        this.thirdAuthService.bindAccount(appId, userId, wxMpUser.getOpenId(), type, wxMpUser);
        return new ResultMessage();
    }

    @ApiOperation("创建新账号并与第三方绑定")
    @RequestMapping(value = {"/registerAccount"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    @Transactional(rollbackFor = {Exception.class})
    public ResultMessage registerNewAccount(@RequestBody WxMpUser wxMpUser, @RequestParam String type, @RequestParam String appId)
            throws Exception {
        if (this.thirdAuthService.isThirdIdAlreadyBind(wxMpUser.getOpenId())) {
            return new ResultMessage(ErrorCode.thirdIdAlreadyBind.getCode(), ErrorCode.thirdIdAlreadyBind.getDesc());
        }
        ProfileVO profileVO = this.wechatService.generateProfile(this.defaultRole, wxMpUser);
        Ref<String> userIdRef = new Ref("");
        ErrorCode errorCode = this.profileService.insert(profileVO, userIdRef);
        if (ErrorCode.Success != errorCode) {
            return new ResultMessage(errorCode.getCode(), errorCode.getDesc());
        }
        this.thirdAuthService.bindAccount(appId, (String) userIdRef.get(), wxMpUser.getOpenId(), type, wxMpUser);

        WxUserFastWrapper wxUserFastWrapper = new WxUserFastWrapper();
        wxUserFastWrapper.setWxMpUser(wxMpUser);
        wxUserFastWrapper.setUserId((String) userIdRef.get());
        wxUserFastWrapper.setRedirectUrl(this.loginUrl);
        return new ResultMessage(wxUserFastWrapper);
    }

    private ResultMessage wechatWebAuth(String appId, String code)
            throws Exception {
        log.debug("wechat网页授权, appId: " + appId + ", code: " + code);

        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = this.wxMpServiceFactory.getService(appId).oauth2getAccessToken(code);
        WxMpUser wxMpUser = this.wxMpServiceFactory.getService(appId).oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        log.debug("wechat网页授权, 用户信息: " + wxMpUser);

        String userId = this.wechatService.queryUserIdByWechat(wxMpUser);

        WxUserFastWrapper wxUserFastWrapper = new WxUserFastWrapper();
        wxUserFastWrapper.setWxMpUser(wxMpUser);
        if (StringUtils.isBlank(userId)) {
            wxUserFastWrapper.setRedirectUrl(this.registerUrl);
        } else {
            wxUserFastWrapper.setRedirectUrl(this.loginUrl);
            wxUserFastWrapper.setUserId(userId);
        }
        return new ResultMessage(wxUserFastWrapper);
    }
}
