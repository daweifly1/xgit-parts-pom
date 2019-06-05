package cn.com.xgit.parts.auth.account.dao.entity;

import java.util.Date;

public class ThirdpartySecretDO
{
  private Integer id;
  private String appId;
  private String appSecret;
  private String token;
  private String aesKey;
  private String workspaceId;
  private Integer settingStatus;
  private Date updateTime;
  
  public void setId(Integer id)
  {
    this.id = id;
  }
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setAppId(String appId)
  {
    this.appId = (appId == null ? null : appId.trim());
  }
  
  public String getAppId()
  {
    return this.appId;
  }
  
  public void setAppSecret(String appSecret)
  {
    this.appSecret = (appSecret == null ? null : appSecret.trim());
  }
  
  public String getAppSecret()
  {
    return this.appSecret;
  }
  
  public void setToken(String token)
  {
    this.token = (token == null ? null : token.trim());
  }
  
  public String getToken()
  {
    return this.token;
  }
  
  public void setAesKey(String aesKey)
  {
    this.aesKey = (aesKey == null ? null : aesKey.trim());
  }
  
  public String getAesKey()
  {
    return this.aesKey;
  }
  
  public void setWorkspaceId(String workspaceId)
  {
    this.workspaceId = (workspaceId == null ? null : workspaceId.trim());
  }
  
  public String getWorkspaceId()
  {
    return this.workspaceId;
  }
  
  public void setSettingStatus(Integer settingStatus)
  {
    this.settingStatus = settingStatus;
  }
  
  public Integer getSettingStatus()
  {
    return this.settingStatus;
  }
  
  public void setUpdateTime(Date updateTime)
  {
    this.updateTime = updateTime;
  }
  
  public Date getUpdateTime()
  {
    return this.updateTime;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer("ThirdpartySecretDO{");
    sb.append("id=").append(this.id);
    sb.append(", appId='").append(this.appId).append('\'');
    sb.append(", appSecret='").append(this.appSecret).append('\'');
    sb.append(", token='").append(this.token).append('\'');
    sb.append(", aesKey='").append(this.aesKey).append('\'');
    sb.append(", workspaceId='").append(this.workspaceId).append('\'');
    sb.append(", settingStatus=").append(this.settingStatus);
    sb.append(", updateTime=").append(this.updateTime);
    sb.append('}');
    return sb.toString();
  }
}
