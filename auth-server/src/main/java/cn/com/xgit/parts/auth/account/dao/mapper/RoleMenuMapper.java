package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.entity.RoleMenuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public abstract interface RoleMenuMapper
{
  public abstract int insert(RoleMenuDO paramRoleMenuDO);
  
  public abstract int insertSelective(RoleMenuDO paramRoleMenuDO);
  
  public abstract int deleteMenu(String paramString);
  
  public abstract List<Integer> getMenuByRoleId(String paramString);
  
  public abstract List<Integer> getAuthByRoleId(String paramString);
  
  public abstract List<Integer> getMenuIdsByRoleIds(List<String> paramList);
  
  public abstract int getCountByMenuId(Integer paramInteger);
}
