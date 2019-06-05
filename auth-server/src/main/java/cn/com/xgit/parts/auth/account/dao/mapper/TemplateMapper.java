package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.entity.TemplateDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public abstract interface TemplateMapper
{
  public abstract int insert(TemplateDO paramTemplateDO);
  
  public abstract TemplateDO selectById(String paramString);
  
  public abstract List<TemplateDO> queryList();
  
  public abstract int updateById(TemplateDO paramTemplateDO);
  
  public abstract int removeById(String paramString);
  
  public abstract List<TemplateDO> queryTemplateBySite(Integer paramInteger);
}
