package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.entity.TmpAuthDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public abstract interface TmpAuthMapper
{
  public abstract int insert(TmpAuthDO paramTmpAuthDO);
  
  public abstract List<Integer> queryList(String paramString);
  
  public abstract int removeByTemplate(String paramString);
  
  public abstract int removeAuth(TmpAuthDO paramTmpAuthDO);
}
