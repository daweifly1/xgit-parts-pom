package cn.com.xgit.parts.auth.module.account.mapper;

import cn.com.xgit.parts.auth.common.base.SuperMapper;
import cn.com.xgit.parts.auth.module.account.entity.SysAccountRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAccountRoleMapper extends SuperMapper<SysAccountRole> {

    /**
     * 根据角色查询limit个用户引用角色的关系
     *
     * @param roleIds
     * @return
     */
    List<SysAccountRole> queryListByRoleIds(@Param("roleIds") List<Long> roleIds, @Param("limit") int limit);
}
