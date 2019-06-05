package cn.com.xgit.parts.auth.account.dao.entity;

public class TmpAuthDO
{
  private Integer authId;
  private String tmpId;
  
  public Integer getAuthId()
  {
    return this.authId;
  }
  
  public void setAuthId(Integer authId)
  {
    this.authId = authId;
  }
  
  public String getTmpId()
  {
    return this.tmpId;
  }
  
  public void setTmpId(String tmpId)
  {
    this.tmpId = tmpId;
  }
}
