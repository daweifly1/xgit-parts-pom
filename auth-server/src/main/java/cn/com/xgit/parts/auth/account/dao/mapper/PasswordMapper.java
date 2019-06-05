package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.entity.PasswordDO;
import cn.com.xgit.parts.auth.account.dao.entity.PasswordDOKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public abstract interface PasswordMapper {
    public abstract int deleteByPrimaryKey(PasswordDOKey paramPasswordDOKey);

    public abstract int insert(PasswordDO paramPasswordDO);

    public abstract PasswordDO selectByPrimaryKey(PasswordDOKey paramPasswordDOKey);

    public abstract int updateByPrimaryKey(PasswordDO paramPasswordDO);

    public abstract int checkByPrimaryKey(PasswordDOKey paramPasswordDOKey);
}
