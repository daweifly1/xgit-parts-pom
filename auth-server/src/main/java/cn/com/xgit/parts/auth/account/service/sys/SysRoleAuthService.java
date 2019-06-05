package cn.com.xgit.parts.auth.account.service.sys;

import cn.com.xgit.parts.auth.account.service.base.BaseService;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.module.menu.vo.SysRoleAuthVO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysAuthsDO;
import cn.com.xgit.parts.auth.account.dao.entity.sys.SysRoleAuthDO;
import cn.com.xgit.parts.auth.account.dao.mapper.sys.SysAuthsMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.sys.SysRoleAuthMapper;
import com.xgit.bj.common.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

/**
 * SysRoleAuth 后台接口实现类
 */
@Slf4j
@Service
public class SysRoleAuthService extends BaseService<SysRoleAuthVO, SysRoleAuthDO> {

    @Autowired
    private SysRoleAuthMapper sysRoleAuthMapper;

    @Autowired
    private SysAuthsMapper sysAuthsMapper;

    @PostConstruct
    public void init() {
        super.addMapper(sysRoleAuthMapper);
    }

    protected SysRoleAuthService() {
        super(SysRoleAuthVO.class, SysRoleAuthDO.class);
    }

    /**
     * 查询列表
     *
     * @param vo
     */
    public List<SysRoleAuthDO> queryList(SysRoleAuthVO vo) {
        return sysRoleAuthMapper.queryList(vo);
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
            i = +sysRoleAuthMapper.deleteByPrimaryKeySelective(id);
        }
        return i;
    }

    /**
     * 根据id集合查询并返回map(id->do)
     */
    public Map<Integer, SysRoleAuthDO> queryMapByIds(Set<Integer> ids) {
        final Map<Integer, SysRoleAuthDO> result = new HashMap<>();
        if (CollectionUtils.isEmpty(ids)) {
            return result;
        }
        CollectionUtil.split(new ArrayList<Integer>(ids), 900, new CollectionUtil.PageProcess<Integer>() {
            @Override
            public void process(List<Integer> pageIdList, Object... params) {
                List<SysRoleAuthDO> ll = sysRoleAuthMapper.queryListByIds(new ArrayList<Integer>(pageIdList));
                if (CollectionUtils.isNotEmpty(ll)) {
                    for (SysRoleAuthDO sysRoleAuthDO : ll) {
                        result.put(sysRoleAuthDO.getId(), sysRoleAuthDO);
                    }
                }
            }
        });
        return result;
    }

    public List<SysRoleAuthDO> queryListRoleId(String roleId) {
        SysRoleAuthVO vo = new SysRoleAuthVO();
        vo.setRoleId(roleId);
        return queryList(vo);
    }


    /**
     * 角色对应的权限code相对固定可以考虑缓存下
     *
     * @param roleIds
     * @return
     */
//    @GuavaLocalCache(expireTime = 300, refreshTime = 100, group = "user_group", preFix = "queryAuthCodes_", keyExt = "#roleIds", nullAble = false)
    public List<String> queryAuthCodes(Set<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return null;
        }
        SysRoleAuthVO vo = new SysRoleAuthVO();
        vo.setRoleIdList(new ArrayList<>(roleIds));
        List<SysRoleAuthDO> ll = queryList(vo);
        if (CollectionUtils.isEmpty(ll)) {
            return null;
        }
        Set<Integer> authIdSet = new HashSet<>();
        for (SysRoleAuthDO ra : ll) {
            authIdSet.add(ra.getAuthId());
        }
        List<SysAuthsDO> auths = sysAuthsMapper.queryListByIds(new ArrayList<>(authIdSet));
        if (CollectionUtils.isEmpty(auths)) {
            return null;
        }
        Set<String> authCodeSet = new HashSet<>();
        for (SysAuthsDO a : auths) {
            if (StringUtils.isNoneBlank(a.getCode())) {
                authCodeSet.add(a.getCode());
            }
            if (StringUtils.isNoneBlank(a.getUrl())) {
                authCodeSet.add(a.getUrl());
            }
        }
        return new ArrayList<>(authCodeSet);
    }

    public List<Integer> queryAuthIds(Set<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        SysRoleAuthVO vo = new SysRoleAuthVO();
        vo.setRoleIdList(new ArrayList<>(roleIds));
        List<SysRoleAuthDO> ll = queryList(vo);
        if (CollectionUtils.isEmpty(ll)) {
            return new ArrayList<>();
        }
        Set<Integer> authIdSet = new HashSet<>();
        for (SysRoleAuthDO ra : ll) {
            authIdSet.add(ra.getAuthId());
        }
        List<SysAuthsDO> auths = sysAuthsMapper.queryListByIds(new ArrayList<>(authIdSet));
        if (CollectionUtils.isEmpty(auths)) {
            return new ArrayList<>();
        }
        Set<Integer> authCodeSet = new HashSet<>();
        for (SysAuthsDO a : auths) {
            authCodeSet.add(a.getId());
        }
        return new ArrayList<>(authCodeSet);
    }

    public List<SysRoleAuthDO> queryAuthCodeExist(Set<String> roleIds, String url) {
        SysAuthsVO auCondition = new SysAuthsVO();
        auCondition.setUrl(url);
        List<SysAuthsDO> ll = sysAuthsMapper.queryList(auCondition);
        if (CollectionUtils.isEmpty(ll)) {
            return new ArrayList<>();
        }
        for (SysAuthsDO authsDO : ll) {
            SysRoleAuthVO condition = new SysRoleAuthVO();
            condition.setRoleIdList(new ArrayList<>(roleIds));
            condition.setAuthId(authsDO.getId());
            return sysRoleAuthMapper.queryList(condition);
        }
        return new ArrayList<>();
    }
}
