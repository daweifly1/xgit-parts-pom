package cn.com.xgit.parts.auth.module.login.controller;

import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.account.facade.sys.UserInfoFacade;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.service.sys.SysAccountService;
import cn.com.xgit.parts.auth.account.web.BasicController;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysAccountDO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Slf4j
@RestController
@RequestMapping({"/auth"})
public class AuthController extends BasicController {
    @Autowired
    SysAccountService sysAccountService;

    @Autowired
    private UserInfoFacade userInfoFacade;

//    @RequestMapping(value = {"/login"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
//    public ResultMessage login(@RequestBody UserLoginVO userLoginVO, @RequestHeader("x-from-site") Integer site)
//            throws Exception {
//        Ref<String> userIdRef = new Ref("");
//        ErrorCode ret = this.accountService.login(userLoginVO, userIdRef, site);
//        return ResultMessage(ret, userIdRef.get());
//    }

    /**
     * 登录若失败一定次数返回验证码
     *
     * @param userLoginVO
     * @return
     */
//    @RequestMapping(value = {"/login"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
//    public ResultMessage<SysUserLoginInfoVO> login(@RequestBody UserLoginVO userLoginVO, HttpServletRequest request) {
//        try {
//            String ip = getRemoteIp(request);
//            SysUserLoginInfoVO ret = userInfoFacade.login(userLoginVO, ip);
//            return ResultMessage(ret);
//        } catch (AuthException e) {
//            log.info("", e);
//            return actionErrorResult(e.getCode(), e.getMessage());
//        } catch (Exception e) {
//            log.info("", e);
//            return actionErrorResult(e.getMessage());
//        }
//    }
    @RequestMapping(value = {"/login"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResultMessage<String> login(@RequestBody UserLoginVO userLoginVO, HttpServletRequest request) {
        try {
            String ip = getRemoteIp(request);
            SysUserLoginInfoVO ret = userInfoFacade.login(userLoginVO, ip);
            return ResultMessage(ret.getUserId());
        } catch (AuthException e) {
            log.info("", e);
            return actionErrorResult(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info("", e);
            return actionErrorResult(e.getMessage());
        }
    }

    @RequestMapping(value = {"/logout"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResultMessage logout(@RequestHeader("x-user-id") String userId) {
        return ResultMessage(userId);
    }


    @RequestMapping(value = {"/authInfo"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage<UserLoginVO> authInfo() {
        UserLoginVO r = BeanUtil.do2bo(userInfoFacade.createAuthInfo(), UserLoginVO.class);
        r.setCode(null);
        return ResultMessage(r);
    }


//    @RequestMapping(value = {"/authInfo"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
//    public ResultMessage authInfo() {
//        return ResultMessage(this.authService.createAuthInfo());
//    }

    @RequestMapping(value = {"/kaptcha"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public void kaptcharImg(@RequestParam("authid") String authId, HttpServletResponse httpServletResponse)
            throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

        BufferedImage verifyImg = userInfoFacade.createVerifyImg(authId);
        ImageIO.write(verifyImg, "jpg", jpegOutputStream);

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0L);
        httpServletResponse.setContentType("image/jpeg");

        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @RequestMapping(value = {"/password"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResultMessage password(@RequestHeader("x-user-id") String userId, @RequestBody UpdatePasswordVO updatePasswordVO) {
        ErrorCode ret = sysAccountService.updateChangePassword(updatePasswordVO, userId);
//        ErrorCode ret = this.accountService.updatePassword(userId, updatePasswordVO);
        return ResultMessage(ret);
    }


    @RequestMapping(value = {"/regist"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("注册用户")
    public ResultMessage<String> regist(@RequestBody UserRegistVO userRegistVO, HttpServletRequest request) {
        try {
            String ip = getRemoteIp(request);
            UserRegistVO ret = userInfoFacade.regist(userRegistVO, ip);
            return ResultMessage(ret.getSysAccountVO().getUserId());
        } catch (AuthException e) {
            log.info("", e);
            return actionErrorResult(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info("", e);
            return actionErrorResult("注册失败：" + e.getMessage());
        }
    }

    @RequestMapping(value = {"/addUser"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("用户添加用户")
    public ResultMessage<String> addUser(@RequestHeader("x-user-id") String userId, @RequestBody UserRegistVO userRegistVO) {
        try {
            SysAccountDO account = sysAccountService.queryById(userId);
            if (null != userRegistVO && null != userRegistVO.getSysAccountVO()) {
                userRegistVO.getSysAccountVO().setDeptId(account.getDeptId());
            }
            UserRegistVO ret = userInfoFacade.regist(userRegistVO, null);
            return ResultMessage(ret.getSysAccountVO().getUserId());
        } catch (AuthException e) {
            log.info("", e);
            return actionErrorResult(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info("", e);
            return actionErrorResult("用户添加用户失败：" + e.getMessage());
        }
    }
}
