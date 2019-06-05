package cn.com.xgit.parts.auth.account.service.sys;

import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import cn.com.xgit.parts.auth.module.account.vo.SysUserRolesVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysRoleDO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysUserRolesDO;
import cn.com.xgit.parts.auth.account.dao.mapper.sys.SysRoleMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.sys.SysUserRolesMapper;
import cn.com.xgit.parts.auth.account.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SysUserRoles 后台接口实现类
 */
@Slf4j
@Service
public class SysUserRolesService extends BaseService<SysUserRolesVO, SysUserRolesDO> {

    @Autowired
    private SysUserRolesMapper sysUserRolesMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @PostConstruct
    public void init() {
        super.addMapper(sysUserRolesMapper);
    }

    protected SysUserRolesService() {
        super(SysUserRolesVO.class, SysUserRolesDO.class);
    }


    /**
     * 查询列表
     *
     * @param vo
     */
    public List<SysUserRolesDO> queryList(SysUserRolesVO vo) {
        return sysUserRolesMapper.queryList(vo);
    }

    @Transactional
    public void updateUserRole(List<String> roleIds, String userId) {
        sysUserRolesMapper.deleteByUserId(userId);
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<SysUserRolesDO> ll = new ArrayList<>(roleIds.size());
            for (String roleId : roleIds) {
                ll.add(new SysUserRolesDO(userId, roleId, 1));
            }
            sysUserRolesMapper.batchInsert(ll);
        }
    }


    /**
     * 用户角色关系数据，数据量相对少可以考虑缓存
     *
     * @param userId
     * @return
     */
    public List<SysUserRolesDO> queryUserRoles(String userId) {
        SysUserRolesVO c = new SysUserRolesVO();
        c.setUserId(userId);
        return sysUserRolesMapper.queryList(c);
    }

    //用户对应的角色
    public Map<String, List<SysRoleDO>> queryUserRolesByUserIds(Set<String> userSet) {
        Map<String, List<SysRoleDO>> result = new HashMap<>();
        if (CollectionUtils.isEmpty(userSet)) {
            return result;
        }
        SysUserRolesVO vo = new SysUserRolesVO();
        vo.setUserIdList(new ArrayList<>(userSet));
        List<SysUserRolesDO> surList = queryList(vo);
        if (CollectionUtils.isEmpty(userSet)) {
            return result;
        }

        Set<String> roleIdSet = new HashSet<>();
        surList.forEach((x) -> roleIdSet.add(x.getRoleId()));
        SysRoleVO cc = new SysRoleVO();
        cc.setIdList(new ArrayList<>(roleIdSet));
        List<SysRoleDO> ll = sysRoleMapper.queryList(cc);
        if (CollectionUtils.isEmpty(userSet)) {
            return result;
        }
        Map<String, SysRoleDO> id2RoleMap = new HashMap<>();
        ll.forEach(x -> id2RoleMap.put(x.getId(), x));

        surList.forEach((x) -> {
            List<SysRoleDO> roles = result.get(x.getUserId());
            if (null == roles) {
                roles = new ArrayList<>();
            }
            roles.add(id2RoleMap.get(x.getRoleId()));
            result.put(x.getUserId(), roles);

        });
        return result;
    }

    public void deleteByUserId(String id) {
        sysUserRolesMapper.deleteByUserId(id);
    }
}
