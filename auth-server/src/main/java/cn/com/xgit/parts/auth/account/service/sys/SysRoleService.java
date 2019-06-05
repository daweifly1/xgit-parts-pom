package cn.com.xgit.parts.auth.account.service.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysRoleAuthDO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysRoleDO;
import cn.com.xgit.parts.auth.account.dao.mapper.sys.SysRoleAuthMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.sys.SysRoleMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.service.base.BaseService;
import com.xgit.bj.common.util.CollectionUtil;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * SysRole 后台接口实现类
 */
@Slf4j
@Service
public class SysRoleService extends BaseService<SysRoleVO, SysRoleDO> {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleAuthMapper sysRoleAuthMapper;

    @Autowired
    private SysAuthsService sysAuthsService;

    @Autowired
    private SysRoleAuthService sysRoleAuthService;

    @PostConstruct
    public void init() {
        super.addMapper(sysRoleMapper);
    }

    protected SysRoleService() {
        super(SysRoleVO.class, SysRoleDO.class);
    }

    /**
     * 查询列表
     *
     * @param condition
     */
    public PageCommonVO<SysRoleVO> list(SearchCommonVO<SysRoleVO> condition) {
        if (null == condition.getFilters()) {
            condition.setFilters(new SysRoleVO());
        }
        PageCommonVO pageCommonVO = new PageCommonVO();
        PageHelper.orderBy("id desc");
        PageHelper.startPage(condition.getPageNum().intValue(), condition.getPageSize().intValue());
        List<SysRoleDO> doList = sysRoleMapper.list(condition.getFilters());
        List<SysRoleVO> voList = getVOList(doList);
        pageCommonVO.setPageInfo(new PageInfo(doList));
        pageCommonVO.setPageInfoList(voList);
        return pageCommonVO;
    }

    /**
     * 查询列表
     *
     * @param vo
     */
//    @Cacheable(value = ROLE, key = "T(com.xgit.bj.auth.user.manager.cache.CacheKeyUtil).ALL_ROLES")
    public List<SysRoleDO> queryList(SysRoleVO vo) {
        return sysRoleMapper.queryList(vo);
    }


    /**
     * 查询单条记录
     *
     * @param id
     */
    public SysRoleVO queryById(String id) {
        SysRoleDO sysRoleDO = getById(id);
        if (null == sysRoleDO) {
            return null;
        }
        SysRoleVO result = getVO(sysRoleDO);
        List<SysAuthsVO> tree = sysAuthsService.queryAllVOList();
        Set<Integer> set = getAuthIdSet(id);
        if (CollectionUtils.isNotEmpty(tree) && CollectionUtils.isNotEmpty(set)) {
            for (SysAuthsVO vo : tree) {
                if (set.contains(vo.getId())) {
                    vo.setChecked(true);
                } else {
                    vo.setChecked(false);
                }
                doCheckedChilren(vo, set);
            }
        }
        doIndeterminateChilren(tree);
        result.setTreeAuthList(tree);
        return result;
    }

    private void doIndeterminateChilren(List<SysAuthsVO> tree) {
        for (SysAuthsVO vvo : tree) {
            if (CollectionUtils.isNotEmpty(vvo.getChildren())) {
                vvo.setIndeterminate(queryParentInt(vvo.getChildren()));
            }
        }
    }

    private boolean queryParentInt(List<SysAuthsVO> node) {
        int checked = 0;
        for (SysAuthsVO vvo : node) {
            if (CollectionUtils.isNotEmpty(vvo.getChildren())) {
                vvo.setIndeterminate(queryParentInt(vvo.getChildren()));
                if (vvo.isIndeterminate()) {
                    return true;
                }
            }
            if (vvo.isChecked()) {
                checked++;
            }
        }
        if (checked > 0 && checked < node.size()) {
            return true;
        }
        return false;
    }

    private void doCheckedChilren(SysAuthsVO vo, Set<Integer> set) {
        if (CollectionUtils.isNotEmpty(vo.getChildren())) {
            for (SysAuthsVO vvo : vo.getChildren()) {
                if (set.contains(vvo.getId())) {
                    vvo.setChecked(true);
                } else {
                    vvo.setChecked(false);
                }
                doCheckedChilren(vvo, set);
            }
        }
    }

