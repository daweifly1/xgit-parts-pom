package cn.com.xgit.parts.auth.account.dao.mapper.sys;

import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysAuthsDO;
import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限信息Mapper
 */
@Mapper
public interface SysAuthsMapper extends BaseMapper<SysAuthsVO, SysAuthsDO> {

    PageCommonVO list(SearchCommonVO<SysAuthsVO> condition);

    List<SysAuthsDO> queryList(SysAuthsVO condition);

    int insert(SysAuthsDO model);

    int updateByPrimaryKeySelective(SysAuthsDO bean);

    int deleteByPrimaryKeySelective(Integer id);

    List<SysAuthsDO> queryListByIds(@Param(value = "ids") List<Integer> ids);

}
