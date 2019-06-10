package cn.com.xgit.parts.auth.module.login.controller;

import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.auth.module.account.param.UserLoginVO;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.common.base.BasicController;
import cn.com.xgit.parts.auth.module.login.facade.UserInfoFacade;
import cn.com.xgit.parts.common.result.ResultMessage;
import cn.com.xgit.parts.common.util.BeanUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

/**
 * 登录、注册相关（不需要权限限制）
 */
@Slf4j
@RestController
@RequestMapping({"/auth"})
public class AuthController extends BasicController {
    @Autowired
    private UserInfoFacade userInfoFacade;

    /**
     * 登录若失败一定次数返回验证码
     *
     * @param userLoginVO
     * @return
     */
    @PostMapping(value = {"/login"})
    public ResultMessage<SysUserLoginInfoVO> login(@RequestBody UserLoginVO userLoginVO, HttpServletRequest request) {
        try {
            String ip = getRemoteIp(request);
            SysUserLoginInfoVO ret = userInfoFacade.login(userLoginVO, ip);
            return ResultMessage(ret);
        } catch (AuthException e) {
            log.info("", e);
            return actionErrorResult(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info("", e);
            return actionErrorResult(e.getMessage());
        }
    }

    @PostMapping(value = {"/sendMobiPsw"})
    public ResultMessage<String> sendMobiPsw(@RequestBody UserLoginVO userLoginVO) {
        boolean b = userInfoFacade.sendMobiPsw(userLoginVO);
        if (b) {
            return ResultMessage.success();
        }
        return ResultMessage.error("短信发送失败");
    }

    @RequestMapping(value = {"/logout"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResultMessage logout(@RequestHeader("x-user-id") String userId) {
        return ResultMessage.success(userId);
    }


    @RequestMapping(value = {"/authInfo"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage<UserLoginVO> authInfo() {
        UserLoginVO r = BeanUtil.do2bo(userInfoFacade.createAuthInfo(), UserLoginVO.class);
        r.setCode(null);
        return ResultMessage.success(r);
    }

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


    @RequestMapping(value = {"/regist"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("注册用户")
    public ResultMessage<String> regist(@RequestBody UserRegistVO userRegistVO, HttpServletRequest request) {
        try {
            String ip = getRemoteIp(request);
            UserRegistVO ret = userInfoFacade.regist(userRegistVO, ip);
            return ResultMessage.success("注册成功");
        } catch (AuthException e) {
            log.info("", e);
            return ResultMessage.success(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info("", e);
            return ResultMessage.success("注册失败：" + e.getMessage());
        }
    }

//    @RequestMapping(value = {"/addUser"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
//    @ApiOperation("用户添加用户")
//    public ResultMessage<String> addUser(@RequestHeader("x-user-id") Long userId, @RequestBody UserRegistVO userRegistVO) {
//        try {
//            UserRegistVO ret = userInfoFacade.regist(userRegistVO, null);
//            return ResultMessage.success();
//        } catch (AuthException e) {
//            log.info("", e);
//            return actionErrorResult(e.getCode(), e.getMessage());
//        } catch (Exception e) {
//            log.info("", e);
//            return actionErrorResult("用户添加用户失败：" + e.getMessage());
//        }
//    }
//@RequestMapping(value = {"/password"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
//public ResultMessage password(@RequestHeader("x-user-id") Long userId, @RequestBody UpdatePasswordVO updatePasswordVO) {
//    ErrorCode ret = sysAccountService.updateChangePassword(updatePasswordVO, userId);
//    return ResultMessage.success(ret);
//}
}