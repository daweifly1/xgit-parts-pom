package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import cn.com.xgit.parts.auth.VO.ProfileConditionVO;
import cn.com.xgit.parts.auth.VO.ProfileVO;
import cn.com.xgit.parts.auth.account.dao.entity.ProfileDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public abstract interface ProfileMapper extends BaseMapper<ProfileVO, ProfileDO> {
    public abstract int insert(ProfileDO paramProfileDO);

    public abstract ProfileDO selectById(String paramString);

    public abstract List<ProfileDO> list(ProfileConditionVO paramProfileConditionVO);

    public abstract int removeById(String paramString);

    public abstract int update(ProfileDO paramProfileDO);

    public abstract Integer queryCountByDept(String paramString);

    public abstract int getCountByMobile(ProfileDO paramProfileDO);
}
