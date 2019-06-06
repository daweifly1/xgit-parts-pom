package cn.com.xgit.parts.auth.module.account.service;

import cn.com.xgit.parts.auth.enums.PasswordType;
import cn.com.xgit.parts.auth.exception.CommonException;
import cn.com.xgit.parts.auth.module.account.entity.SysAccount;
import cn.com.xgit.parts.auth.module.account.entity.SysPassword;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.base.SuperMapper;
import cn.com.xgit.parts.auth.module.base.SuperService;
import cn.com.xgit.parts.common.util.AccountValidatorUtil;
import cn.com.xgit.parts.common.util.security.CryptoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class SysAccountService extends SuperService<SuperMapper<SysAccount>, SysAccount> {

    @Autowired
    private SysPasswordService sysPasswordService;

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
        sysAccount.setLoginName(loginNameOrMobi);
        List<SysAccount> ll = super.list(new QueryWrapper<>(sysAccount));
        if (CollectionUtils.isNotEmpty(ll)) {
            return ll.get(0);
        }

        return null;
    }

    public boolean checkLoginPsw(Long userId, String password) {
        String dbNomalPsw = queryDbNomalPsw(userId);
        if (StringUtils.isNoneBlank(dbNomalPsw) && dbNomalPsw.equals(password)) {
            return true;
        }
        return false;
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
        sysAccount.setLoginName(account.getLoginName());
        List<SysAccount> ll = super.list(new QueryWrapper<>(sysAccount));
        if (CollectionUtils.isNotEmpty(ll)) {
            throw new CommonException("用户名已经存在！");
        }
        if (StringUtils.isNoneBlank(account.getMobile())) {
            sysAccount = new SysAccount();
            sysAccount.setLoginName(account.getMobile());
            ll = super.list(new QueryWrapper<>(sysAccount));
            if (CollectionUtils.isNotEmpty(ll)) {
                throw new CommonException("手机号码已经存在！");
            }
        }
    }


    private String cryptoPassword(String text, Long salt) {
        try {
            String orginalText = text + "_" + salt;
            byte[] cypherBytes = CryptoUtil.encryptMD5(orginalText.getBytes());
            String cypherText = new BigInteger(cypherBytes).toString(16);
            return cypherText;
        } catch (Exception e) {
            throw new CommonException("系统异常，加密出错");
        }
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
}
