package cn.com.xgit.parts.auth.account.dao.entity;

public class MenuDO
{
  private Integer id;
  private String name;
  private String code;
  private Long parentId;
  private Integer seq;
  private String icon;
  private String state;
  private Short showFlag;
  private String url;
  private Integer channel;
  private Short leaf;
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id)
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
  
  public String getCode()
  {
    return this.code;
  }
  
  public void setCode(String code)
  {
    this.code = (code == null ? null : code.trim());
  }
  
  public Long getParentId()
  {
    return this.parentId;
  }
  
  public void setParentId(Long parentId)
  {
    this.parentId = parentId;
  }
  
  public Integer getSeq()
  {
    return this.seq;
  }
  
  public void setSeq(Integer seq)
  {
    this.seq = seq;
  }
  
  public String getIcon()
  {
    return this.icon;
  }
  
  public void setIcon(String icon)
  {
    this.icon = (icon == null ? null : icon.trim());
  }
  
  public String getState()
  {
    return this.state;
  }
  
  public void setState(String state)
  {
    this.state = (state == null ? null : state.trim());
  }
  
  public Short getShowFlag()
  {
    return this.showFlag;
  }
  
  public void setShowFlag(Short showFlag)
  {
    this.showFlag = showFlag;
  }
  
  public String getUrl()
  {
    return this.url;
  }
  
  public void setUrl(String url)
  {
    this.url = (url == null ? null : url.trim());
  }
  
  public Integer getChannel()
  {
    return this.channel;
  }
  
  public void setChannel(Integer channel)
  {
    this.channel = channel;
  }
  
  public Short getLeaf()
  {
    return this.leaf;
  }
  
  public void setLeaf(Short leaf)
  {
    this.leaf = leaf;
  }
}
