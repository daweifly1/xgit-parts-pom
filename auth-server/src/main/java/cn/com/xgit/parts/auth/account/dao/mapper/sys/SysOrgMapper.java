package cn.com.xgit.parts.auth.account.dao.mapper.sys;

import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import cn.com.xgit.parts.auth.module.org.vo.SysOrgVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysOrgDO;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 组织结构信息表 Mapper
 */
@Mapper
public interface SysOrgMapper  extends BaseMapper<SysOrgVO, SysOrgDO> {

    PageCommonVO list(SearchCommonVO<SysOrgVO> condition);

    List<SysOrgDO> queryList(SysOrgVO condition);

    int insert(SysOrgDO model);

    int merge(SysOrgDO model);

    int updateByPrimaryKeySelective(SysOrgDO bean);

    int deleteByPrimaryKeySelective(String id);

    List<SysOrgDO> queryListByIds(@Param(value = "ids") List<String> ids);

}
