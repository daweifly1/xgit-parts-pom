package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.entity.WorkspaceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public abstract interface WorkspaceMapper {
    public abstract int insert(WorkspaceDO paramWorkspaceDO);

    public abstract WorkspaceDO selectById(String paramString);

    public abstract List<WorkspaceDO> queryList();

    public abstract int removeById(String paramString);

    public abstract int updateById(WorkspaceDO paramWorkspaceDO);

    public abstract int queryCountByTempId(String paramString);
}
