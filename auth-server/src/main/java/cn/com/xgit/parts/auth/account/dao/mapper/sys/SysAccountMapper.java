package cn.com.xgit.parts.auth.account.dao.mapper.sys;

import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import cn.com.xgit.parts.auth.VO.LockVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysAccountDO;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账号信息表 Mapper
 */
@Mapper
public interface SysAccountMapper extends BaseMapper<SysAccountVO, SysAccountDO> {

    PageCommonVO list(SearchCommonVO<SysAccountVO> condition);

    List<SysAccountDO> queryList(SysAccountVO condition);

    int insert(SysAccountDO model);

    int updateByPrimaryKeySelective(SysAccountDO bean);

    int deleteByPrimaryKeySelective(@Param(value = "userId") String userId);

    List<SysAccountDO> queryListByIds(@Param(value = "userIds") List<String> userIds);


    int updateLock(LockVO lockVO);
}
