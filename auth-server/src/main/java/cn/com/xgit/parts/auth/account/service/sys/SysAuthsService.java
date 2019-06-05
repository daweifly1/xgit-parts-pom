package cn.com.xgit.parts.auth.account.service.sys;

import cn.com.xgit.parts.auth.account.service.base.BaseService;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysAuthsDO;
import cn.com.xgit.parts.auth.account.dao.mapper.sys.SysAuthsMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.common.util.BeanUtil;
import com.xgit.bj.common.util.CollectionUtil;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SysAuths 后台接口实现类
 */
@Slf4j
@Service
public class SysAuthsService extends BaseService<SysAuthsVO, SysAuthsDO> {
    @Autowired
    private SysAuthsMapper sysAuthsMapper;

    @PostConstruct
    public void init() {
        super.addMapper(sysAuthsMapper);
    }

    protected SysAuthsService() {
        super(SysAuthsVO.class, SysAuthsDO.class);
    }

    /**
     * 查询列表
     *
     * @param condition
     */
    public PageCommonVO<SysAuthsVO> list(SearchCommonVO<SysAuthsVO> condition) {
        if (null == condition.getFilters()) {
            condition.setFilters(new SysAuthsVO());
        }
        PageCommonVO<SysAuthsVO> page = super.list(condition);
        return page;
    }

    /**
     * 查询列表
     *
     * @param vo
     */
    public List<SysAuthsDO> queryList(SysAuthsVO vo) {
        return sysAuthsMapper.queryList(vo);
    }


