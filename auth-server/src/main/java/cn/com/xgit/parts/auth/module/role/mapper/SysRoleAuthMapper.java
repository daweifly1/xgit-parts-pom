package cn.com.xgit.parts.auth.module.role.mapper;

import cn.com.xgit.parts.auth.common.base.SuperMapper;
import cn.com.xgit.parts.auth.module.role.entity.SysRoleAuth;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleAuthMapper extends SuperMapper<SysRoleAuth> {

    /**
     * 在角色资源关系中冗余平台id，实现根据角色查询拥有的权限资源id
     *
     * @param platformId
     * @param roleIds
     * @return
     */
    List<SysRoleAuth> queryRoleAuth(@Param("platformId") Long platformId, @Param("roleIds") List<Long> roleIds);

    /**
     * 通过连表的方法实现，查询角色拥有的权限（可以过滤按钮权限）-
     *
     * @param platformId
     * @param roleIds
     * @param onlyMenu
     * @return
     */
    List<SysRoleAuth> queryRoleAuthJoin(@Param("platformId") Long platformId, @Param("roleIds") List<Long> roleIds, @Param("onlyMenu") boolean onlyMenu);
}
