package cn.com.xgit.parts.auth.module.account.service;

import cn.com.xgit.parts.auth.module.account.entity.SysAccountRole;
import cn.com.xgit.parts.auth.module.account.mapper.SysAccountRoleMapper;
import cn.com.xgit.parts.auth.common.base.SuperMapper;
import cn.com.xgit.parts.auth.common.base.SuperService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SysAccountRole 系统密码
 */
@Slf4j
@Service
public class SysAccountRoleService extends SuperService<SuperMapper<SysAccountRole>, SysAccountRole> {

    @Autowired
    private SysAccountRoleMapper sysAccountRoleMapper;

    public boolean queryCheckByRoleIds(List<Long> roleIds) {
        List<SysAccountRole> ll = sysAccountRoleMapper.queryListByRoleIds(roleIds, 1);
        if (CollectionUtils.isNotEmpty(ll)) {
            return false;
        }
//        for (Long roleId : roleIds) {
//            SysAccountRole e = new SysAccountRole();
//            e.setRoleId(roleId);
//            List<SysAccountRole> ll = list(new QueryWrapper<>(e));
//            if (CollectionUtils.isNotEmpty(ll)) {
//                return false;
//            }
//        }
        return true;
    }

    /**
     * 平台用户角色
     *
     * @param platformId
     * @param userId
     * @return
     */
    public List<Long> querRolesByUserId(Long platformId, Long userId) {
        Set<Long> r = new HashSet<>();
        SysAccountRole e = new SysAccountRole();
        e.setUserId(userId);
        e.setPlatformId(platformId);
        List<SysAccountRole> urs = list(new QueryWrapper<>(e));
        if (CollectionUtils.isNotEmpty(urs)) {
            for (SysAccountRole ur : urs) {
                r.add(ur.getRoleId());
            }
        }
        return new ArrayList<>(r);
    }
}
