package cn.com.xgit.parts.auth.account.service;

import cn.com.xgit.parts.auth.account.dao.mapper.RoleAuthMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.RoleMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.RoleMenuMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService
{
  @Autowired
  private RoleMapper roleMapper;
  @Autowired
  private RoleAuthMapper roleAuthMapper;
  @Autowired
  private RoleMenuMapper roleMenuMapper;
  @Value("${role.admin.roleId}")
  private String adminRoleId;
  
  public boolean ifAdminRole(List<String> roleIds)
  {
    return roleIds.contains(this.adminRoleId.trim());
  }
  
  public boolean ifAdminUser(String userId)
  {
    List<String> roleIds = queryRoleIdsByUserId(userId);
    
    return ifAdminRole(roleIds);
  }
  
  public List<String> queryIdsByWorkspaceId(String workspaceId)
  {
    return this.roleMapper.selectByWorkspaceId(workspaceId);
  }
  
  public List<String> queryRoleIdsByUserId(String userId)
  {
    return this.roleMapper.selectRoleIdsByUserId(userId);
  }
  
  public void releaseAuthMenu(String roleId)
  {
    this.roleAuthMapper.deleteAuth(roleId);
    this.roleMenuMapper.deleteMenu(roleId);
  }
  
  public ErrorCode releaseByWorkspace(String workspaceId)
  {
    List<String> roleIds = queryIdsByWorkspaceId(workspaceId);
    if (CollectionUtils.isEmpty(roleIds)) {
      return ErrorCode.Success;
    }
    for (String roleId : roleIds) {
      releaseAuthMenu(roleId);
    }
    return ErrorCode.Success;
  }
}
