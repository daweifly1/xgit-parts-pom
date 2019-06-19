package cn.com.xgit.parts.auth.module.account.service;

import cn.com.xgit.parts.auth.common.base.SuperMapper;
import cn.com.xgit.parts.auth.common.base.SuperService;
import cn.com.xgit.parts.auth.enums.PasswordType;
import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.exception.CommonException;
import cn.com.xgit.parts.auth.exception.code.ErrorCode;
import cn.com.xgit.parts.auth.module.account.entity.SysAccount;
import cn.com.xgit.parts.auth.module.account.entity.SysAccountRole;
import cn.com.xgit.parts.auth.module.account.entity.SysPassword;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.account.vo.SysPasswordVO;
import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import cn.com.xgit.parts.auth.module.role.entity.SysRole;
import cn.com.xgit.parts.common.util.AccountValidatorUtil;
import cn.com.xgit.parts.common.util.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


@Slf4j
@Service
public class SysAccountService extends SuperService<SuperMapper<SysAccount>, SysAccount> {

    @Value("${password.defaultPsw:123456}")
    private String defaultPsw;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SysPasswordService sysPasswordService;

    @Autowired
    private SysAccountRoleService sysAccountRoleService;

    public SysAccount queryByLoginNameOrMobi(String loginNameOrMobi) {
        SysAccount sysAccount = new SysAccount();
        if (AccountValidatorUtil.isMobile(loginNameOrMobi)) {
            sysAccount.setMobile(loginNameOrMobi);
            List<SysAccount> ll = super.list(new QueryWrapper<>(sysAccount));
            if (CollectionUtils.isNotEmpty(ll)) {
                return ll.get(0);
            }
        }
        sysAccount = new SysAccount();
        sysAccount.setUsername(loginNameOrMobi);
        List<SysAccount> ll = super.list(new QueryWrapper<>(sysAccount));
        if (CollectionUtils.isNotEmpty(ll)) {
            return ll.get(0);
        }

        return null;
    }

    public boolean checkLoginPsw(Long userId, String password) {
        String dbNomalPsw = queryDbNomalPsw(userId);
        return StringUtils.isNoneBlank(dbNomalPsw) && dbNomalPsw.equals(cryptoPassword(password, userId));
    }

    public String queryDbNomalPsw(Long userId) {
        SysPassword sysPassword = new SysPassword();
        sysPassword.setUserId(userId);
        sysPassword.setType(PasswordType.NORMAL.getType());
        List<SysPassword> ll = sysPasswordService.list(new QueryWrapper<>(sysPassword));
        log.info(" checkLoginPsw ：{}", ll);
        if (CollectionUtils.isNotEmpty(ll) && 1 == ll.size()) {
            if (null != ll.get(0) && StringUtils.isNoneBlank(ll.get(0).getPassword())) {
                return ll.get(0).getPassword();
            }
        }
        return null;
    }

    public boolean saveRegist(SysAccountVO account) {
        checkExistMobileOrLoginName(account);
        if (!super.insertByVO(account)) {
            throw new CommonException("注册用户失败！");
        }
        return true;
    }

    private void checkExistMobileOrLoginName(SysAccountVO account) {
        SysAccount sysAccount = new SysAccount();
        sysAccount.setUsername(account.getUsername());
        List<SysAccount> ll = super.list(new QueryWrapper<>(sysAccount));
        if (CollectionUtils.isNotEmpty(ll)) {
            throw new CommonException("用户名已经存在！");
        }
        if (StringUtils.isNoneBlank(account.getMobile())) {
            sysAccount = new SysAccount();
            sysAccount.setUsername(account.getMobile());
            ll = super.list(new QueryWrapper<>(sysAccount));
            if (CollectionUtils.isNotEmpty(ll)) {
                throw new CommonException("手机号码已经存在！");
            }
        }
    }


    private String cryptoPassword(String text, Long salt) {
        return passwordEncoder.encode(text);
    }

    public void updateLoginTime(SysAccount account, String ip, boolean pass) {
        if (pass) {
            account.setLastLoginIp(ip);
            account.setLastLoginTime(new Date());
            account.setPasswordErrorTimes(0);
        } else {
            account.setLastLoginIp(ip);
            account.setLastLoginTime(new Date());
            account.setPasswordErrorTimes(account.getPasswordErrorTimes() + 1);
        }
        super.updateById(account);
    }

    public void saveRegistPassword(Long userId, String password) {
        SysPassword sysPassword = new SysPassword();
        sysPassword.setUserId(userId);
        sysPassword.setType(PasswordType.NORMAL.getType());
        sysPassword.setPassword(cryptoPassword(password, userId));
        if (!sysPasswordService.save(sysPassword)) {
            throw new CommonException("系统异常，设置密码错误");
        }
    }

    @Transactional
    public boolean removeAccountByUserId(Long userId) {
        boolean r = super.removeById(userId);
        if (r) {
            //删除密码信息
            SysPassword sysPassword = new SysPassword();
            sysPassword.setUserId(userId);
            r = sysPasswordService.remove(new QueryWrapper<>(sysPassword));
            if (r) {
                SysAccountRole sysAccountRole = new SysAccountRole();
                sysAccountRole.setUserId(userId);
                r = sysAccountRoleService.remove(new QueryWrapper<>(sysAccountRole));
            }
        }
        if (!r) {
            throw new CommonException("删除失败");
        }
        return r;
    }

