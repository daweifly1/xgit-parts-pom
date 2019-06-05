package cn.com.xgit.parts.auth.account.dao.mapper.sys;

import cn.com.xgit.parts.auth.module.account.vo.SysPasswordVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysPasswordDO;
import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 密码信息表 Mapper
 */
@Mapper
public interface SysPasswordMapper  extends BaseMapper<SysPasswordVO, SysPasswordDO> {

    PageCommonVO list(SearchCommonVO<SysPasswordVO> condition);

    List<SysPasswordDO> queryList(SysPasswordVO condition);

    int insert(SysPasswordDO model);

    int merge(SysPasswordDO model);

    int updateByPrimaryKeySelective(SysPasswordDO bean);

    int deleteByPrimaryKeySelective(String id);

    List<SysPasswordDO> queryListByIds(@Param(value = "ids") List<String> ids);

}
