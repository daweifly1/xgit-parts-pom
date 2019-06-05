package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.entity.RoleAuthDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public abstract interface RoleAuthMapper
{
  public abstract int insertSelective(RoleAuthDO paramRoleAuthDO);
  
  public abstract int deleteAuth(String paramString);
}
