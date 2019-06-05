package cn.com.xgit.parts.auth.account.dao.entity;

import java.sql.Date;

public class ProfileDO
{
  private String userId;
  private String name;
  private String mobile;
  private String telephone;
  private Integer sex;
  private String deptId;
  private String spaceId;
  private String icon;
  private String nickname;
  private String email;
  private Integer locked;
  private String idNumber;
  private String areaCode;
  private Date createDate;
  private Date updateDate;
  
  public String getIdNumber()
  {
    return this.idNumber;
  }
  
  public void setIdNumber(String idNumber)
  {
    this.idNumber = idNumber;
  }
  
  public String getSpaceId()
  {
    return this.spaceId;
  }
  
  public void setSpaceId(String spaceId)
  {
    this.spaceId = spaceId;
  }
  
  public String getTelephone()
  {
    return this.telephone;
  }
  
  public void setTelephone(String telephone)
  {
    this.telephone = telephone;
  }
  
  public Integer getSex()
  {
    return this.sex;
  }
  
  public void setSex(Integer sex)
  {
    this.sex = sex;
  }
  
  public String getDeptId()
  {
    return this.deptId;
  }
  
  public void setDeptId(String deptId)
  {
    this.deptId = deptId;
  }
  
  public String getIcon()
  {
    return this.icon;
  }
  
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  public String getNickname()
  {
    return this.nickname;
  }
  
  public void setNickname(String nickname)
  {
    this.nickname = nickname;
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  public void setEmail(String email)
  {
    this.email = email;
  }
  
  public Integer getLocked()
  {
    return this.locked;
  }
  
  public void setLocked(Integer locked)
  {
    this.locked = locked;
  }
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getMobile()
  {
    return this.mobile;
  }
  
  public void setMobile(String mobile)
  {
    this.mobile = mobile;
  }
  
  public Date getCreateDate()
  {
    return this.createDate;
  }
  
  public void setCreateDate(Date createDate)
  {
    this.createDate = createDate;
  }
  
  public Date getUpdateDate()
  {
    return this.updateDate;
  }
  
  public void setUpdateDate(Date updateDate)
  {
    this.updateDate = updateDate;
  }
  
  public String getAreaCode()
  {
    return this.areaCode;
  }
  
  public void setAreaCode(String areaCode)
  {
    this.areaCode = areaCode;
  }
}
