package cn.com.xgit.parts.auth.account.dao.entity;

import java.sql.Date;

public class AccountDO
{
  private String loginName;
  private String userId;
  private Date lastLoginTime;
  private String lastLoginIp;
  private Integer status;
  private Date createTime;
  private Date updateTime;
  
  public String getLoginName()
  {
    return this.loginName;
  }
  
  public void setLoginName(String loginName)
  {
    this.loginName = (loginName == null ? null : loginName.trim());
  }
  
  public Integer getStatus()
  {
    return this.status;
  }
  
  public void setStatus(Integer status)
  {
    this.status = status;
  }
  
  public Date getLastLoginTime()
  {
    return this.lastLoginTime;
  }
  
  public void setLastLoginTime(Date lastLoginTime)
  {
    this.lastLoginTime = lastLoginTime;
  }
  
  public String getLastLoginIp()
  {
    return this.lastLoginIp;
  }
  
  public void setLastLoginIp(String lastLoginIp)
  {
    this.lastLoginIp = (lastLoginIp == null ? null : lastLoginIp.trim());
  }
  
  public Date getCreateTime()
  {
    return this.createTime;
  }
  
  public void setCreateTime(Date createTime)
  {
    this.createTime = createTime;
  }
  
  public Date getUpdateTime()
  {
    return this.updateTime;
  }
  
  public void setUpdateTime(Date updateTime)
  {
    this.updateTime = updateTime;
  }
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  public AccountDO()
  {
    this.lastLoginIp = "";
    this.lastLoginTime = new Date(0L);
  }
}
