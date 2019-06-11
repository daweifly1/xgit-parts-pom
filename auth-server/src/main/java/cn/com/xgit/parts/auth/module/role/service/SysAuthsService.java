package cn.com.xgit.parts.auth.module.role.service;

import cn.com.xgit.parts.auth.common.base.SuperMapper;
import cn.com.xgit.parts.auth.common.base.SuperService;
import cn.com.xgit.parts.auth.exception.CommonException;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.module.role.entity.SysAuths;
import cn.com.xgit.parts.auth.module.role.mapper.SysAuthsMapper;
import cn.com.xgit.parts.common.util.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SysAuths 后台接口实现类
 */
@Slf4j
@Service
public class SysAuthsService extends SuperService<SuperMapper<SysAuths>, SysAuths> {

    @Autowired
    private SysAuthsMapper sysAuthsMapper;
    @Autowired
    private SysRoleAuthService sysRoleAuthService;

    public List<SysAuthsVO> treeList(SysAuths condition, boolean onlyMenu) {
        //顶级节点id
        Long top = 0L;
        List<SysAuthsVO> level1 = new ArrayList<>();
        List<SysAuths> ll = super.list(new QueryWrapper<>(condition));
        //父子节点关系
        Map<Long, List<Long>> id2Children = new HashMap<>();
        Map<Long, SysAuthsVO> sysAuthsVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ll)) {
            for (SysAuths sysAuthsDO : ll) {
                if (onlyMenu && sysAuthsDO.getType() == 2) {
                    continue;
                }
                List<Long> childrenList = id2Children.get(sysAuthsDO.getParentId());
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

    private void doGetChildren(SysAuthsVO sysAuthsDO, Map<Long, List<Long>> id2Children, Map<Long, SysAuthsVO> sysAuthsVOMap) {
        List<Long> children = id2Children.get(sysAuthsDO.getId());
        if (CollectionUtils.isNotEmpty(children)) {
//            sysAuthsDO.setLeaf(0);
            for (Long id : children) {
                SysAuthsVO child = sysAuthsVOMap.get(id);
                if (null != child) {
                    child.setChildren(new ArrayList<>());
                    doGetChildren(child, id2Children, sysAuthsVOMap);
                    sysAuthsDO.getChildren().add(child);
                }
            }
        }
//        } else {
//            sysAuthsDO.setLeaf(1);
//        }
    }

    @Transactional
    public void deleteById(Long authId) {
        //检查是否被角色引用
        boolean used = sysRoleAuthService.queryCheckUsedByAuthId(authId);
        if (used) {
            throw new CommonException("该资源以及有角色使用不得删除");
        }
        boolean r = removeById(authId);
        if (r) {
            throw new CommonException("删除资源失败");
        }
    }

}
