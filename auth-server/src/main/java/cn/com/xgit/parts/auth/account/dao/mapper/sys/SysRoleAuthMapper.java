package cn.com.xgit.parts.auth.account.dao.mapper.sys;

import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import cn.com.xgit.parts.auth.module.menu.vo.SysRoleAuthVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysRoleAuthDO;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限关联关系表 Mapper
 */
@Mapper
public interface SysRoleAuthMapper extends BaseMapper<SysRoleAuthVO, SysRoleAuthDO> {

    PageCommonVO list(SearchCommonVO<SysRoleAuthVO> condition);

    List<SysRoleAuthDO> queryList(SysRoleAuthVO condition);

    int insert(SysRoleAuthDO model);

    int batchInsert(List<SysRoleAuthDO> list);

    int merge(SysRoleAuthDO model);

    int updateByPrimaryKeySelective(SysRoleAuthDO bean);

    int deleteByPrimaryKeySelective(String id);

    List<SysRoleAuthDO> queryListByIds(@Param(value = "ids") List<Integer> ids);

    void deleteByRole(@Param(value = "roleId") String roleId);

}
