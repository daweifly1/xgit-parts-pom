package cn.com.xgit.parts.auth.account.service;

import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.account.manager.UserContextManager;
import cn.com.xgit.parts.auth.account.service.base.BaseService;
import cn.com.xgit.parts.auth.account.service.sys.SysUserRolesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.com.xgit.parts.auth.VO.AccountPwdVO;
import cn.com.xgit.parts.auth.VO.DepartmentVO;
import cn.com.xgit.parts.auth.VO.LockVO;
import cn.com.xgit.parts.auth.VO.ProfileConditionVO;
import cn.com.xgit.parts.auth.VO.ProfileVO;
import cn.com.xgit.parts.auth.VO.RegMemberVO;
import cn.com.xgit.parts.auth.VO.UserCacheVO;
import cn.com.xgit.parts.auth.VO.UserVO;
import cn.com.xgit.parts.auth.VO.WorkspaceVO;
import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import cn.com.xgit.parts.auth.account.dao.entity.AccountDO;
import cn.com.xgit.parts.auth.account.dao.entity.DepartmentDO;
import cn.com.xgit.parts.auth.account.dao.entity.ProfileDO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysRoleDO;
import cn.com.xgit.parts.auth.account.dao.mapper.ProfileMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.common.util.BeanUtil;
import com.xgit.bj.core.Ref;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class ProfileService extends BaseService<ProfileVO, ProfileDO> {
    @Autowired
    ProfileMapper profileMapper;

    @Autowired
    private SysUserRolesService sysUserRolesService;
    @Autowired
    AccountService accountService;
    @Autowired
    private GuidService guidService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    WorkspaceService workspaceService;
    @Autowired
    private UserContextManager userContextManager;
    @Value("${role.admin.roleId}")
    private String roleAdminId;

    @PostConstruct
    public void init() {
        super.addMapper(this.profileMapper);
    }

    public ProfileService() {
        super(ProfileVO.class, ProfileDO.class);
    }

    public PageCommonVO<UserVO> list(SearchCommonVO<ProfileConditionVO> condtion, String userId) {
//        init();
        String spaceId = this.userContextManager.getWorkspaceId(userId);

        condtion.getFilters().setSpaceId(spaceId);
        condtion.getFilters().setMyself(userId);

        ProfileConditionVO temp = condtion.getFilters();
        if (StringUtils.isNoneBlank(temp.getName())) {
            temp.setName(temp.getName().trim().replaceAll(" +", "%"));
            condtion.setFilters(temp);
        }
        PageCommonVO pageCommonVO = pageList(condtion);
        List<ProfileVO> profileVOList = pageCommonVO.getPageInfoList();
        List<UserVO> userVOs = new ArrayList();
        batchPackageUserInfo(profileVOList, userVOs);
        pageCommonVO.setPageInfoList(userVOs);

        return pageCommonVO;
    }


    private PageCommonVO<ProfileVO> pageList(SearchCommonVO<ProfileConditionVO> condition) {
        PageCommonVO pageCommonVO = new PageCommonVO();
        if (StringUtils.isEmpty(condition.getSort())) {
            PageHelper.orderBy("create_date");
        } else {
            PageHelper.orderBy(condition.getSort());
        }
        PageHelper.startPage(condition.getPageNum().intValue(), condition.getPageSize().intValue());
        List<ProfileDO> doList = this.profileMapper.list((ProfileConditionVO) condition.getFilters());
        List<ProfileVO> voList = getVOList(doList);
        pageCommonVO.setPageInfo(new PageInfo(doList));
        pageCommonVO.setPageInfoList(voList);
        return pageCommonVO;
    }

    private ErrorCode checkAddProfile(ProfileVO profileVO) {
        ErrorCode checkCode = this.accountService.checkExistAccountName(profileVO.getLoginName());
        if (checkCode.getCode() == ErrorCode.Success.getCode()) {
            checkCode = checkExistMobile(profileVO.getMobile(), null);
        }
        return checkCode;
    }

    @Transactional(rollbackFor = {Exception.class})
    public ErrorCode insert(ProfileVO profileVO, Ref<String> userIdRef) throws Exception {
        ErrorCode checkCode = checkAddProfile(profileVO);
        if (checkCode.getCode() != ErrorCode.Success.getCode()) {
            return checkCode;
        }
        String userId = guidService.genTextId();
        ProfileDO profileDO = new ProfileDO();
        profileVO.setUserId(userId);
        profileVO.setType(Integer.valueOf(0));
        profileVO.setStatus(1);
        if (StringUtils.isBlank(profileVO.getPassword())) {
            log.debug("账户密码为空，使用默认密码！");
            profileVO.setPassword("123456");
        }
        AccountPwdVO accountPwdVO = new AccountPwdVO();
        BeanUtils.copyProperties(profileVO, accountPwdVO);
        ErrorCode code = accountService.addAccountPwd(accountPwdVO);
        if (ErrorCode.Success != code) {
            return code;
        }
        BeanUtils.copyProperties(profileVO, profileDO);
        profileDO.setSpaceId(profileVO.getSpaceId());
        if (null == profileDO.getLocked()) {
            profileDO.setLocked(Integer.valueOf(0));
        }
        if (StringUtils.isBlank(profileDO.getDeptId())) {
            profileDO.setDeptId("0");
        }
        int ret = this.profileMapper.insert(profileDO);
        if (0 >= ret) {
            log.error("添加用户信息步骤异常！", ErrorCode.FailedToInsertRecord.getCode());
            return ErrorCode.FailedToInsertRecord;
        }
        List<String> roleIds = profileVO.getRoleIds();
        if (CollectionUtils.isEmpty(roleIds)) {
            userIdRef.set(userId);
            log.debug("用户没有设置角色信息！");
            return ErrorCode.Success;
        }
        sysUserRolesService.updateUserRole(roleIds, userId);
        userIdRef.set(userId);
        return ErrorCode.Success;
    }


    @Transactional(rollbackFor = {Exception.class})
    public ErrorCode removeByIds(List<String> ids) throws Exception {
        for (String id : ids) {
            this.profileMapper.removeById(id);

            this.sysUserRolesService.deleteByUserId(id);

            this.accountService.removeAccountByUserId(id);
        }
        return ErrorCode.Success;
    }


    public ErrorCode updateByAccount(ProfileVO profileVO) {
        if ((null == profileVO) || (StringUtils.isBlank(profileVO.getLoginName()))) {
            return ErrorCode.IllegalArument;
        }
        String userId = this.accountService.queryUserIdByAccount(profileVO.getLoginName());
        if (StringUtils.isBlank(userId)) {
            return ErrorCode.IllegalArument;
        }
        profileVO.setUserId(userId);

        return update(profileVO);
    }


    public UserCacheVO getUserCacheVO(ProfileVO profileVO) {
        if (null == profileVO) {
            return new UserCacheVO();
        }
        UserCacheVO userCacheVO = new UserCacheVO();

        userCacheVO.setUserId(profileVO.getUserId());
        userCacheVO.setSpaceId(profileVO.getSpaceId());
        userCacheVO.setDeptId(profileVO.getDeptId());
        userCacheVO.setName(profileVO.getName());
        userCacheVO.setMobile(profileVO.getMobile());
        userCacheVO.setAreaCode(profileVO.getAreaCode());
        if ((StringUtils.isBlank(profileVO.getDeptId())) || ("0".equals(profileVO.getDeptId()))) {
            return userCacheVO;
        }
        DepartmentVO departmentVO = this.departmentService.item(profileVO.getDeptId());
        if (null == departmentVO) {
            return userCacheVO;
        }
        userCacheVO.setDeptName(departmentVO.getName());
        userCacheVO.setDeptCode(departmentVO.getCode());
        return userCacheVO;
    }

    public Integer queryListByDept(String deptId) {
        return this.profileMapper.queryCountByDept(deptId);
    }

    public ErrorCode updateLock(LockVO lockVO) {
        List<String> userIds = lockVO.getUserIds();
        for (String userId : userIds) {
            ProfileDO profileDO = new ProfileDO();
            profileDO.setUserId(userId);
            profileDO.setLocked(lockVO.getLock());
            this.profileMapper.update(profileDO);
        }
        return ErrorCode.Success;
    }


    //批量完善用户数据，每页数据量少不考虑分页
    private void batchPackageUserInfo(List<ProfileVO> profileVOList, List<UserVO> userVOs) {
        if (CollectionUtils.isEmpty(profileVOList)) {
            return;
        }
        Set<String> userSet = new HashSet<>();
        Set<String> deptSet = new HashSet<>();
        for (ProfileVO profileVO : profileVOList) {
            userSet.add(profileVO.getUserId());
            deptSet.add(profileVO.getDeptId());
        }
        if (CollectionUtils.isEmpty(userSet)) {
            return;
        }
        Map<String, AccountDO> accountVOMap = accountService.queryAccountsByUserIds(userSet);

        Map<String, DepartmentDO> userDeptMap = departmentService.queryUserDeptMapByIds(deptSet);

        Map<String, List<SysRoleDO>> userRolesMap = sysUserRolesService.queryUserRolesByUserIds(userSet);

        for (ProfileVO profileVO : profileVOList) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(profileVO, userVO);
            AccountDO accountVO = accountVOMap.get(profileVO.getUserId());
            if (null != accountVO) {
                userVO.setLoginName(accountVO.getLoginName());
                userVO.setLastLoginTime(accountVO.getLastLoginTime());
            }
            DepartmentDO departmentDO = userDeptMap.get(profileVO.getDeptId());
            if (null != departmentDO) {
                userVO.setDeptName(departmentDO.getName());
            }

            List<SysRoleDO> roleVOs = userRolesMap.get(profileVO.getUserId());
            if (CollectionUtils.isNotEmpty(roleVOs)) {
                List<String> roleIds = new ArrayList();
                List<String> roleName = new ArrayList();
                for (SysRoleDO roleVO : roleVOs) {
                    if (null != roleVO) {
                        roleIds.add(roleVO.getId());
                        roleName.add(roleVO.getName());
                    }
                }
                userVO.setRoleNames(StringUtils.join(roleName));
                userVO.setRoleIds(roleIds);
                userVO.setRoleVOs(BeanUtil.do2bo4List(roleVOs, SysRoleVO.class));
            }

            userVOs.add(userVO);
        }
    }


    @Transactional(rollbackFor = {Exception.class})
    public ErrorCode addUserWithAccount(AccountPwdVO accountPwdVO, Ref<String> userIdRef) throws Exception {
        ProfileVO profileVO = new ProfileVO();
        profileVO.setSpaceId("1");

        profileVO.setPassword(accountPwdVO.getPassword());
        profileVO.setLoginName(accountPwdVO.getLoginName());

        return insert(profileVO, userIdRef);
    }


    public ErrorCode checkExistMobile(String mobile, String userId) {
        if (StringUtils.isNotBlank(mobile)) {
            ProfileDO profileDO = new ProfileDO();
            profileDO.setMobile(mobile);
            profileDO.setUserId(userId);
            int count = this.profileMapper.getCountByMobile(profileDO);
            if (count > 0) {
                return ErrorCode.MobileExistError;
            }
        }
        return ErrorCode.Success;
    }

    @Transactional
    public ErrorCode regMember(RegMemberVO regMemberVO) {
        WorkspaceVO workspaceVO = new WorkspaceVO();
        workspaceVO.setId(regMemberVO.getWorkSpaceId());
        workspaceVO.setSite(regMemberVO.getSite());

        workspaceVO.setName(regMemberVO.getWorkSpaceId());
        workspaceVO.setTempId(String.valueOf(regMemberVO.getType()));
        workspaceVO.setAccount(regMemberVO.getLoginName());
        workspaceVO.setPassword(regMemberVO.getPassword());
        workspaceVO.setType(String.valueOf(regMemberVO.getType()));
        try {
            ErrorCode errorCode = this.workspaceService.insert(workspaceVO);
            if (errorCode.getCode() != ErrorCode.Success.getCode()) {
                throw new AuthException(ErrorCode.FailedToInsertRecord);
            }
        } catch (Exception e) {
            throw new AuthException(ErrorCode.FailedToInsertRecord);
        }
        ProfileVO profileVO = new ProfileVO();
        profileVO.setLoginName(regMemberVO.getLoginName());
        profileVO.setName(regMemberVO.getName());
        profileVO.setIdNumber(regMemberVO.getIdNumber());
        profileVO.setMobile(regMemberVO.getMobile());
        ErrorCode errorCode = updateByAccount(profileVO);
        if (errorCode.getCode() != ErrorCode.Success.getCode()) {
            throw new AuthException(errorCode);
        }
        return errorCode;
    }
}
