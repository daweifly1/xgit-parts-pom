package cn.com.xgit.parts.auth.account.dao.entity;

public class WorkspaceDO
{
  private String id;
  private String name;
  private Integer site;
  private String remark;
  private String status;
  private String tempId;
  private String type;
  
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public Integer getSite()
  {
    return this.site;
  }
  
  public void setSite(Integer site)
  {
    this.site = site;
  }
  
  public String getRemark()
  {
    return this.remark;
  }
  
  public void setRemark(String remark)
  {
    this.remark = remark;
  }
  
  public String getStatus()
  {
    return this.status;
  }
  
  public void setStatus(String status)
  {
    this.status = status;
  }
  
  public String getTempId()
  {
    return this.tempId;
  }
  
  public void setTempId(String tempId)
  {
    this.tempId = tempId;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
}
