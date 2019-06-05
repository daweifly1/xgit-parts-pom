package cn.com.xgit.parts.auth.account.dao.entity;

import java.util.Date;

public class ThirdpartyOauthDO
{
  private Integer id;
  private String appId;
  private String userId;
  private String thirdpartyId;
  private Integer bindType;
  private String metaData;
  private String remark;
  private Integer bindStatus;
  private Date bindTime;
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
  
  public void setUserId(String userId)
  {
    this.userId = (userId == null ? null : userId.trim());
  }
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public void setThirdpartyId(String thirdpartyId)
  {
    this.thirdpartyId = (thirdpartyId == null ? null : thirdpartyId.trim());
  }
  
  public String getThirdpartyId()
  {
    return this.thirdpartyId;
  }
  
  public void setBindType(Integer bindType)
  {
    this.bindType = bindType;
  }
  
  public Integer getBindType()
  {
    return this.bindType;
  }
  
  public void setMetaData(String metaData)
  {
    this.metaData = (metaData == null ? null : metaData.trim());
  }
  
  public String getMetaData()
  {
    return this.metaData;
  }
  
  public void setRemark(String remark)
  {
    this.remark = (remark == null ? null : remark.trim());
  }
  
  public String getRemark()
  {
    return this.remark;
  }
  
  public void setBindStatus(Integer bindStatus)
  {
    this.bindStatus = bindStatus;
  }
  
  public Integer getBindStatus()
  {
    return this.bindStatus;
  }
  
  public void setBindTime(Date bindTime)
  {
    this.bindTime = bindTime;
  }
  
  public Date getBindTime()
  {
    return this.bindTime;
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
    StringBuffer sb = new StringBuffer("ThirdpartyOauthDO{");
    sb.append("id=").append(this.id);
    sb.append(", appId='").append(this.appId).append('\'');
    sb.append(", userId='").append(this.userId).append('\'');
    sb.append(", thirdpartyId='").append(this.thirdpartyId).append('\'');
    sb.append(", bindType=").append(this.bindType);
    sb.append(", metaData='").append(this.metaData).append('\'');
    sb.append(", remark='").append(this.remark).append('\'');
    sb.append(", bindStatus=").append(this.bindStatus);
    sb.append(", bindTime=").append(this.bindTime);
    sb.append(", updateTime=").append(this.updateTime);
    sb.append('}');
    return sb.toString();
  }
}
