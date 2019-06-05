package cn.com.xgit.parts.auth.account.dao.mapper.sys;

import cn.com.xgit.parts.auth.module.account.vo.SysUserRolesVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysUserRolesDO;
import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色表 Mapper
 */
@Mapper
public interface SysUserRolesMapper extends BaseMapper<SysUserRolesVO, SysUserRolesDO> {

    List<SysUserRolesDO> queryList(SysUserRolesVO condition);

    int insert(SysUserRolesDO model);


    int deleteByUserId(@Param(value = "userId") String id);

    List<SysUserRolesDO> queryListByIds(@Param(value = "ids") List<String> ids);

    void batchInsert(@Param(value = "list") List<SysUserRolesDO> list);
}
