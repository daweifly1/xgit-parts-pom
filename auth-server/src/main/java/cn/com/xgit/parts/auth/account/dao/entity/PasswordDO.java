package cn.com.xgit.parts.auth.account.dao.entity;

import java.sql.Date;

public class PasswordDO
  extends PasswordDOKey
{
  private String password;
  private Date updateTime;
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String password)
  {
    this.password = (password == null ? null : password.trim());
  }
  
  public Date getUpdateTime()
  {
    return this.updateTime;
  }
  
  public void setUpdateTime(Date updateTime)
  {
    this.updateTime = updateTime;
  }
}
