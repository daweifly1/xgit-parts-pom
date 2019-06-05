package cn.com.xgit.parts.auth.account.dao.mapper.sys;

import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysRoleDO;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色信息表 Mapper
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleVO, SysRoleDO> {

    PageCommonVO list(SearchCommonVO<SysRoleVO> condition);

    List<SysRoleDO> queryList(SysRoleVO condition);

    int insert(SysRoleDO model);

    int merge(SysRoleDO model);

    int updateByPrimaryKeySelective(SysRoleDO bean);

    int deleteByPrimaryKeySelective(String id);

    List<SysRoleDO> queryListByIds(@Param(value = "ids") List<String> ids);

}
