package cn.com.xgit.parts.auth.account.dao.entity;

public class RoleMenuDO
{
  private String roleId;
  private Integer menuId;
  private Integer channel;
  
  public String getRoleId()
  {
    return this.roleId;
  }
  
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
  public void setMenuId(Integer menuId)
  {
    this.menuId = menuId;
  }
  
  public Integer getMenuId()
  {
    return this.menuId;
  }
  
  public Integer getChannel()
  {
    return this.channel;
  }
  
  public void setChannel(Integer channel)
  {
    this.channel = channel;
  }
}
