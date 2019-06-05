package cn.com.xgit.parts.auth.account.service;

import cn.com.xgit.parts.auth.account.manager.AuthInfoManager;
import cn.com.xgit.parts.auth.VO.AccountPwdVO;
import cn.com.xgit.parts.auth.VO.AccountVO;
import cn.com.xgit.parts.auth.VO.PasswordVO;
import cn.com.xgit.parts.auth.VO.TemplateVO;
import cn.com.xgit.parts.auth.VO.UpdatePasswordVO;
import cn.com.xgit.parts.auth.account.dao.entity.AccountDO;
import cn.com.xgit.parts.auth.account.dao.entity.PasswordDO;
import cn.com.xgit.parts.auth.account.dao.entity.PasswordDOKey;
import cn.com.xgit.parts.auth.account.dao.entity.ProfileDO;
import cn.com.xgit.parts.auth.account.dao.mapper.AccountMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.PasswordMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.ProfileMapper;
import cn.com.xgit.parts.auth.account.infra.AuthConstant;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.common.util.security.CryptoUtil;
import com.xgit.bj.core.Ref;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class AccountService {
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    ProfileService profileService;
    @Autowired
    PasswordMapper passwordMapper;
    @Autowired
    ProfileMapper profileMapper;
    @Autowired
    AuthInfoManager authInfoManager;
    @Autowired
    TemplateService templateService;

    private String cryptoPassword(String text, String salt) throws Exception {
        String orginalText = text + "_" + salt;
        byte[] cypherBytes = CryptoUtil.encryptMD5(orginalText.getBytes());
        String cypherText = new BigInteger(cypherBytes).toString(16);
        return cypherText;
    }

    public ErrorCode login(UserLoginVO userLoginVO, Ref<String> userIdRef, Integer site)
            throws Exception {
        if (StringUtils.isBlank(userLoginVO.getCode())) {
            log.error("验证码为空！", ErrorCode.FailedToVerifyCode.getCode());
            return ErrorCode.FailedToVerifyCode;
        }
        String verifyCode = this.authInfoManager.getVerifyCode(userLoginVO.getAuthId());
        if (!userLoginVO.getCode().equalsIgnoreCase(verifyCode)) {
            log.error("验证码错误！", ErrorCode.FailedToVerifyCode.getCode());
            return ErrorCode.FailedToVerifyCode;
        }
        AccountDO accountDO = this.accountMapper.selectByPrimaryKey(userLoginVO.getLoginName());
        if (accountDO == null) {
            log.error("登录账号不存在！", ErrorCode.CheckLoginFailure.getCode());
            return ErrorCode.CheckLoginFailure;
        }
        PasswordDOKey passwordDOKey = new PasswordDOKey();
        passwordDOKey.setUserId(accountDO.getUserId());
        passwordDOKey.setType(Integer.valueOf(0));
        PasswordDO passwordDO = this.passwordMapper.selectByPrimaryKey(passwordDOKey);
        if (passwordDO == null) {
            log.error("登录密码不存在！", ErrorCode.CheckLoginFailure.getCode());
            return ErrorCode.CheckLoginFailure;
        }
        ProfileDO profileDO = this.profileMapper.selectById(passwordDO.getUserId());
        if ((profileDO == null) || (profileDO.getLocked() == AuthConstant.USER_LOCKED)) {
            log.error("用户状态被锁！", ErrorCode.UserLocked.getCode());
            return ErrorCode.UserLocked;
        }
        String cypherPassword = cryptoPassword(userLoginVO.getPassword(), accountDO.getUserId());
        if (!cypherPassword.equals(passwordDO.getPassword())) {
            log.error("登录密码错误！", ErrorCode.CheckLoginFailure.getCode());
            return ErrorCode.CheckLoginFailure;
        }
        if (!checkUserOnSite(accountDO.getUserId(), site)) {
            log.error(ErrorCode.NoPermissionForThisSite.getDesc(), ErrorCode.CheckLoginFailure.getCode());
            return ErrorCode.NoPermissionForThisSite;
        }
        this.accountMapper.updateLoginTime(accountDO.getUserId());

        userIdRef.set(accountDO.getUserId());
        return ErrorCode.Success;
    }

    public boolean checkUserOnSite(String userId, Integer site) {
        TemplateVO templateVO = this.templateService.queryTempByUserId(userId);
        if ((null == templateVO) || (site != templateVO.getSite())) {
            return false;
        }
        return true;
    }

    public ErrorCode addAccountPwd(AccountPwdVO accountPwdVO) throws Exception {
        ErrorCode ret = addAccount(accountPwdVO);
        if (ret != ErrorCode.Success) {
            log.error(ret.getDesc(), ret.getCode());
            return ret;
        }
        ErrorCode insertRet = insertPassword(accountPwdVO);
        if (insertRet != ErrorCode.Success) {
            log.error(insertRet.getDesc(), insertRet.getCode());
            return insertRet;
        }
        return ErrorCode.Success;
    }

    public ErrorCode addAccount(AccountVO accountVO) {
        int ret = this.accountMapper.checkByPrimaryKey(accountVO.getLoginName());
        if (ret > 0) {
            log.error(ErrorCode.UserNameExists.getDesc(), ErrorCode.UserNameExists.getCode());
            return ErrorCode.UserNameExists;
        }
        AccountDO accountDO = new AccountDO();
        accountDO.setUserId(accountVO.getUserId());
        accountDO.setLoginName(accountVO.getLoginName());
        accountDO.setStatus(accountVO.getStatus());
        accountDO.setLastLoginTime(null);

        ret = this.accountMapper.insertSelective(accountDO);
        if (ret <= 0) {
            log.error("新增账号失败！", ErrorCode.FailedToInsertRecord.getCode());
            return ErrorCode.FailedToInsertRecord;
        }
        return ErrorCode.Success;
    }

    public ErrorCode removeAccount(String loginName) {
        AccountDO accountDO = this.accountMapper.selectByPrimaryKey(loginName);
        if (accountDO == null) {
            log.error("目标数据不存在！", ErrorCode.Success.getCode());
            return ErrorCode.Success;
        }
        int ret = this.accountMapper.deleteByPrimaryKey(loginName);
        if (ret <= 0) {
            log.error("删除登录账号失败！", ErrorCode.FailedToRemoveRecord.getCode());
            return ErrorCode.FailedToRemoveRecord;
        }
        ret = this.accountMapper.checkByUserId(accountDO.getUserId());
        if (ret > 0) {
            return ErrorCode.Success;
        }
        removePassword(accountDO.getUserId(), Integer.valueOf(0));
        return ErrorCode.Success;
    }

    public ErrorCode removeAccountByUserId(String userId) {
        if (ErrorCode.Success != rmAccountByUserId(userId)) {
            log.error("删除用户登录账户失败！", ErrorCode.FailedToRemoveRecord.getCode());
            return ErrorCode.FailedToRemoveRecord;
        }
        return removePassword(userId, Integer.valueOf(0));
    }

    public ErrorCode rmAccountByUserId(String userId) {
        this.accountMapper.deleteByUserId(userId);
        return ErrorCode.Success;
    }

    public ErrorCode removePassword(String userId, Integer type) {
        PasswordDOKey passwordDOKey = new PasswordDOKey();
        passwordDOKey.setUserId(userId);
        passwordDOKey.setType(type);
        this.passwordMapper.deleteByPrimaryKey(passwordDOKey);

        return ErrorCode.Success;
    }

    public ErrorCode removeAllPassword(String userId) {
        return removePassword(userId, Integer.valueOf(0));
    }

    private ErrorCode insertPassword(PasswordDO passwordDO) throws Exception {
        int ret = this.passwordMapper.checkByPrimaryKey(passwordDO);
        if (ret > 0) {
            log.error(ErrorCode.PwdAlreadSet.getDesc(), ErrorCode.PwdAlreadSet.getCode());
            return ErrorCode.PwdAlreadSet;
        }
        String cypherPassword = cryptoPassword(passwordDO.getPassword(), passwordDO.getUserId());
        passwordDO.setPassword(cypherPassword);
        ret = this.passwordMapper.insert(passwordDO);
        if (ret <= 0) {
            log.error("添加用户密码信息失败！", ErrorCode.FailedToInsertRecord.getCode());
            return ErrorCode.FailedToInsertRecord;
        }
        return ErrorCode.Success;
    }

    public ErrorCode insertPassword(PasswordVO passwordVO)
            throws Exception {
        PasswordDO passwordDO = new PasswordDO();
        passwordDO.setUserId(passwordVO.getUserId());
        passwordDO.setType(passwordVO.getType());
        passwordDO.setPassword(passwordVO.getPassword());
        return insertPassword(passwordDO);
    }

    public ErrorCode insertPassword(AccountPwdVO accountPwdVO) throws Exception {
        PasswordDO passwordDO = new PasswordDO();
        passwordDO.setUserId(accountPwdVO.getUserId());
        passwordDO.setType(accountPwdVO.getType());
        passwordDO.setPassword(accountPwdVO.getPassword());
        return insertPassword(passwordDO);
    }

    public ErrorCode resetPassword(List<String> userIds)
            throws Exception {
        List<String> faildList = new ArrayList();
        for (String userid : userIds) {
            ErrorCode ee = updatePassword(userid, "123456");
            if (0 != ee.getCode()) {
                faildList.add(userid);
            }
        }
        if (!faildList.isEmpty()) {
            return ErrorCode.FailedToUpdateRecord;
        }
        return ErrorCode.Success;
    }

    public ErrorCode updatePassword(String userNo, String password)
            throws Exception {
        PasswordDO passwordDO = new PasswordDO();
        passwordDO.setUserId(userNo);

        String cypherPassword = cryptoPassword(password, userNo);
        passwordDO.setPassword(cypherPassword);
        passwordDO.setType(Integer.valueOf(0));
        int ret = this.passwordMapper.updateByPrimaryKey(passwordDO);
        if (ret <= 0) {
            return ErrorCode.FailedToUpdateRecord;
        }
        return ErrorCode.Success;
    }

    public ErrorCode updatePassword(String userId, UpdatePasswordVO updatePasswordVO)
            throws Exception {
        PasswordDOKey passwordDOKey = new PasswordDOKey();
        passwordDOKey.setUserId(userId);
        passwordDOKey.setType(Integer.valueOf(0));
        PasswordDO passwordDO = this.passwordMapper.selectByPrimaryKey(passwordDOKey);
        if (passwordDO == null) {
            log.error("原密码信息不存在！", ErrorCode.OldPwdNotRight.getCode());
            return ErrorCode.OldPwdNotRight;
        }
        String cypherPassword = cryptoPassword(updatePasswordVO.getOldPassword(), userId);
        if (!cypherPassword.equals(passwordDO.getPassword())) {
            log.error(ErrorCode.OldPwdNotRight.getDesc(), ErrorCode.OldPwdNotRight.getCode());
            return ErrorCode.OldPwdNotRight;
        }
        return updatePassword(userId, updatePasswordVO.getNewPassword());
    }

    public ErrorCode updateLoginName(AccountVO accountVO) {
        AccountDO accountDO = new AccountDO();
        accountDO.setUserId(accountVO.getUserId());
        accountDO.setLoginName(accountVO.getLoginName());
        int err = this.accountMapper.checkLoginName(accountDO);
        if (0 != err) {
            log.error(ErrorCode.UserNameExists.getDesc(), ErrorCode.UserNameExists.getCode());
            return ErrorCode.UserNameExists;
        }
        ErrorCode rmCode = rmAccountByUserId(accountVO.getUserId());
        if (ErrorCode.Success != rmCode) {
            return rmCode;
        }
        accountVO.setStatus(Integer.valueOf(0));
        ErrorCode addCode = addAccount(accountVO);
        if (ErrorCode.Success != addCode) {
            return addCode;
        }
        return ErrorCode.Success;
    }

    public AccountVO queryAccountById(String userId) {
        AccountDO accountDO = this.accountMapper.selectByUserId(userId);
        AccountVO accountVO = getVO(accountDO);
        if (null != accountVO) {
            accountVO.setLastLoginTime(this.accountMapper.getLastLoginTime(userId));
        }
        return accountVO;
    }

    public List<AccountVO> queryAccountsByUserId(String userId) {
        List<AccountDO> accountDOs = this.accountMapper.selectListByUserId(userId);

        List<AccountVO> accountVOs = new ArrayList();
        for (AccountDO accountDO : accountDOs) {
            accountVOs.add(getVO(accountDO));
        }
        return accountVOs;
    }

    private AccountVO getVO(AccountDO accountDO) {
        if (null == accountDO) {
            return null;
        }
        AccountVO accountVO = new AccountVO();
        accountVO.setLoginName(accountDO.getLoginName());
        accountVO.setStatus(accountDO.getStatus());
        accountVO.setUserId(accountDO.getUserId());
        accountVO.setLastLoginTime(accountDO.getLastLoginTime());
        return accountVO;
    }

    public String queryUserIdByAccount(String loginName) {
        return this.accountMapper.selectUserIdByAccount(loginName);
    }

    public ErrorCode checkLoginName(String loginName) {
        AccountDO accountDO = new AccountDO();
        accountDO.setUserId("");
        accountDO.setLoginName(loginName);
        int err = this.accountMapper.checkLoginName(accountDO);
        if (0 != err) {
            return ErrorCode.UserNameExists;
        }
        return err == 0 ? ErrorCode.Success : ErrorCode.UserNameExists;
    }

    public ErrorCode checkExistAccountName(String loginName) {
        int ret = this.accountMapper.checkByPrimaryKey(loginName);
        if (ret > 0) {
            log.error(ErrorCode.UserNameExists.getDesc(), ErrorCode.UserNameExists.getCode());
            return ErrorCode.UserNameExists;
        }
        return ErrorCode.Success;
    }

    public Map<String, AccountDO> queryAccountsByUserIds(Set<String> userSet) {
        Map<String, AccountDO> result = new HashMap<>(userSet.size());
        List<AccountDO> ll = accountMapper.queryAccountsByUserIds(new ArrayList<String>(userSet));
        if (CollectionUtils.isNotEmpty(ll)) {
            for (AccountDO accountDO : ll) {
                result.put(accountDO.getUserId(), accountDO);
            }
        }
        return result;
    }
}
