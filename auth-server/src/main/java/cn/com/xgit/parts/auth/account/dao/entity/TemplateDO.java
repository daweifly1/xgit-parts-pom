package cn.com.xgit.parts.auth.account.dao.entity;

public class TemplateDO
{
  private String id;
  private String name;
  private Integer site;
  private String remark;
  
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
}
