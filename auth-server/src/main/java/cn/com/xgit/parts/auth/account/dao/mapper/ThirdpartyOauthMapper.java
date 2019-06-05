package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import cn.com.xgit.parts.auth.VO.ThirdpartyOauthVO;
import cn.com.xgit.parts.auth.account.dao.entity.ThirdpartyOauthDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public abstract interface ThirdpartyOauthMapper extends BaseMapper<ThirdpartyOauthVO, ThirdpartyOauthDO>
{
  public abstract ThirdpartyOauthDO itemByThirdpartyId(String paramString);
  
  public abstract List<ThirdpartyOauthDO> listBindedByUserId(String paramString);
  
  public abstract List<ThirdpartyOauthDO> listBindedByThirdpartyId(String paramString);
}
