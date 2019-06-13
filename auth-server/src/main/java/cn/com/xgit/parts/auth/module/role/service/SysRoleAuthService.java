package cn.com.xgit.parts.auth.module.role.service;

import cn.com.xgit.parts.auth.common.base.SuperMapper;
import cn.com.xgit.parts.auth.common.base.SuperService;
import cn.com.xgit.parts.auth.module.role.entity.SysRoleAuth;
import cn.com.xgit.parts.auth.module.role.mapper.SysRoleAuthMapper;
import cn.com.xgit.parts.auth.module.role.param.AuthRolePlatformParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SysRoleAuth 后台接口实现类
 */
@Slf4j
@Service
public class SysRoleAuthService extends SuperService<SuperMapper<SysRoleAuth>, SysRoleAuth> {

    @Autowired
    private SysRoleAuthMapper sysRoleAuthMapper;

    public boolean queryCheckUsedByAuthId(Long authId) {
        SysRoleAuth e = new SysRoleAuth();
        e.setAuthId(authId);
        List<SysRoleAuth> ll = list(new QueryWrapper<>(e));
        if (CollectionUtils.isNotEmpty(ll)) {
            return true;
        }
        return false;
    }

    /**
     * 根据平台、角色查询权限id
     *
     * @param platformId
     * @param roleIds
     * @param onlyMenu
     * @return
     */
    public Set<Long> queryAuthIdSet(Long platformId, List<Long> roleIds, boolean onlyMenu) {
        Set<Long> set = new HashSet<>();
        List<SysRoleAuth> ll = sysRoleAuthMapper.queryRoleAuth(platformId, roleIds);
//        List<SysRoleAuth> ll = sysRoleAuthMapper.queryRoleAuthJoin(platformId, roleIds,onlyMenu);
        if (CollectionUtils.isNotEmpty(ll)) {
            ll.forEach(x -> set.add(x.getAuthId()));
        }
        return set;
    }

    public Set<Long> queryAuthIdList(AuthRolePlatformParam param) {
        Set<Long> authIds = new HashSet<>();
        List<SysRoleAuth> ll = sysRoleAuthMapper.queryAuthIdList(param.getRoleIdList(),param.getPlatformId());
        for (SysRoleAuth ra : ll) {
            authIds.add(ra.getAuthId());
        }
        return authIds;
    }
}
