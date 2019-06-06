package cn.com.xgit.parts.auth.account.facade.sys;

import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.service.DepartmentService;
import cn.com.xgit.parts.auth.account.service.sys.SysAuthsService;
import cn.com.xgit.parts.auth.account.service.sys.SysRoleAuthService;
import cn.com.xgit.parts.auth.account.service.sys.SysRoleService;
import cn.com.xgit.parts.auth.account.service.sys.SysUserRolesService;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysRoleAuthDO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysRoleDO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysUserRolesDO;
import com.xgit.bj.common.util.BeanUtil;
import com.xgit.bj.core.Ref;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户权限关系
 */
@Service
public class UserAuthFacade {
    @Autowired
    private SysRoleAuthService sysRoleAuthService;

    @Autowired
    private SysUserRolesService sysUserRolesService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysAuthsService sysAuthsService;

    @Autowired
    private SysAccountService sysAccountService;


    @Autowired
    DepartmentService departmentService;

    //查询用户登录的信息，此处考虑用redis
//    @Cacheable(value = CacheGroup.USER, key = "T(com.xgit.bj.auth.user.manager.cache.CacheKeyUtil).AUTH_USER+#userId")
    public SysAccountVO queryLoginUser(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new AuthException("用户未登录！");
        }

        SysAccountDO sysAccount = sysAccountService.queryById(userId);
        if (null == sysAccount) {
            throw new AuthException(userId + "用户信息不存在！");
        }
        SysAccountVO userVO = BeanUtil.do2bo(sysAccount, SysAccountVO.class);
        if (StringUtils.isNotBlank(userVO.getDeptId())) {
            userVO.setDeptName(departmentService.queryFullDeptName(userVO.getDeptId()));
        }
        //处理用户的角色权限信息
        processRoleAuth(userVO, userId);
        return userVO;
    }

    private void processRoleAuth(SysAccountVO userVO, String userId) {
        List<SysUserRolesDO> ll = sysUserRolesService.queryUserRoles(userId);
        if (CollectionUtils.isEmpty(ll)) {
            return;
        }
        Set<String> roleIds = new HashSet<>();
        ll.forEach((x) -> roleIds.add(x.getRoleId()));
        Map<String, SysRoleDO> m = sysRoleService.queryMapByIds(roleIds);
        List<String> roleName = new ArrayList<>(m.size());
        List<SysRoleVO> roleVOs = new ArrayList<>(m.size());
        for (Map.Entry<String, SysRoleDO> en : m.entrySet()) {
            SysRoleDO e = en.getValue();
            if (null != e) {
                roleIds.add(e.getId());
                roleName.add(e.getName());
                roleVOs.add(BeanUtil.do2bo(e, SysRoleVO.class));
            }
        }
        userVO.setRoleNames(StringUtils.join(roleName));
        userVO.setRoleIds(new ArrayList<>(roleIds));
        userVO.setRoleVOs(roleVOs);

        List<String> aus = sysRoleAuthService.queryAuthCodes(roleIds);
        userVO.setAuthIds(aus);
    }

    @Transactional
    public ErrorCode save(SysAccountVO profileVO, Ref<String> userIdRef) {
        checkExistMobile(profileVO.getMobile(), profileVO.getUserId());
        ErrorCode ec = sysAccountService.save(profileVO);
        if (ec.getCode() == ErrorCode.Success.getCode() && StringUtils.isBlank(userIdRef.get())) {
            userIdRef.set(profileVO.getUserId());
            //添加初始化密码
            sysAccountService.saveInitPsw(profileVO.getUserId());
        }
        return ec;
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

    public List<SysRoleVO> queryAllRoles(String userId) {
        //TODO　暂时返回所有角色
        List<SysRoleDO> ll = sysRoleService.queryList(new SysRoleVO());
        return BeanUtil.do2bo4List(ll, SysRoleVO.class);
    }

    //此处考虑用redis
//    @GuavaLocalCache(expireTime = 300, refreshTime = 150, group = "user_group", preFix = "queryMenuByUserId_", keyExt = "#userId")
//    @Cacheable(value = CacheGroup.USER, key = "T(com.xgit.bj.auth.user.manager.cache.CacheKeyUtil).USER_MENU+#userId")
    public List<SysAuthsVO> queryMenuByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new AuthException(ErrorCode.NeedLogin, "用户未登录！");
        }
        List<SysAuthsVO> tree = sysAuthsService.queryAllMenuVOList();
        List<SysUserRolesDO> ll = sysUserRolesService.queryUserRoles(userId);
        if (CollectionUtils.isEmpty(ll)) {
            return null;
        }
        Set<String> roleIds = new HashSet<>();
        ll.forEach((x) -> roleIds.add(x.getRoleId()));
        List<Integer> ausIdList = sysRoleAuthService.queryAuthIds(roleIds);
        if (CollectionUtils.isNotEmpty(tree)) {
            Iterator<SysAuthsVO> it = tree.iterator();
            while (it.hasNext()) {
                SysAuthsVO vo = it.next();
                if (1 == vo.getType() && ausIdList.contains(vo.getId())) {
//                    vo.setChecked(true);
                    vo.setState("app" + vo.getUrl().replace("/", "."));
                    doCheckedChilren(vo, ausIdList);
                } else {
                    it.remove();
                }
            }
        }
        return tree;
    }

    private void doCheckedChilren(SysAuthsVO vo, List<Integer> ausIdList) {
        if (CollectionUtils.isNotEmpty(vo.getChildren())) {
            Iterator<SysAuthsVO> it = vo.getChildren().iterator();
            while (it.hasNext()) {
                SysAuthsVO vvo = it.next();
                if (1 == vvo.getType() && ausIdList.contains(vvo.getId())) {
//                    vo.setChecked(true);
                    vvo.setState("app" + vvo.getUrl().replace("/", "."));
                    doCheckedChilren(vvo, ausIdList);
                } else {
                    it.remove();
                }
            }

        }
    }

    public List<String> authCodesByUserId(String userId) {
        List<SysUserRolesDO> ll = sysUserRolesService.queryUserRoles(userId);
        if (CollectionUtils.isEmpty(ll)) {
            return null;
        }
        Set<String> roleIds = new HashSet<>();
        ll.forEach((x) -> roleIds.add(x.getRoleId()));

        List<String> aus = sysRoleAuthService.queryAuthCodes(roleIds);
        return aus;
    }

    public Boolean checkAuthCodes(String userId, String url) {
        List<SysUserRolesDO> ll = sysUserRolesService.queryUserRoles(userId);
        if (CollectionUtils.isEmpty(ll)) {
            return false;
        }
        Set<String> roleIds = new HashSet<>();
        for (SysUserRolesDO x : ll) {
            roleIds.add(x.getRoleId());
        }
        List<SysRoleAuthDO> aus = sysRoleAuthService.queryAuthCodeExist(roleIds, url);
        if (CollectionUtils.isNotEmpty(aus)) {
            return true;
        }
        return false;
    }
}
