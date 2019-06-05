package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.entity.TmpMenuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public abstract interface TmpMenuMapper
{
  public abstract int insert(TmpMenuDO paramTmpMenuDO);
  
  public abstract List<Integer> queryList(String paramString);
  
  public abstract int removeByTemplate(String paramString);
  
  public abstract int removeMenu(TmpMenuDO paramTmpMenuDO);
}
