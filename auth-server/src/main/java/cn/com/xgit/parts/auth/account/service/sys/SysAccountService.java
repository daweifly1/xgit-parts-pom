package cn.com.xgit.parts.auth.account.service.sys;

import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.account.service.base.BaseService;
import cn.com.xgit.parts.auth.enums.PasswordType;
import cn.com.xgit.parts.auth.VO.LockVO;
import cn.com.xgit.parts.auth.VO.UpdatePasswordVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.account.vo.SysPasswordVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysAccountDO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysPasswordDO;
import cn.com.xgit.parts.auth.account.dao.mapper.sys.SysAccountMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.sys.SysPasswordMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.common.util.BeanUtil;
import com.xgit.bj.common.util.CollectionUtil;
import com.xgit.bj.common.util.security.CryptoUtil;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * SysAccount 后台接口实现类
 */
@Slf4j
@Service
public class SysAccountService extends BaseService<SysAccountVO, SysAccountDO> {

    @Autowired
    private SysAccountMapper sysAccountMapper;

    @Autowired
    private SysPasswordMapper sysPasswordMapper;

    @Autowired
    private SysUserRolesService sysUserRolesService;

    @Value("${initPassword:123456}")
    private String initPassword;

    @PostConstruct
    public void init() {
        super.addMapper(sysAccountMapper);
    }

    protected SysAccountService() {
        super(SysAccountVO.class, SysAccountDO.class);
    }

    /**
     * 查询列表
     *
     * @param condition
     */
    public PageCommonVO<SysAccountVO> list(SearchCommonVO<SysAccountVO> condition) {
        if (null == condition.getFilters()) {
            condition.setFilters(new SysAccountVO());
        }
        if (StringUtils.isBlank(condition.getOrderBy())) {
            condition.setOrderBy(" ID ");
        }
        PageCommonVO<SysAccountVO> page = super.list(condition);
        return page;
    }


    /**
     * 查询列表
     *
     * @param vo
     */
    public List<SysAccountDO> queryList(SysAccountVO vo) {
        return sysAccountMapper.queryList(vo);
    }


