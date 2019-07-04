package cn.com.xgit.parts.auth.module.role.service;

import cn.com.xgit.parts.auth.common.base.SuperMapper;
import cn.com.xgit.parts.auth.common.base.SuperService;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.module.role.entity.SysRole;
import cn.com.xgit.parts.auth.module.role.entity.SysRoleAuth;
import cn.com.xgit.parts.auth.module.role.mapper.SysRoleAuthMapper;
import cn.com.xgit.parts.auth.module.role.param.AuthRolePlatformParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return CollectionUtils.isNotEmpty(ll);
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
        List<SysRoleAuth> ll = sysRoleAuthMapper.queryAuthIdList(param.getRoleIdList(), param.getPlatformId());
        for (SysRoleAuth ra : ll) {
            authIds.add(ra.getAuthId());
        }
        return authIds;
    }

    @Transactional
    public boolean saveRoleAuth(SysRole sysRole) {
        SysRoleAuth ss = new SysRoleAuth();
        ss.setRoleId(sysRole.getId());
        sysRoleAuthMapper.delete(new QueryWrapper<>(ss));
        if (CollectionUtils.isNotEmpty(sysRole.getTreeAuthList())) {
            List<SysRoleAuth> list = new ArrayList<>();
            collectSysRoleAuthList(list, sysRole.getTreeAuthList(), sysRole);
            if (CollectionUtils.isNotEmpty(list)) {
                doSaveRoleAuth(list);
            }
        }
        return true;
    }

    private void doSaveRoleAuth(List<SysRoleAuth> list) {
        int max = 1000;
        Stream.iterate(0, n -> n + 1).limit((list.size() - 1) / max + 1).forEach(i -> {
            List<SysRoleAuth> pageList = list.stream().skip(i * max).limit(max).collect(Collectors.toList());
            sysRoleAuthMapper.batchInsert(pageList);
        });
    }

    private void collectSysRoleAuthList(List<SysRoleAuth> list, List<SysAuthsVO> treeAuthList, SysRole sysRole) {
        for (SysAuthsVO vo : treeAuthList) {
            if (vo.isChecked()) {
                SysRoleAuth dd = new SysRoleAuth();
                dd.setRoleId(sysRole.getId());
                dd.setPlatformId(sysRole.getPlatformId());
                dd.setAuthId(vo.getId());
                list.add(dd);
                if (CollectionUtils.isNotEmpty(vo.getChildren())) {
                    collectSysRoleAuthList(list, vo.getChildren(), sysRole);
                }
            }
        }
    }
}
