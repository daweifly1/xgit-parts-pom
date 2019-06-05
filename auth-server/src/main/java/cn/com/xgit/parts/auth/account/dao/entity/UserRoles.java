package cn.com.xgit.parts.auth.account.dao.entity;

public class UserRoles
{
  private String userId;
  private String roleId;
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  public String getRoleId()
  {
    return this.roleId;
  }
  
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
}
