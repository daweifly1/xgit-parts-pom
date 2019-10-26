package cn.com.xgit.parts.auth.module.role.facade;

import cn.com.xgit.parts.auth.module.account.service.SysAccountRoleService;
import cn.com.xgit.parts.auth.module.menu.param.SysAuthsParam;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.module.role.entity.SysAuths;
import cn.com.xgit.parts.auth.module.role.param.AuthRolePlatformParam;
import cn.com.xgit.parts.auth.module.role.service.SysAuthsService;
import cn.com.xgit.parts.auth.module.role.service.SysRoleAuthService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Set<Long> hasAuthCode = queryAuthIds(condition.getPlatformId(), condition.getDataId(), userId, true);
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

    private Set<Long> queryAuthIds(Long platformId, Long dataId, Long userId, boolean onlyMenu) {
        //查询用户对应平台所有角色集合
        List<Long> roleIds = sysAccountRoleService.querRoleIdsByUserId(platformId, userId);
        if (null != dataId) {
            //如果有dataId要区分下当前有效的角色
            AuthRolePlatformParam param = new AuthRolePlatformParam();
            param.setRoleIdList(roleIds);
            param.setDataId(dataId);
            List<Long> roleIds2 = sysAccountRoleService.queryDataRole(param);
            if (CollectionUtils.isEmpty(roleIds2)) {
                return null;
            }
            //根据roleId查询拥有的权限码（权限id）
            return sysRoleAuthService.queryAuthIdSet(platformId, roleIds2, onlyMenu);
        }
        //根据roleId查询拥有的权限码（权限id）
        Set<Long> hasAuthCode = sysRoleAuthService.queryAuthIdSet(platformId, roleIds, onlyMenu);
        return hasAuthCode;
    }

    public List<Long> getAuthIds(SysAuthsParam sysAuthsParam) {
        return new ArrayList<>(queryAuthIds(sysAuthsParam.getPlatformId(), null, sysAuthsParam.getUserId(), false));
    }

    //此内容可以考虑localcache(角色[dataId]---》url集合)
    public Set<String> queryUrlsByRoleIds(AuthRolePlatformParam param) {
        Set<String> urls = new HashSet<>();
        if (null == param || CollectionUtils.isEmpty(param.getRoleIdList())) {
            return urls;
        }
        //如果有dataId要区分下当前有效的角色
        List<Long> roleIds = sysAccountRoleService.queryDataRole(param);
        param.setRoleIdList(roleIds);
        if (CollectionUtils.isEmpty(roleIds)) {
            return urls;
        }
        Set<Long> authIds = sysRoleAuthService.queryAuthIdList(param);
        //根据authId分批查询对应的url
        int max = 1000;
        Stream.iterate(0, n -> n + 1).limit((authIds.size() - 1) / max + 1).forEach(i -> {
            List<Long> pageIdList = authIds.stream().skip(i * max).limit(max).collect(Collectors.toList());
            Collection<SysAuths> ll = sysAuthsService.listByIds(pageIdList);
            if (CollectionUtils.isNotEmpty(ll)) {
                for (SysAuths a : ll) {
                    urls.add(a.getUrl());
                }
            }
        });
        return urls;
    }
}
