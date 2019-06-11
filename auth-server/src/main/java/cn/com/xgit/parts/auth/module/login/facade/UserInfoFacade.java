package cn.com.xgit.parts.auth.module.login.facade;

import cn.com.xgit.parts.auth.account.infra.AuthConstant;
import cn.com.xgit.parts.auth.account.manager.cache.RedisClient;
import cn.com.xgit.parts.auth.enums.PasswordType;
import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.exception.CommonException;
import cn.com.xgit.parts.auth.exception.code.ErrorCode;
import cn.com.xgit.parts.auth.module.account.entity.SysAccount;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.auth.module.account.param.UserLoginVO;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.module.account.service.SysAccountService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.UUID;

/**
 * 用户登录、注册（包含数据库、缓存、第三方服务调用）
 */
@Slf4j
@Service
public class UserInfoFacade {
    @Value("${password.verifyCodeClose:false}")
    private boolean verifyCodeClose;

    //密码输错超过该数字锁住
    @Value("${password.error.lock.times:10}")
    private int lockTimes;

    //密码输错一定次数，要先验证验证码
    @Value("${password.error.verify.times:3}")
    private int passwordErrorTimes;

    //手机动态密码保存缓存前缀
    @Value("${password.dynamicPswPrefix:LoginByMoBi_}")
    private String dynamicPswPrefix;

    //手机动态密码保存缓存前缀
    @Value("${password.dynamicPswPrefix:30000}")
    private Long dynamicPswTime;

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Autowired
    RedisClient redisClient;

    @Autowired
    private SysAccountService sysAccountService;

    public UserLoginVO createAuthInfo() {
        UserLoginVO authInfoVO = new UserLoginVO();
        authInfoVO.setAuthId(UUID.randomUUID().toString());
        if (verifyCodeClose) {
            authInfoVO.setCode("1234");
        } else {
            authInfoVO.setCode(this.defaultKaptcha.createText());
        }
        redisClient.set(authInfoVO.getAuthId(), authInfoVO.getCode());
        return authInfoVO;
    }

    public BufferedImage createVerifyImg(String authId) {
        String text = redisClient.get(authId);
        return this.defaultKaptcha.createImage(text);
    }

    public SysUserLoginInfoVO login(UserLoginVO userLoginVO, String ip) {
        preLoginCheck(userLoginVO);
        SysAccount accountDO = sysAccountService.queryByLoginNameOrMobi(userLoginVO.getLoginName());
        if (accountDO == null) {
            throw new CommonException("用户不存在或者密码错误");
        }
        if (accountDO.getLocked() == AuthConstant.USER_LOCKED) {
            throw new CommonException("用户被锁定");
        }
        //非手机动态验证码时候，错误一定次数后要求输入验证码
        if (PasswordType.DYNAMIC.getType() != userLoginVO.getPswType() && null != accountDO.getPasswordErrorTimes() && accountDO.getPasswordErrorTimes() > passwordErrorTimes) {
            if (StringUtils.isBlank(userLoginVO.getCode())) {
                //错误大于一定次数后需要验证码
                throw new AuthException(ErrorCode.UserNeedValidateCode);
            }
        }
        boolean pass = checkeLoginPassword(accountDO.getId(), userLoginVO);
        this.sysAccountService.updateLoginTime(accountDO, ip, pass);
        if (pass) {
            SysUserLoginInfoVO r = new SysUserLoginInfoVO();
            r.setId(accountDO.getId());
            r.setLoginName(accountDO.getLoginName());
            r.setName(accountDO.getName());
            return r;
        } else {
            throw new CommonException("密码错误");
        }
    }

    private void preLoginCheck(UserLoginVO userLoginVO) {
        if (StringUtils.isBlank(userLoginVO.getLoginName())) {
            throw new CommonException("用户名不能为空");
        }
        if (StringUtils.isBlank(userLoginVO.getPassword())) {
            throw new CommonException("密码不能为空");
        }
        if (null == userLoginVO.getPswType()) {
            //默认密码方式
            userLoginVO.setPswType(PasswordType.NORMAL.getType());
        }
        if (StringUtils.isNoneBlank(userLoginVO.getCode())) {
            boolean codeIsTrue = checkCode(userLoginVO);
            if (!codeIsTrue) {
                throw new CommonException("验证码错误");
            }
        }
    }

    private boolean checkeLoginPassword(Long userId, UserLoginVO userLoginVO) {
        if (PasswordType.DYNAMIC.getType() == userLoginVO.getPswType()) {
            String psw = redisClient.get(dynamicPswPrefix + userLoginVO.getLoginName());
            if (StringUtils.isNoneBlank(psw) && psw.equals(userLoginVO.getPassword())) {
                return true;
            }
            return false;
        }
        return sysAccountService.checkLoginPsw(userId, userLoginVO.getPassword());
    }

    private boolean checkCode(UserLoginVO userLoginVO) {
        if (null == userLoginVO || null == userLoginVO.getAuthId()) {
            return false;
        }
        String code = redisClient.get(userLoginVO.getAuthId());
        if (StringUtils.isNoneBlank(code) && userLoginVO.getCode().equals(code)) {
            redisClient.delete(userLoginVO.getAuthId());
            return true;
        }
        return false;
    }

    @Transactional
    public UserRegistVO regist(UserRegistVO userRegistVO, String ip) {
        if (null == userRegistVO || null == userRegistVO.getSysAccountVO() || null == userRegistVO.getUserLoginVO()
                || StringUtils.isBlank(userRegistVO.getUserLoginVO().getLoginName())) {
            throw new AuthException("注册信息不能为空");
        }
        boolean codeIsTrue = checkCode(userRegistVO.getUserLoginVO());
        if (!codeIsTrue) {
            throw new AuthException("验证码错误");
        }
        sysAccountService.addRegistUser(userRegistVO);
        return userRegistVO;
    }

    public boolean sendMobiPsw(UserLoginVO userLoginVO) {
        if (StringUtils.isBlank(userLoginVO.getCode())) {
            throw new CommonException("验证码错误");
        }
        //校验验证码
        boolean codeIsTrue = checkCode(userLoginVO);
        if (!codeIsTrue) {
            throw new CommonException("验证码错误");
        }
        String value = getRandomString(6);
        //TODO 发送短信
        redisClient.set(dynamicPswPrefix + userLoginVO.getLoginName(), value, dynamicPswTime);
        return true;
    }

    public String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