    /**
     * 查询单条记录
     *
     * @param userId
     */
    public SysAccountDO queryById(String userId) {
        SysAccountVO vo = new SysAccountVO();
        vo.setUserId(userId);
        List<SysAccountDO> list = queryList(vo);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public SysAccountDO queryByLoginName(String loginName) {
        SysAccountVO vo = new SysAccountVO();
        vo.setLoginName(loginName);
        List<SysAccountDO> list = queryList(vo);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 保存数据
     *
     * @param sysAccountVO
     */
    @Transactional
//    @Caching(evict = {
//            @CacheEvict(value = ROLE, key = "T(com.xgit.bj.auth.user.manager.cache.CacheKeyUtil).ALL_ROLES"),
//            @CacheEvict(value = USER, allEntries = true)
//    })
    public ErrorCode save(SysAccountVO sysAccountVO) {
        doSave(sysAccountVO);
        return ErrorCode.Success;
    }

    /**
     * 根据状态，等信息保存数据
     *
     * @param sysAccountVO
     */
    private void doSave(SysAccountVO sysAccountVO) {
        //保存是否需要校验
        preSaveCheck(sysAccountVO);
        SysAccountDO sysAccountDO = getDO(sysAccountVO);

        boolean isSuccess = this.merge(sysAccountDO) > 0;
        if (!isSuccess) {
            throw new AuthException("保存记录失败");
        }
        sysAccountVO.setUserId(sysAccountDO.getUserId());
        //关联信息处理
        sysUserRolesService.updateUserRole(sysAccountVO.getRoleIds(), sysAccountDO.getUserId());
    }

    /**
     * 保存前信息校验
     */
    private void preSaveCheck(SysAccountVO sysAccountVO) {
        if (null == sysAccountVO) {
            throw new AuthException(ErrorCode.IllegalArument.getDesc());
        }
        if (StringUtils.isNotBlank(sysAccountVO.getUserId())) {
        }
    }


    @Transactional
    public int merge(SysAccountDO dto) {
        dto.setUpdateTime(new Date());
        if (StringUtils.isBlank(dto.getUserId())) {
            //id生成策略
            dto.setUserId(UUID.randomUUID().toString());
            dto.setCreateTime(new Date());
            dto.setOrgId(genOrgId(dto.getDeptId()));
            dto.setStatus(1);
            return sysAccountMapper.insert(dto);
        }
        return sysAccountMapper.updateByPrimaryKeySelective(dto);
    }

    private String genOrgId(String deptId) {
        //根据部门id查询公司
        return deptId;
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public int batchDelete(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        int i = 0;
        for (String id : ids) {
            i = +sysAccountMapper.deleteByPrimaryKeySelective(id);
        }
        return i;
    }

    /**
     * 根据id集合查询并返回map(id->do)
     */
    public Map<String, SysAccountDO> queryMapByIds(Set<String> ids) {
        final Map<String, SysAccountDO> result = new HashMap<>();
        if (CollectionUtils.isEmpty(ids)) {
            return result;
        }
        CollectionUtil.split(new ArrayList<String>(ids), 900, new CollectionUtil.PageProcess<String>() {
            @Override
            public void process(List<String> pageIdList, Object... params) {
                List<SysAccountDO> ll = sysAccountMapper.queryListByIds(new ArrayList<String>(pageIdList));
                if (CollectionUtils.isNotEmpty(ll)) {
                    for (SysAccountDO sysAccountDO : ll) {
                        result.put(sysAccountDO.getUserId(), sysAccountDO);
                    }
                }
            }
        });
        return result;
    }

    //校验登录的密码是否正确
    public boolean checkLoginPsw(String userId, String password) {
        SysPasswordDO sysPasswordDO = queryPswInfoByUserIdAndType(userId, PasswordType.NORMAL.getType());
        if (null == sysPasswordDO) {
            return false;
        }

        String cypherPassword = cryptoPassword(password, userId);
        if (!cypherPassword.equals(sysPasswordDO.getPassword())) {
            return false;
        }
        return true;
    }


    private String cryptoPassword(String text, String salt) {
        String orginalText = text + "_" + salt;
        byte[] cypherBytes = new byte[0];
        try {
            cypherBytes = CryptoUtil.encryptMD5(orginalText.getBytes());
            String cypherText = new BigInteger(cypherBytes).toString(16);
            return cypherText;
        } catch (Exception e) {
            log.error("登录密码加密错误", e);
            return "";
        }
    }

    private SysPasswordDO queryPswInfoByUserIdAndType(String userId, int type) {
        SysPasswordVO condition = new SysPasswordVO();
        condition.setUserId(userId);
        condition.setType(type);
        List<SysPasswordDO> list = sysPasswordMapper.queryList(condition);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    //更新登录的信息，此处可以降级
    public void updateLoginTime(SysAccountDO sysAccountDO, String ip, boolean pass) {
        try {
            SysAccountDO bean = new SysAccountDO();
            bean.setUserId(sysAccountDO.getUserId());
            bean.setLastLoginTime(new Date());
            bean.setLastLoginIp(ip);
            if (pass) {
                bean.setPasswordErrorTimes(0);
            } else {
                if (null == sysAccountDO.getPasswordErrorTimes()) {
                    bean.setPasswordErrorTimes(1);
                } else {
                    bean.setPasswordErrorTimes(sysAccountDO.getPasswordErrorTimes() + 1);
                }
            }
            sysAccountMapper.updateByPrimaryKeySelective(bean);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Transactional
    public ErrorCode updatePassword(String userId, String password) {
        SysPasswordDO sysPasswordDO = queryPswInfoByUserIdAndType(userId, PasswordType.NORMAL.getType());
        if (null == sysPasswordDO) {
            return ErrorCode.Failure;
        }
        sysPasswordDO.setUserId(userId);
        String cypherPassword = cryptoPassword(password, userId);
        sysPasswordDO.setPassword(cypherPassword);
        int r = sysPasswordMapper.updateByPrimaryKeySelective(sysPasswordDO);
        if (r > 0) {
            return ErrorCode.Success;
        }
        return ErrorCode.Failure;
    }

    @Transactional
    public ErrorCode resetPassword(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return ErrorCode.Failure;
        }
        for (String userId : userIds) {
            saveInitPsw(userId);
        }
        return ErrorCode.Success;
    }

    public ErrorCode checkLoginName(String loginName) {
        SysAccountDO m = queryByLoginName(loginName);
        if (null == m) {
            return ErrorCode.Success;
        }
        return ErrorCode.UserNameExists;
    }

    public int getCountByMobile(SysAccountDO profileDO) {
        SysAccountVO vo = new SysAccountVO();
        vo.setMobile(profileDO.getMobile());
        vo.setExUserId(profileDO.getUserId());
        List<SysAccountDO> list = queryList(vo);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.size();
        }
        return 0;
    }

    public ErrorCode updateLock(LockVO lockVO) {
        if (null == lockVO || null == lockVO.getLock() || CollectionUtils.isEmpty(lockVO.getUserIds())) {
            return ErrorCode.IllegalArument;
        }
        int r = sysAccountMapper.updateLock(lockVO);
        if (r > 0) {
            return ErrorCode.Success;
        }
        return ErrorCode.Failure;
    }

    //    @CacheEvict(value = CacheGroup.USER, key = "T(com.xgit.bj.auth.user.manager.cache.CacheKeyUtil).AUTH+#userId", beforeInvocation = true)
    public void saveInitPsw(String userId) {
        SysPasswordDO sysPasswordDO = queryPswInfoByUserIdAndType(userId, PasswordType.NORMAL.getType());
        if (null != sysPasswordDO) {
            return;
        }
        sysPasswordDO = new SysPasswordDO();
        sysPasswordDO.setType(PasswordType.NORMAL.getType());
        sysPasswordDO.setUserId(userId);
        String cypherPassword = cryptoPassword(initPassword, userId);
        sysPasswordDO.setUpdateTime(new Date());
        sysPasswordDO.setPassword(cypherPassword);
        int r = sysPasswordMapper.insert(sysPasswordDO);
        if (r < 1) {
            throw new AuthException("密码初始化失败");
        }
    }

    public ErrorCode updateChangePassword(UpdatePasswordVO updatePasswordVO, String userId) {
        if (null == updatePasswordVO || StringUtils.isBlank(updatePasswordVO.getNewPassword()) || StringUtils.isBlank(updatePasswordVO.getOldPassword())) {
            return ErrorCode.IllegalArument;
        }
        SysPasswordDO sysPasswordDO = queryPswInfoByUserIdAndType(userId, PasswordType.NORMAL.getType());
        if (null == sysPasswordDO) {
            throw new AuthException("原密码错误");
        }
        String old = cryptoPassword(updatePasswordVO.getOldPassword(), userId);
        if (old.equals(sysPasswordDO.getPassword())) {
            sysPasswordDO.setType(PasswordType.NORMAL.getType());
            sysPasswordDO.setUserId(userId);
            String cypherPassword = cryptoPassword(updatePasswordVO.getNewPassword(), userId);
            sysPasswordDO.setUpdateTime(new Date());
            sysPasswordDO.setPassword(cypherPassword);
            int r = sysPasswordMapper.updateByPrimaryKeySelective(sysPasswordDO);
            if (r < 1) {
                throw new AuthException("密码更新失败");
            }
            return ErrorCode.Success;
        } else {
            throw new AuthException("原密码错误");
        }
    }

    public SysAccountVO queryAccountByLoginName(String loginName) {
        SysAccountDO m = queryByLoginName(loginName);
        if (null == m) {
            return BeanUtil.do2bo(m, SysAccountVO.class);
        }
        return null;
    }
}
