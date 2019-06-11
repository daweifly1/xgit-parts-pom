package cn.com.xgit.parts.auth.module.role.facade;

import cn.com.xgit.parts.auth.module.account.service.SysAccountRoleService;
import cn.com.xgit.parts.auth.module.menu.param.SysAuthsParam;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.module.role.entity.SysAuths;
import cn.com.xgit.parts.auth.module.role.service.SysAuthsService;
import cn.com.xgit.parts.auth.module.role.service.SysRoleAuthService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户权限关系
 */
@Service
public class MenuFacade {

    /**
     * 未来可能是网络调用
     */
    @Autowired
    private SysAccountRoleService sysAccountRoleService;

    @Autowired
    private SysAuthsService sysAuthsService;

    @Autowired
    private SysRoleAuthService sysRoleAuthService;


    public List<SysAuthsVO> menuTreeList(SysAuths condition, Long userId) {
        List<SysAuthsVO> result = new ArrayList<>();
        //查询平台所有权限资源
        List<SysAuthsVO> ll = sysAuthsService.treeList(condition, true);
        //权限码（权限id）
        Set<Long> hasAuthCode = queryAuthIds(condition.getPlatformId(), userId, true);
        if (CollectionUtils.isNotEmpty(ll)) {
            //ll可以加local cache
            result.addAll(ll);
            for (SysAuthsVO sysAuthsVO : result) {
                if (!hasAuthCode.contains(sysAuthsVO.getId())) {
                    sysAuthsVO.setShowFlag(0);
                    processChildren(sysAuthsVO, hasAuthCode, false);
                } else {
                    sysAuthsVO.setShowFlag(1);
                    processChildren(sysAuthsVO, hasAuthCode, true);
                }
            }
        }
        return result;
    }

    private void processChildren(SysAuthsVO sysAuthsVOParent, Set<Long> hasAuthCode, boolean needCheck) {
        if (CollectionUtils.isNotEmpty(sysAuthsVOParent.getChildren())) {
            for (SysAuthsVO sysAuthsVO : sysAuthsVOParent.getChildren()) {
                if (needCheck) {
                    if (!hasAuthCode.contains(sysAuthsVO.getId())) {
                        sysAuthsVO.setShowFlag(0);
                        processChildren(sysAuthsVO, hasAuthCode, false);
                    } else {
                        sysAuthsVO.setShowFlag(1);
                        processChildren(sysAuthsVO, hasAuthCode, true);
                    }
                } else {
                    sysAuthsVO.setShowFlag(0);
                    processChildren(sysAuthsVO, hasAuthCode, false);
                }
            }
        }
    }

    private Set<Long> queryAuthIds(Long platformId, Long userId, boolean onlyMenu) {
        //查询用户对应平台所有角色集合
        List<Long> roleIds = sysAccountRoleService.querRolesByUserId(platformId, userId);
        //根据roleId查询拥有的权限码（权限id）
        Set<Long> hasAuthCode = sysRoleAuthService.queryAuthIdSet(platformId, roleIds, onlyMenu);
        return hasAuthCode;
    }

    public List<Long> getAuthIds(SysAuthsParam sysAuthsParam) {
        return new ArrayList<>(queryAuthIds(sysAuthsParam.getPlatformId(), sysAuthsParam.getUserId(), false));
    }

//    @Autowired
//    private SysRoleAuthService sysRoleAuthService;
//
//    @Autowired
//    private SysUserRolesService sysUserRolesService;
//
//    @Autowired
//    private SysRoleService sysRoleService;
//
//    @Autowired
//    private SysAuthsService sysAuthsService;
//
//    @Autowired
//    private SysAccountService sysAccountService;
//
//
//    @Autowired
//    DepartmentService departmentService;
//
//
//    /**
//     * 查询菜单按钮集合（树形结构）
//     *
//     * @param userId
//     * @return
//     */
//    public List<SysAuthsVO> queryMenuByUserId(Long userId) {
//        if (null == userId) {
//            throw new AuthException(ErrorCode.NeedLogin, "用户未登录！");
//        }
//        List<SysAuthsVO> tree = sysAuthsService.queryAllMenuVOList();
//        List<SysUserRolesDO> ll = sysUserRolesService.queryUserRoles(userId);
//        if (CollectionUtils.isEmpty(ll)) {
//            return null;
//        }
//        Set<String> roleIds = new HashSet<>();
//        ll.forEach((x) -> roleIds.add(x.getRoleId()));
//        List<Integer> ausIdList = sysRoleAuthService.queryAuthIds(roleIds);
//        if (CollectionUtils.isNotEmpty(tree)) {
//            Iterator<SysAuthsVO> it = tree.iterator();
//            while (it.hasNext()) {
//                SysAuthsVO vo = it.next();
//                if (1 == vo.getType() && ausIdList.contains(vo.getId())) {
////                    vo.setChecked(true);
//                    vo.setState("app" + vo.getUrl().replace("/", "."));
//                    doCheckedChilren(vo, ausIdList);
//                } else {
//                    it.remove();
//                }
//            }
//        }
//        return tree;
//    }
//
//    private void doCheckedChilren(SysAuthsVO vo, List<Integer> ausIdList) {
//        if (CollectionUtils.isNotEmpty(vo.getChildren())) {
//            Iterator<SysAuthsVO> it = vo.getChildren().iterator();
//            while (it.hasNext()) {
//                SysAuthsVO vvo = it.next();
//                if (1 == vvo.getType() && ausIdList.contains(vvo.getId())) {
////                    vo.setChecked(true);
//                    vvo.setState("app" + vvo.getUrl().replace("/", "."));
//                    doCheckedChilren(vvo, ausIdList);
//                } else {
//                    it.remove();
//                }
//            }
//        }
//    }
//
//
//    public List<SysRoleVO> queryAllRoles(String userId) {
//        //TODO　暂时返回所有角色
//        List<SysRoleDO> ll = sysRoleService.queryList(new SysRoleVO());
//        return BeanUtil.do2bo4List(ll, SysRoleVO.class);
//    }
//
//    public Boolean checkAuthCodes(String userId, String url) {
//        List<SysUserRolesDO> ll = sysUserRolesService.queryUserRoles(userId);
//        if (CollectionUtils.isEmpty(ll)) {
//            return false;
//        }
//        Set<String> roleIds = new HashSet<>();
//        for (SysUserRolesDO x : ll) {
//            roleIds.add(x.getRoleId());
//        }
//        List<SysRoleAuthDO> aus = sysRoleAuthService.queryAuthCodeExist(roleIds, url);
//        if (CollectionUtils.isNotEmpty(aus)) {
//            return true;
//        }
//        return false;
//    }
}
