package cn.com.xgit.parts.auth.account.dao.mapper.sys;

import cn.com.xgit.parts.auth.module.log.vo.SysOpLogVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysOpLogDO;
import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统操作日志 Mapper
 */
@Mapper
public interface SysOpLogMapper extends BaseMapper<SysOpLogVO, SysOpLogDO> {

    PageCommonVO list(SearchCommonVO<SysOpLogVO> condition);

    List<SysOpLogDO> queryList(SysOpLogVO condition);

    int insert(SysOpLogDO model);

    int merge(SysOpLogDO model);

    int updateByPrimaryKeySelective(SysOpLogDO bean);

    int deleteByPrimaryKeySelective(String id);

    List<SysOpLogDO> queryListByIds(@Param(value = "ids") List<String> ids);

}
