package cn.com.xgit.parts.auth.account.dao.entity;

public class PasswordDOKey
{
  private String userId;
  private Integer type;
  
  public void setType(Integer type)
  {
    this.type = type;
  }
  
  public Integer getType()
  {
    return this.type;
  }
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
}
