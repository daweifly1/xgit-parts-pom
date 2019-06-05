package cn.com.xgit.parts.auth.account.service.sys;

import cn.com.xgit.parts.auth.account.service.base.BaseService;
import cn.com.xgit.parts.auth.module.org.vo.SysOrgVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysOrgDO;
import cn.com.xgit.parts.auth.account.dao.mapper.sys.SysOrgMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.service.GuidService;
import com.xgit.bj.common.util.CollectionUtil;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SysOrg 后台接口实现类
 */
@Slf4j
@Service
public class SysOrgService extends BaseService<SysOrgVO, SysOrgDO> {

    @Autowired
    private SysOrgMapper sysOrgMapper;

    @Autowired
    private GuidService guidService;

    @PostConstruct
    public void init() {
        super.addMapper(sysOrgMapper);
    }

    protected SysOrgService() {
        super(SysOrgVO.class, SysOrgDO.class);
    }

    /**
     * 查询列表
     *
     * @param condition
     */
    public PageCommonVO<SysOrgVO> list(SearchCommonVO<SysOrgVO> condition) {
        if (null == condition.getFilters()) {
            condition.setFilters(new SysOrgVO());
        }
        PageCommonVO<SysOrgVO> page = super.list(condition);
        //TODO．．．是否需要其他操作完善数据
        return page;
    }

    /**
     * 查询列表
     *
     * @param vo
     */
    public List<SysOrgDO> queryList(SysOrgVO vo) {
        return sysOrgMapper.queryList(vo);
    }


    /**
     * 查询单条记录
     *
     * @param id
     */
    public SysOrgDO queryById(String id) {
        SysOrgVO vo = new SysOrgVO();
        vo.setId(id);
        List<SysOrgDO> list = queryList(vo);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 保存数据
     *
     * @param sysOrgVO
     * @param userId
     * @param userName
     * @param orgId
     */
    @Transactional(rollbackFor = Exception.class)
    public ErrorCode save(SysOrgVO sysOrgVO, String userId, String userName, String orgId) throws Exception {
        //TODO　是否需要前置校验逻辑
        //初始时候数据状态
        int saveStatus = 0;
        doSave(sysOrgVO, userId, userName, orgId, false, saveStatus);
        //TODO　是否需要后置操作
        return ErrorCode.Success;
    }

    /**
     * 提交数据
     *
     * @param sysOrgVO
     * @param userId
     * @param userName
     * @param orgId
     */
    @Transactional(rollbackFor = Exception.class)
    public ErrorCode submit(SysOrgVO sysOrgVO, String userId, String userName, String orgId) throws Exception {
        //TODO　是否需要前置校验逻辑
        //TODO 提交后的时候数据状态
        int saveStatus = 1;
        doSave(sysOrgVO, userId, userName, orgId, false, saveStatus);
        //TODO　是否需要后置操作
        return ErrorCode.Success;
    }

    /**
     * 根据状态，等信息保存数据
     *
     * @param sysOrgVO
     * @param userId
     * @param userName
     * @param orgId
     */
    private void doSave(SysOrgVO sysOrgVO, String userId, String userName, String orgId, boolean isSubmit, int status) throws Exception {
        //TODO 保存是否需要校验？
        preSaveCheck(sysOrgVO, isSubmit, status);
        SysOrgDO sysOrgDO = getDO(sysOrgVO);
        if (StringUtils.isBlank(sysOrgDO.getId())) {
            log.info("新增操作，sysOrgDO:{}", sysOrgDO);
        } else {
            log.info("编辑操作，sysOrgDO:{}", sysOrgDO);
        }
        boolean isSuccess = this.merge(sysOrgDO) > 0;
        if (!isSuccess) {
            throw new Exception("保存记录失败");
        }
    }

    /**
     * 保存前信息校验
     */
    private void preSaveCheck(SysOrgVO sysOrgVO, boolean isSubmit, int status) throws Exception {
        if (null == sysOrgVO) {
            throw new Exception(ErrorCode.IllegalArument.getDesc());
        }
        //TODO如果是提交操作或者某些状态下需要做校验吗？
        //        if (isSubmit&&status？？？) {
        //        }
        //TODO 如果是编辑,需要做的校验 。。。
        if (StringUtils.isNotBlank(sysOrgVO.getId())) {
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public int merge(SysOrgDO dto) {
        if (null == dto.getId()) {
            return sysOrgMapper.insert(dto);
        }
        return sysOrgMapper.updateByPrimaryKeySelective(dto);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public int batchDelete(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        int i = 0;
        for (String id : ids) {
            i = +sysOrgMapper.deleteByPrimaryKeySelective(id);
        }
        return i;
    }


    /**
     * 根据id集合查询并返回map(id->do)
     */
    public Map<String, SysOrgDO> queryMapByIds(Set<String> ids) {
        final Map<String, SysOrgDO> result = new HashMap<>();
        if (CollectionUtils.isEmpty(ids)) {
            return result;
        }
        CollectionUtil.split(new ArrayList<String>(ids), 900, new CollectionUtil.PageProcess<String>() {
            @Override
            public void process(List<String> pageIdList, Object... params) {
                List<SysOrgDO> ll = sysOrgMapper.queryListByIds(new ArrayList<String>(pageIdList));
                if (CollectionUtils.isNotEmpty(ll)) {
                    for (SysOrgDO sysOrgDO : ll) {
                        result.put(sysOrgDO.getId(), sysOrgDO);
                    }
                }
            }
        });
        return result;
    }
}