    private Set<Integer> getAuthIdSet(String roleId) {
        Set<Integer> set = new HashSet<>();
        List<SysRoleAuthDO> ll = sysRoleAuthService.queryListRoleId(roleId);
        if (CollectionUtils.isEmpty(ll)) {
            return set;
        }
        for (SysRoleAuthDO ra : ll) {
            set.add(ra.getAuthId());
        }
        return set;
    }

    private SysRoleDO getById(String id) {
        SysRoleVO vo = new SysRoleVO();
        vo.setId(id);
        List<SysRoleDO> list = queryList(vo);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 保存数据
     *
     * @param sysRoleVO
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
//    @Caching(evict = {
//            @CacheEvict(value = ROLE, key = "T(com.xgit.bj.auth.user.manager.cache.CacheKeyUtil).ALL_ROLES"),
//            @CacheEvict(value = USER, allEntries = true)
//            })
    public ErrorCode save(SysRoleVO sysRoleVO, String userId) throws Exception {
        log.info("save ,{}  --{},{}", sysRoleVO, userId);
        doSave(sysRoleVO, userId);
        return ErrorCode.Success;
    }

    /**
     * 根据状态，等信息保存数据
     *
     * @param sysRoleVO
     * @param userId
     */
    private void doSave(SysRoleVO sysRoleVO, String userId) throws Exception {
        preSaveCheck(sysRoleVO);
        SysRoleDO sysRoleDO = getDO(sysRoleVO);
        boolean isSuccess = this.merge(sysRoleDO) > 0;
        if (!isSuccess) {
            throw new Exception("保存记录失败");
        }
        //根据
        if (CollectionUtils.isNotEmpty(sysRoleVO.getTreeAuthList())) {
            List<SysRoleAuthDO> list = new ArrayList<>();
            //递归添加选中的权限
            addAuthRole(list, sysRoleVO.getTreeAuthList(), sysRoleDO.getId());
            sysRoleAuthMapper.deleteByRole(sysRoleDO.getId());
            sysRoleAuthMapper.batchInsert(list);
        }
    }

    private void addAuthRole(List<SysRoleAuthDO> list, List<SysAuthsVO> authsList, String roleId) {
        if (CollectionUtils.isNotEmpty(authsList)) {
            for (SysAuthsVO sysAuthsVO : authsList) {
                if (sysAuthsVO.isChecked()) {
                    SysRoleAuthDO sysRoleAuthDO = new SysRoleAuthDO();
                    sysRoleAuthDO.setAuthId(sysAuthsVO.getId());
                    sysRoleAuthDO.setRoleId(roleId);
                    sysRoleAuthDO.setStatus(1);
                    list.add(sysRoleAuthDO);
                    if (CollectionUtils.isNotEmpty(sysAuthsVO.getChildren())) {
                        addAuthRole(list, sysAuthsVO.getChildren(), roleId);
                    }
                }
            }
        }
    }

    /**
     * 保存前信息校验
     */
    private void preSaveCheck(SysRoleVO sysRoleVO) throws Exception {
        if (null == sysRoleVO) {
            throw new Exception(ErrorCode.IllegalArument.getDesc());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int merge(SysRoleDO dto) {
        if (null == dto.getId()) {
            dto.setId(UUID.randomUUID().toString());
            dto.setType(2L);
            dto.setChannel(1L);
            dto.setSpaceId("0");
            dto.setDeptId("0");
            dto.setSeqNo(0L);
            return sysRoleMapper.insert(dto);
        }
        return sysRoleMapper.updateByPrimaryKeySelective(dto);
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
    public int batchDelete(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        int i = 0;
        for (String id : ids) {
            sysRoleAuthMapper.deleteByRole(id);
            i = +sysRoleMapper.deleteByPrimaryKeySelective(id);
        }
        return i;
    }


    /**
     * 根据id集合查询并返回map(id->do)
     */
    public Map<String, SysRoleDO> queryMapByIds(Set<String> ids) {
        final Map<String, SysRoleDO> result = new HashMap<>();
        if (CollectionUtils.isEmpty(ids)) {
            return result;
        }
        CollectionUtil.split(new ArrayList<>(ids), 900, new CollectionUtil.PageProcess<String>() {
            @Override
            public void process(List<String> pageIdList, Object... params) {
                List<SysRoleDO> ll = sysRoleMapper.queryListByIds(new ArrayList<String>(pageIdList));
                if (CollectionUtils.isNotEmpty(ll)) {
                    for (SysRoleDO sysRoleDO : ll) {
                        result.put(sysRoleDO.getId(), sysRoleDO);
                    }
                }
            }
        });
        return result;
    }
}
