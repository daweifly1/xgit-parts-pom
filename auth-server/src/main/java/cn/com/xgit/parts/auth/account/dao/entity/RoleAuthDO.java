package cn.com.xgit.parts.auth.account.dao.entity;

public class RoleAuthDO
{
  private String roleId;
  private Integer authId;
  
  public String getRoleId()
  {
    return this.roleId;
  }
  
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
  public Integer getAuthId()
  {
    return this.authId;
  }
  
  public void setAuthId(Integer authId)
  {
    this.authId = authId;
  }
}
