package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import cn.com.xgit.parts.auth.VO.ThirdpartySecretVO;
import cn.com.xgit.parts.auth.account.dao.entity.ThirdpartySecretDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public abstract interface ThirdpartySecretMapper extends BaseMapper<ThirdpartySecretVO, ThirdpartySecretDO> {
    public abstract ThirdpartySecretDO itemByAppId(String paramString);
}
