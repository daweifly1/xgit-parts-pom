package cn.com.xgit.parts.auth.account.dao.entity;

public class RoleDO
{
  private String id;
  private String name;
  private String remark;
  private Integer type;
  private Integer channel;
  private String spaceId;
  private String deptId;
  private Integer seqNo;
  
  public Integer getSeqNo()
  {
    return this.seqNo;
  }
  
  public void setSeqNo(Integer seqNo)
  {
    this.seqNo = seqNo;
  }
  
  public String getDeptId()
  {
    return this.deptId;
  }
  
  public void setDeptId(String deptId)
  {
    this.deptId = deptId;
  }
  
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
    this.name = (name == null ? null : name.trim());
  }
  
  public String getRemark()
  {
    return this.remark;
  }
  
  public void setRemark(String remark)
  {
    this.remark = (remark == null ? null : remark.trim());
  }
  
  public Integer getType()
  {
    return this.type;
  }
  
  public void setType(Integer type)
  {
    this.type = type;
  }
  
  public Integer getChannel()
  {
    return this.channel;
  }
  
  public void setChannel(Integer channel)
  {
    this.channel = channel;
  }
  
  public String getSpaceId()
  {
    return this.spaceId;
  }
  
  public void setSpaceId(String spaceId)
  {
    this.spaceId = spaceId;
  }
}