    public ErrorCode updatePassword(SysPasswordVO sysPasswordVO) {
        SysPassword sysPassword = new SysPassword();
        sysPassword.setUserId(sysPasswordVO.getUserId());
        if (PasswordType.NORMAL.contain(sysPasswordVO.getType())) {
            sysPassword.setType(sysPasswordVO.getType());
        } else {
            sysPassword.setType(PasswordType.NORMAL.getType());
        }

        List<SysPassword> ll = sysPasswordService.list(new QueryWrapper<>(sysPassword));
        if (CollectionUtils.isEmpty(ll)) {
            return ErrorCode.Failure;
        }
        sysPassword = ll.get(0);
        sysPassword.setPassword(cryptoPassword(sysPasswordVO.getPassword(), sysPasswordVO.getUserId()));
        sysPasswordService.updateById(sysPassword);
        return ErrorCode.Success;
    }

    @Transactional
    public ErrorCode resetPassword(List<Long> userIds) {
        for (Long userId : userIds) {
            SysPasswordVO sysPasswordVO = new SysPasswordVO();
            sysPasswordVO.setUserId(userId);
            sysPasswordVO.setPassword(defaultPsw);
            ErrorCode r = updatePassword(sysPasswordVO);
            if (ErrorCode.Success != r) {
                throw new CommonException("初始密码失败");
            }
        }
        return ErrorCode.Success;
    }

    public ErrorCode checkLoginName(String loginName) {
        SysAccount sysAccount = queryByLoginNameOrMobi(loginName);
        if (null == sysAccount) {
            return ErrorCode.Success;
        }
        return ErrorCode.UserNameExists;
    }

    public SysAccountVO queryAccountByIdOrLoginName(Long userId, String loginName, Long platformId) {
        SysAccount sysAccount = null;
        SysAccountVO sysAccountVO = null;
        if (null != userId) {
            sysAccount = this.getById(userId);
        }
        if (null == sysAccount) {
            sysAccount = queryByLoginNameOrMobi(loginName);
        }
        sysAccountVO = BeanUtil.do2bo(sysAccount, SysAccountVO.class);
        if (null != sysAccountVO && null != platformId) {
            //根据用户id、平台查询角色列表
            List<SysRole> roles = sysAccountRoleService.querRolesByUserId(platformId, sysAccount.getId());
            sysAccountVO.setRoles(BeanUtil.do2bo4List(roles, SysRoleVO.class));
        }
        return sysAccountVO;
    }

    @Transactional
    public void addRegistUser(UserRegistVO userRegistVO) {
        if (StringUtils.isBlank(userRegistVO.getUserLoginVO().getPassword())) {
            userRegistVO.getUserLoginVO().setPassword("123456");
        }
        SysAccountVO account = userRegistVO.getSysAccountVO();
        account.setUsername(userRegistVO.getUserLoginVO().getUsername());
        boolean ec = saveRegist(account);
        if (ec && null != account.getId()) {
            saveRegistPassword(account.getId(), userRegistVO.getUserLoginVO().getPassword());
            if (CollectionUtils.isNotEmpty(account.getRoles())) {
                List<SysAccountRole> rs = getRolesFromRoles(account.getRoles(), account.getId());
                sysAccountRoleService.saveBatch(rs);
            }
        } else {
            throw new AuthException("注册失败，保存错误");
        }
    }

    /**
     * 若注册时候指定了角色，则将角色与用户关联(不校验角色平台是否存在)
     *
     * @param roles
     */
    private List<SysAccountRole> getRolesFromRoles(List<SysRoleVO> roles, Long userId) {
        List<SysAccountRole> r = new ArrayList<>(roles.size());
        for (SysRoleVO vo : roles) {
            if (null == vo || null == vo.getId() || null == vo.getPlatformId()) {
                throw new AuthException("保存用户角色信息错误");
            }
            SysAccountRole sysAccountRole = new SysAccountRole();
            sysAccountRole.setPlatformId(vo.getPlatformId());
            sysAccountRole.setUserId(userId);
            sysAccountRole.setRoleId(vo.getId());
            r.add(sysAccountRole);
        }
        return r;
    }

    public SysUserLoginInfoVO queryAccountByUserNameOrMobi(String account, Long platformId, Boolean dynamicPsw) {
        SysAccount ddo = null;
        SysAccount sysAccount = new SysAccount();
        if (AccountValidatorUtil.isMobile(account)) {
            sysAccount.setMobile(account);
            List<SysAccount> ll = super.list(new QueryWrapper<>(sysAccount));
            if (CollectionUtils.isNotEmpty(ll)) {
                ddo = ll.get(0);
            }
        }
        if (null == ddo) {
            sysAccount = new SysAccount();
            sysAccount.setUsername(account);
            List<SysAccount> ll = super.list(new QueryWrapper<>(sysAccount));
            if (CollectionUtils.isNotEmpty(ll)) {
                ddo = ll.get(0);
            }
        }
        if (null == ddo) {
            return null;
        }
        SysUserLoginInfoVO sysUserLoginInfoVO = new SysUserLoginInfoVO();
        sysUserLoginInfoVO.setId(ddo.getId());
        sysUserLoginInfoVO.setUsername(ddo.getUsername());
        sysUserLoginInfoVO.setName(ddo.getName());
        if (dynamicPsw) {
            sysUserLoginInfoVO.setPassword(queryDbNomalPsw(ddo.getId()));
        } else {
            sysUserLoginInfoVO.setPassword(queryDbNomalPsw(ddo.getId()));
        }
        sysUserLoginInfoVO.setRoleIds(new HashSet<>(sysAccountRoleService.querRoleIdsByUserId(null, ddo.getId())));
        return sysUserLoginInfoVO;
    }
}
