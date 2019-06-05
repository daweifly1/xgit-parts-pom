package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.entity.RoleDO;
import cn.com.xgit.parts.auth.account.dao.entity.UserRoles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public abstract interface RoleMapper {
    public abstract int deleteByPrimaryKey(String paramString);

    public abstract int insert(RoleDO paramRoleDO);

    public abstract int insertSelective(RoleDO paramRoleDO);

    public abstract RoleDO selectByPrimaryKey(String paramString);

    public abstract int updateByPrimaryKeySelective(RoleDO paramRoleDO);

    public abstract int updateByPrimaryKey(RoleDO paramRoleDO);

    public abstract int insertRoleUsers(UserRoles paramUserRoles);

    public abstract int deleteRoleUsers(String paramString);

    public abstract List<String> selectUserIdByRoleId(@Param("roleId") String paramString);

    public abstract List<String> selectByWorkspaceId(@Param("workspaceId") String paramString);

    public abstract List<RoleDO> selectAll(@Param("channel") int paramInt, @Param("type") Integer paramInteger, @Param("workspaceId") String paramString);

    public abstract List<RoleDO> queryRolesByDepartment(@Param("deptId") String paramString1, @Param("workspaceId") String paramString2);

    public abstract Long countByName(@Param("roleId") String paramString1, @Param("name") String paramString2, @Param("spaceId") String paramString3, @Param("deptId") String paramString4);

    public abstract Long countUserByRoleId(@Param("roleId") String paramString);

    public abstract List<String> selectRoleIdsByUserId(String paramString);

    public abstract int addUserRole(@Param("roleId") String paramString1, @Param("userId") String paramString2, @Param("userFlag") Integer paramInteger);

    public abstract int addUserRoleRelation(@Param("roleId") String paramString1, @Param("userId") String paramString2, @Param("userFlag") Integer paramInteger);

    public abstract int checkUserRole(@Param("roleId") String paramString1, @Param("userId") String paramString2);

    public abstract int removeUserRole(@Param("roleId") String paramString1, @Param("userId") String paramString2);

    public abstract int removeRoleByUser(String paramString);

    public abstract List<Integer> queryAuthIdsByRoleId(String paramString);

    public abstract int selectRolesReferencedCount(@Param("deptId") String paramString1, @Param("spaceId") String paramString2);

    List<UserRoles> queryUserRolesByUserIds(@Param(value = "userIds") List<String> userIds);

    List<RoleDO> getRolesByIds(@Param(value = "ids") List<String> ids);
}
