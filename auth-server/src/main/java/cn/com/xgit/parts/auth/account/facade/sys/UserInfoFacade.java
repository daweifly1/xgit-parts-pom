package cn.com.xgit.parts.auth.account.facade.sys;

import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.account.infra.AuthConstant;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.manager.cache.RedisClient;
import cn.com.xgit.parts.auth.account.service.DepartmentService;
import cn.com.xgit.parts.auth.account.service.sys.SysAccountService;
import cn.com.xgit.parts.auth.account.service.sys.SysAuthsService;
import cn.com.xgit.parts.auth.account.service.sys.SysRoleAuthService;
import cn.com.xgit.parts.auth.account.service.sys.SysRoleService;
import cn.com.xgit.parts.auth.account.service.sys.SysUserRolesService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import cn.com.xgit.parts.auth.VO.DepartmentVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysAccountDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * 用户权限关系
 */
@Slf4j
@Service
public class UserInfoFacade {
    @Autowired
    private SysRoleAuthService sysRoleAuthService;

    @Autowired
    private SysUserRolesService sysUserRolesService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysAuthsService sysAuthsService;
//
//    @Autowired
//    AccountService accountService;
//
//    @Autowired
//    ProfileMapper profileMapper;
//
//    @Autowired
//    RoleMapper roleMapper;

    @Autowired
    DepartmentService departmentService;

    @Value("${password.verifyCodeClose:false}")
    private boolean verifyCodeClose;

    //密码输错超过该数字锁住
    @Value("${password.error.lock.times:10}")
    private int lockTimes;

    //密码输错一定次数，要先验证验证码
    @Value("${password.error.verify.times:3}")
    private int passwordErrorTimes;

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
        if (StringUtils.isBlank(userLoginVO.getLoginName())) {
            throw new AuthException("用户名不能为空");
        }
        if (StringUtils.isBlank(userLoginVO.getPassword())) {
            throw new AuthException("密码不能为空");
        }
        if (StringUtils.isNoneBlank(userLoginVO.getCode())) {
            boolean codeIsTrue = checkCode(userLoginVO);
            if (!codeIsTrue) {
                throw new AuthException("验证码错误");
            }
        }
        SysAccountDO accountDO = sysAccountService.queryByLoginName(userLoginVO.getLoginName());
        if (accountDO == null) {
            throw new AuthException("登录账号不存在");
        }
        if (accountDO.getLocked() == AuthConstant.USER_LOCKED) {
            throw new AuthException("用户状态被锁定");
        }

        if (null != accountDO.getPasswordErrorTimes() && accountDO.getPasswordErrorTimes() > passwordErrorTimes) {
            if (StringUtils.isBlank(userLoginVO.getCode())) {
                //错误大于一定次数后需要验证码
                throw new AuthException(ErrorCode.UserNeedValidateCode);
            }
        }
        boolean pass = sysAccountService.checkLoginPsw(accountDO.getUserId(), userLoginVO.getPassword());
        this.sysAccountService.updateLoginTime(accountDO, ip, pass);
        if (pass) {
            SysUserLoginInfoVO r = new SysUserLoginInfoVO();
            r.setUserId(accountDO.getUserId());
            r.setLoginName(accountDO.getLoginName());
            r.setName(accountDO.getName());
            return r;
        } else {
            throw new AuthException("密码错误");
        }
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
        if (StringUtils.isBlank(userRegistVO.getUserLoginVO().getPassword())) {
            userRegistVO.getUserLoginVO().setPassword("123456");
        }
        boolean codeIsTrue = checkCode(userRegistVO.getUserLoginVO());
        if (!codeIsTrue) {
            throw new AuthException("验证码错误");
        }
        SysAccountVO account = userRegistVO.getSysAccountVO();
        if (null != userRegistVO.getDepartmentVO()) {
            String depId = saveDepartment(userRegistVO.getDepartmentVO());
            account.setDeptId(depId);
            account.setDeptName(userRegistVO.getDepartmentVO().getName());
        }
        account.setLoginName(userRegistVO.getUserLoginVO().getLoginName());
        checkExistMobile(account.getMobile(), account.getUserId());
        ErrorCode ec = sysAccountService.save(account);
        if (ec.getCode() == ErrorCode.Success.getCode() && StringUtils.isNoneBlank(account.getUserId())) {
            sysAccountService.updatePassword(account.getUserId(), userRegistVO.getUserLoginVO().getPassword());
        } else {
            throw new AuthException(ec.getDesc());
        }
        return userRegistVO;
    }

    private String saveDepartment(DepartmentVO departmentVO) {
        if (StringUtils.isBlank(departmentVO.getId())) {
            departmentService.insert(departmentVO, "");
        }
        return departmentVO.getId();
    }

    public void checkExistMobile(String mobile, String userId) {
        if (StringUtils.isNotBlank(mobile)) {
            SysAccountDO profileDO = new SysAccountDO();
            profileDO.setMobile(mobile);
            profileDO.setUserId(userId);
            int count = sysAccountService.getCountByMobile(profileDO);
            if (count > 0) {
                throw new AuthException("手机号码已存在");
            }
        }
    }
}