    /**
     * 查询单条记录
     *
     * @param id
     */
    public SysAuthsDO queryById(Integer id) {
        SysAuthsVO vo = new SysAuthsVO();
        vo.setId(id);
        List<SysAuthsDO> list = queryList(vo);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 保存数据
     *
     * @param sysAuthsVO
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public ErrorCode save(SysAuthsVO sysAuthsVO, String userId) throws Exception {
        doSave(sysAuthsVO, userId);
        return ErrorCode.Success;
    }


    /**
     * 根据状态，等信息保存数据
     *
     * @param sysAuthsVO
     * @param userId
     */
    private void doSave(SysAuthsVO sysAuthsVO, String userId) throws Exception {
        //保存校验
        preSaveCheck(sysAuthsVO);
        SysAuthsDO sysAuthsDO = getDO(sysAuthsVO);
        if (null == sysAuthsDO.getId()) {
            sysAuthsDO.setCreateId(userId);
            sysAuthsDO.setCreateDate(new Date());
            sysAuthsDO.setChannel(1);
            sysAuthsDO.setLeaf(1);
            sysAuthsDO.setShowFlag(1);
            log.info("新增操作，sysAuthsDO:{}", sysAuthsDO);
        } else {
            log.info("编辑操作，sysAuthsDO:{}", sysAuthsDO);
        }
        boolean isSuccess = this.merge(sysAuthsDO) > 0;
        if (!isSuccess) {
            throw new Exception("保存记录失败");
        }
    }

    /**
     * 保存前信息校验
     */
    private void preSaveCheck(SysAuthsVO sysAuthsVO) throws Exception {
        if (null == sysAuthsVO) {
            throw new Exception(ErrorCode.IllegalArument.getDesc());
        }
        //编辑,需要做的校验 。。。
        if (null != sysAuthsVO.getId()) {
            SysAuthsDO model = queryById(sysAuthsVO.getId());
            if (null == model) {
                throw new RuntimeException("资源ID不存在");
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public int merge(SysAuthsDO dto) {
        if (null == dto.getId()) {
            return sysAuthsMapper.insert(dto);
        }
        return sysAuthsMapper.updateByPrimaryKeySelective(dto);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
//    @Caching(evict = {
//            @CacheEvict(value = ROLE, key = "T(com.xgit.bj.auth.user.manager.cache.CacheKeyUtil).ALL_ROLES"),
//            @CacheEvict(value = USER, allEntries = true)
//    })
    public int batchDelete(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        int i = 0;
        for (Integer id : ids) {
            i = +sysAuthsMapper.deleteByPrimaryKeySelective(id);
        }
        return i;
    }


    /**
     * 根据id集合查询并返回map(id->do)
     */
    public Map<Integer, SysAuthsDO> queryMapByIds(Set<Integer> ids) {
        final Map<Integer, SysAuthsDO> result = new HashMap<>();
        if (CollectionUtils.isEmpty(ids)) {
            return result;
        }
        CollectionUtil.split(new ArrayList<>(ids), 900, new CollectionUtil.PageProcess<Integer>() {
            @Override
            public void process(List<Integer> pageIdList, Object... params) {
                List<SysAuthsDO> ll = sysAuthsMapper.queryListByIds(new ArrayList<Integer>(pageIdList));
                if (CollectionUtils.isNotEmpty(ll)) {
                    for (SysAuthsDO sysAuthsDO : ll) {
                        result.put(sysAuthsDO.getId(), sysAuthsDO);
                    }
                }
            }
        });
        return result;
    }

    //可以使用全局localCache
//    @GuavaLocalCache(expireTime = 300, refreshTime = 150, group = "user_group", preFix = "queryAllVOList_", nullAble = false)
    public List<SysAuthsVO> queryAllVOList() {
        return queryVOList(null);
    }

    //可以使用全局localCache
//    @GuavaLocalCache(expireTime = 300, refreshTime = 150, group = "user_group", preFix = "queryAllMenuVOList_", nullAble = false)
    public List<SysAuthsVO> queryAllMenuVOList() {
        return queryVOList(null);
    }

    /**
     * 查询所有树形信息
     *
     * @param condition
     * @return
     */
    public List<SysAuthsVO> queryVOList(SysAuthsVO condition) {
        if (null == condition) {
            condition = new SysAuthsVO();
            condition.setShowFlag(1);
        }
        //顶级节点id
        Integer top = 0;
        List<SysAuthsVO> level1 = new ArrayList<>();
        List<SysAuthsDO> ll = queryList(condition);
        //父子节点关系
        Map<Integer, List<Integer>> id2Children = new HashMap<>();
        Map<Integer, SysAuthsVO> sysAuthsVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ll)) {
            for (SysAuthsDO sysAuthsDO : ll) {
                List<Integer> childrenList = id2Children.get(sysAuthsDO.getParentId());
                if (null == childrenList) {
                    childrenList = new ArrayList<>();
                }
                if (childrenList.contains(sysAuthsDO.getId())) {
                    continue;
                }
                childrenList.add(sysAuthsDO.getId());
                id2Children.put(sysAuthsDO.getParentId(), childrenList);

                SysAuthsVO sysAuthsVO = BeanUtil.do2bo(sysAuthsDO, SysAuthsVO.class);
                if (top == sysAuthsDO.getParentId()) {
                    level1.add(sysAuthsVO);
                }
                sysAuthsVOMap.put(sysAuthsDO.getId(), sysAuthsVO);
            }
        }

        if (CollectionUtils.isNotEmpty(level1)) {
            for (SysAuthsVO sysAuthsDO : level1) {
                sysAuthsDO.setChildren(new ArrayList<>());
                doGetChildren(sysAuthsDO, id2Children, sysAuthsVOMap);
            }
        }
        return level1;
    }

    private void doGetChildren(SysAuthsVO sysAuthsDO, Map<Integer, List<Integer>> id2Children, Map<Integer, SysAuthsVO> sysAuthsVOMap) {
        List<Integer> children = id2Children.get(sysAuthsDO.getId());
        if (CollectionUtils.isNotEmpty(children)) {
            sysAuthsDO.setLeaf(0);
            for (Integer id : children) {
                SysAuthsVO child = sysAuthsVOMap.get(id);
                if (null != child) {
                    child.setChildren(new ArrayList<>());
                    doGetChildren(child, id2Children, sysAuthsVOMap);
                    sysAuthsDO.getChildren().add(child);
                }
            }
        } else {
            sysAuthsDO.setLeaf(1);
        }

    }

    public ErrorCode remove(SysAuthsVO condition, String userId) {
        if (null == condition || null == condition.getId()) {
            return ErrorCode.Failure;
        }
        List<Integer> ids = processIds(condition.getId());
        int r = batchDelete(ids);
        if (r > 0) {
            return ErrorCode.Success;
        }
        return ErrorCode.Failure;
    }

    private List<Integer> processIds(Integer id) {
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        Map<Integer, List<Integer>> id2Children = queryAllParent2ChildrenMap(id);
        if (MapUtils.isNotEmpty(id2Children)) {
            collectIdsFromMap(id, id2Children, ids);
        }
        return ids;
    }

    private void collectIdsFromMap(Integer id, Map<Integer, List<Integer>> id2Children, List<Integer> ids) {
        List<Integer> children = id2Children.get(id);
        if (CollectionUtils.isNotEmpty(children)) {
            ids.addAll(children);
            for (Integer iid : children) {
                collectIdsFromMap(iid, id2Children, ids);
            }
        }
    }

    /**
     * 从某个节点查询父子关系map
     *
     * @param id
     * @return
     */
    private Map<Integer, List<Integer>> queryAllParent2ChildrenMap(Integer id) {
        SysAuthsVO condition = new SysAuthsVO();
        condition.setShowFlag(1);
        condition.setParentId(id);
        List<SysAuthsDO> ll = queryList(condition);
        Map<Integer, List<Integer>> id2Children = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ll)) {
            for (SysAuthsDO sysAuthsDO : ll) {
                List<Integer> childrenList = id2Children.get(sysAuthsDO.getParentId());
                if (null == childrenList) {
                    childrenList = new ArrayList<>();
                }
                if (childrenList.contains(sysAuthsDO.getId())) {
                    continue;
                }
                childrenList.add(sysAuthsDO.getId());
                id2Children.put(sysAuthsDO.getParentId(), childrenList);
            }
        }
        return id2Children;
    }
}
