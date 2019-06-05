package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.entity.AccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public abstract interface AccountMapper {
    public abstract int deleteByPrimaryKey(String paramString);

    public abstract int deleteByUserId(String paramString);

    public abstract int insert(AccountDO paramAccountDO);

    public abstract int insertSelective(AccountDO paramAccountDO);

    public abstract AccountDO selectByPrimaryKey(String paramString);

    public abstract AccountDO selectByUserId(String paramString);

    public abstract List<AccountDO> selectListByUserId(String paramString);

    public abstract int updateByPrimaryKeySelective(AccountDO paramAccountDO);

    public abstract int updateByPrimaryKey(AccountDO paramAccountDO);

    public abstract int checkByPrimaryKey(String paramString);

    public abstract int checkByUserId(String paramString);

    public abstract int checkLoginName(AccountDO paramAccountDO);

    public abstract Date getLastLoginTime(String paramString);

    public abstract int updateLoginTime(String paramString);

    public abstract String selectUserIdByAccount(String paramString);

    List<AccountDO> queryAccountsByUserIds(@Param(value = "userIds") List<String> userIds);
}
