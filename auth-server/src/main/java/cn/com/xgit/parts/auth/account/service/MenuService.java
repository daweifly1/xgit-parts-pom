package cn.com.xgit.parts.auth.account.service;

import cn.com.xgit.parts.auth.VO.AuthVO;
import cn.com.xgit.parts.auth.VO.ConfigMenuVO;
import cn.com.xgit.parts.auth.VO.MenuAuthListVO;
import cn.com.xgit.parts.auth.VO.MenuConditionVO;
import cn.com.xgit.parts.auth.VO.MenuDisplayVO;
import cn.com.xgit.parts.auth.VO.MenuVO;
import cn.com.xgit.parts.auth.VO.TemplateVO;
import cn.com.xgit.parts.auth.account.dao.entity.AuthDO;
import cn.com.xgit.parts.auth.account.dao.entity.MenuDO;
import cn.com.xgit.parts.auth.account.dao.entity.ProfileDO;
import cn.com.xgit.parts.auth.account.dao.entity.TmpMenuDO;
import cn.com.xgit.parts.auth.account.dao.entity.WorkspaceDO;
import cn.com.xgit.parts.auth.account.dao.mapper.MenuMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.ProfileMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.RoleMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.RoleMenuMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.WorkspaceMapper;
import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.common.util.BeanUtil;
import com.xgit.bj.common.util.CollectionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    ProfileMapper profileMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    ProfileService profileService;
    @Autowired
    private WorkspaceMapper workspaceMapper;
    @Autowired
    private TemplateService templateService;


    public List<Integer> tempMenusByUserId(String userId) {
        ProfileDO profileDO = this.profileMapper.selectById(userId);
        if (null == profileDO) {
            return new ArrayList();
        }
        WorkspaceDO workspaceDO = this.workspaceMapper.selectById(profileDO.getSpaceId());
        if (null == workspaceDO) {
            return new ArrayList();
        }
        List<Integer> tempMenuIds = this.templateService.queryMenuByTemplate(workspaceDO.getTempId());

        return tempMenuIds;
    }

    private List<MenuDisplayVO> getMenuTreeExt(Integer topId, boolean showFlag, String templateId, Integer site) {
        if (topId == null) {
            topId = 0;
        }

        List<MenuDisplayVO> menuDisplayVOList = queryAllMenu(topId, templateId, site);
        return menuDisplayVOList;
    }

    /**
     * 查询所有菜单并组织成树结构
     *
     * @param topId
     * @param templateId
     * @param site
     * @return
     */
    private List<MenuDisplayVO> queryAllMenu(Integer topId, String templateId, Integer site) {
        List<MenuDisplayVO> level1Menu = new ArrayList<>();
        List<MenuDO> allMenuDOs = this.menuMapper.getAllList();
        if (CollectionUtils.isEmpty(allMenuDOs)) {
            return level1Menu;
        }
        List<Integer> menuIds = queryMenuIdsByTempIdOrSite(templateId, site);
        Map<String, MenuDisplayVO> map = doCalcMenuMap(allMenuDOs, templateId);

        Map<String, List<String>> id2Children = new HashMap<>();
        for (MenuDO menuDO : allMenuDOs) {
            if (!menuIds.contains(menuDO.getId())) {
                continue;
            }
            List<String> childrenSet = id2Children.get(menuDO.getParentId() + "");
            if (null == childrenSet) {
                childrenSet = new ArrayList<>();
            }
            if (childrenSet.contains(menuDO.getId())) {
                continue;
            }
            childrenSet.add(menuDO.getId() + "");
            id2Children.put(menuDO.getParentId() + "", childrenSet);
            if (topId.toString().equals(menuDO.getParentId() + "")) {
                level1Menu.add(BeanUtil.do2bo(menuDO, MenuDisplayVO.class));
            }
        }
        for (MenuDisplayVO menuDisplayVO : level1Menu) {
            List<MenuDisplayVO> children = menuDisplayVO.getChildren();
            if (null == children) {
                menuDisplayVO.setChildren(new ArrayList<>());
            }

            List<MenuDisplayVO> cc = doGetChildren(menuDisplayVO, id2Children, map);
            menuDisplayVO.getChildren().addAll(cc);

        }
        return level1Menu;
    }

    private Map<String, MenuDisplayVO> doCalcMenuMap(List<MenuDO> allMenuDOs, String templateId) {
        Map<String, MenuDisplayVO> map = new HashMap<>();
        Set<Integer> menuIdSet = new HashSet<>(allMenuDOs.size());
        for (MenuDO m : allMenuDOs) {
            MenuDisplayVO vo = BeanUtil.do2bo(m, MenuDisplayVO.class);
            map.put(m.getId() + "", vo);
            menuIdSet.add(m.getId());
            //        vo.setAuths(getMenuAuthListByTemp(vo.getId(), templateId));
        }
        List<Integer> tempAuths = this.templateService.queryAuthByTemplate(templateId);
        Map<Integer, List<AuthVO>> authDOs = doSelectAuthListByMenuSet(menuIdSet, tempAuths);
        for (Map.Entry<String, MenuDisplayVO> m : map.entrySet()) {
            MenuDisplayVO vo = m.getValue();
            List<AuthVO> authVOS = authDOs.get(vo.getId());
            if (CollectionUtils.isEmpty(authVOS)) {
                authVOS = new ArrayList<>();
            }
            vo.setAuths(authVOS);
        }
        return map;
    }

    private Map<Integer, List<AuthVO>> doSelectAuthListByMenuSet(Set<Integer> menuIdSet, List<Integer> tempAuths) {
        final Map<Integer, List<AuthVO>> result = new HashMap<>();
        CollectionUtil.split(new ArrayList<>(menuIdSet), 900, new CollectionUtil.PageProcess<Integer>() {
            @Override
            public void process(List<Integer> pageIdList, Object... params) {
                List<AuthDO> ll = menuMapper.selectAuthListByMenuIds(pageIdList);
                if (CollectionUtils.isNotEmpty(ll)) {
                    for (AuthDO authDO : ll) {
                        if (!tempAuths.contains(authDO.getAuthId())) {
                            continue;
                        }
                        Integer mid = authDO.getMenuId();
                        List<AuthVO> as = result.get(mid);
                        if (null == as) {
                            as = new ArrayList<>();
                        }
                        as.add(BeanUtil.do2bo(authDO, AuthVO.class));
                        result.put(mid, as);
                    }
                }
            }
        });
        return result;
    }

    private List<MenuDisplayVO> doGetChildren(MenuDisplayVO menuDisplayVO, Map<String, List<String>> id2Children, Map<String, MenuDisplayVO> map) {
        List<MenuDisplayVO> result = new ArrayList<>();
        List<String> set = id2Children.get(menuDisplayVO.getId() + "");
        if (CollectionUtils.isNotEmpty(set)) {
            for (String id : set) {
                MenuDisplayVO child = map.get(id);
                List<MenuDisplayVO> children = menuDisplayVO.getChildren();
                if (null == children) {
                    menuDisplayVO.setChildren(new ArrayList<>());
                }
                if (null != child) {
                    child.setChildren(new ArrayList<>());
                    child.getChildren().addAll(doGetChildren(child, id2Children, map));
                    result.add(child);
                }
            }
        }
        return result;
    }

    private List<Integer> queryMenuIdsByTempIdOrSite(String templateId, Integer site) {
        List<Integer> menuIds = new ArrayList();
        if (StringUtils.isNotBlank(templateId)) {
            menuIds = this.templateService.queryMenuByTemplate(templateId);
        } else {
            if (null != site) {
                menuIds = queryMenuIdsBySite(site);
            }
        }
        return menuIds;
    }


    private List<MenuDisplayVO> getMenuTree(Integer parentId, boolean showFlag, String templateId, Integer site) {
        if (parentId == null) {
            parentId = Integer.valueOf(0);
        }
        List<MenuDisplayVO> menuDisplayVOList = new ArrayList();

        List<MenuDO> menuDOList = getChildrenByParentId(parentId, templateId, site);
        for (MenuDO menuDO : menuDOList) {
            MenuDisplayVO menuDisplayVO = new MenuDisplayVO();
            BeanUtils.copyProperties(menuDO, menuDisplayVO);
            menuDisplayVOList.add(menuDisplayVO);
        }
        for (MenuDisplayVO menuDisplayVO : menuDisplayVOList) {
            List<MenuDisplayVO> childrenMenuList = getMenuTree(menuDisplayVO.getId(), showFlag, templateId, site);
            menuDisplayVO.setChildren(childrenMenuList);
        }
        setMenuListWithAuth(menuDisplayVOList, templateId);
        return menuDisplayVOList;
    }

    private void setMenuListWithAuth(List<MenuDisplayVO> menuDisplayVOList, String tempId) {
        if (StringUtils.isNotBlank(tempId)) {
            for (MenuDisplayVO menuDisplayVO : menuDisplayVOList) {
                menuDisplayVO.setAuths(getMenuAuthListByTemp(menuDisplayVO.getId(), tempId));
            }
        } else {
            for (MenuDisplayVO menuDisplayVO : menuDisplayVOList) {
                menuDisplayVO.setAuths(getMenuAuthListBySite(menuDisplayVO.getId()));
            }
        }
    }

    private List<AuthVO> getMenuAuthListByTemp(Integer menuId, String tempId) {
        List<AuthVO> authList = new ArrayList();

        List<AuthDO> authDOs = this.menuMapper.selectAuthListByMenu(menuId);
        List<Integer> tempAuths = this.templateService.queryAuthByTemplate(tempId);
        for (AuthDO authDO : authDOs) {
            if (tempAuths.contains(authDO.getAuthId())) {
                AuthVO authVO = new AuthVO();
                BeanUtils.copyProperties(authDO, authVO);
                authVO.setParentId(menuId);
                authList.add(authVO);
            }
        }
        return authList;
    }

    private List<AuthVO> getMenuAuthListBySite(Integer menuId) {
        List<AuthVO> authList = new ArrayList();

        List<AuthDO> authDOs = this.menuMapper.selectAuthListByMenu(menuId);
        for (AuthDO authDO : authDOs) {
            AuthVO authVO = new AuthVO();
            BeanUtils.copyProperties(authDO, authVO);
            authVO.setParentId(menuId);
            authList.add(authVO);
        }
        return authList;
    }

    public List<MenuAuthListVO> getAuthListByMenu(String userId) {
        List<MenuAuthListVO> menuAuthList = new ArrayList();

        TemplateVO templateVO = this.templateService.queryTempByUserId(userId);
        if (null == templateVO) {
            return new ArrayList();
        }
        List<Integer> menuIdList = this.templateService.queryMenuByTemplate(templateVO.getId());
        List<Integer> tempAuthIds = this.templateService.queryAuthByTemplate(templateVO.getId());
        for (Integer menuId : menuIdList) {
            MenuAuthListVO menuAuth = new MenuAuthListVO();
            List<AuthVO> authList = new ArrayList();

            List<AuthDO> authDOs = this.menuMapper.selectAuthListByMenu(menuId);
            for (AuthDO authDO : authDOs) {
                if (tempAuthIds.contains(authDO.getAuthId())) {
                    AuthVO authVO = new AuthVO();
                    BeanUtils.copyProperties(authDO, authVO);
                    authList.add(authVO);
                }
            }
            menuAuth.setMenuId(menuId);
            menuAuth.setAuths(authList);

            menuAuthList.add(menuAuth);
        }
        return menuAuthList;
    }

    public List<AuthVO> listAuthByUser(String userId) {
        Set<AuthVO> authVOSet = new HashSet();

        List<AuthVO> result = new ArrayList();

        TemplateVO templateVO = this.templateService.queryTempByUserId(userId);

        List<Integer> tempAuthIds = this.templateService.queryAuthByTemplate(templateVO.getId());

        List<String> roleIds = this.roleMapper.selectRoleIdsByUserId(userId);
        for (String roleId : roleIds) {
            List<AuthDO> authDOList = this.menuMapper.selectAuthListByRole(roleId);
            for (AuthDO authDO : authDOList) {
                if (tempAuthIds.contains(authDO.getAuthId())) {
                    AuthVO authVO = new AuthVO();
                    BeanUtils.copyProperties(authDO, authVO);
                    authVOSet.add(authVO);
                }
            }
        }
        result.addAll(authVOSet);
        return result;
    }

    public ErrorCode removeByTemplate(String tmpId) {
        return ErrorCode.Success;
    }

    public List<Integer> queryMenuIdsBySite(Integer site) {
        List<Integer> menuIds = this.menuMapper.selectBySite(site);

        return menuIds;
    }

    private List<AuthVO> queryAuthsByRoleIds(List<String> roleIds) {
        List<AuthVO> authVOList = new ArrayList();
        Set<AuthVO> authVOSet = new HashSet();
        for (String roleId : roleIds) {
            List<AuthDO> authDOList = this.menuMapper.selectAuthListByRole(roleId);
            for (AuthDO authDO : authDOList) {
                AuthVO authVO = new AuthVO();
                BeanUtils.copyProperties(authDO, authVO);
                authVOSet.add(authVO);
            }
        }
        authVOList.addAll(authVOSet);

        return authVOList;
    }

    public List<Integer> queryAuthIdsByRoleIds(List<String> roleIds) {
        List<Integer> authIds = new ArrayList();
        Set<Integer> authIdSet = new HashSet();
        for (String roleId : roleIds) {
            List<Integer> authList = this.roleMapper.queryAuthIdsByRoleId(roleId);
            authIdSet.addAll(authList);
        }
        authIds.addAll(authIdSet);

        return authIds;
    }

    public List<MenuDisplayVO> listMenuBySite(Integer site) {
        Integer parentId = Integer.valueOf(0);
        List<MenuDisplayVO> menuDisplayVOList = new ArrayList();

        List<MenuDO> menuDOList = this.menuMapper.queryListMenuForConfig(parentId, site);
        for (MenuDO menuDO : menuDOList) {
            MenuDisplayVO menuDisplayVO = new MenuDisplayVO();
            BeanUtils.copyProperties(menuDO, menuDisplayVO);
            List<MenuDisplayVO> menuDisplayVOs = getMenuTree(menuDisplayVO.getId(), true, null, site);
            menuDisplayVO.setChildren(menuDisplayVOs);
            menuDisplayVOList.add(menuDisplayVO);
        }
        setMenuListWithAuth(menuDisplayVOList, null);
        return menuDisplayVOList;
    }

    private List<MenuDO> getChildrenByParentId(Integer parentId, String templateId, Integer site) {
        List<MenuDO> allMenuDOs = this.menuMapper.getChildList(parentId);
        List<Integer> menuIds;
        if (StringUtils.isNotBlank(templateId)) {
            menuIds = this.templateService.queryMenuByTemplate(templateId);
        } else {
            if (null != site) {
                menuIds = queryMenuIdsBySite(site);
            } else {
                return new ArrayList();
            }
        }
        List<MenuDO> menuDOs = new ArrayList();
        for (MenuDO menuDO : allMenuDOs) {
            if (menuIds.contains(menuDO.getId())) {
                menuDOs.add(menuDO);
            }
        }
        return menuDOs;
    }

    public List<MenuVO> listMenus(MenuConditionVO menuConditionVO) {
        List<MenuDO> menuDOs = this.menuMapper.listMenus(menuConditionVO);
        List<MenuVO> menuVOs = new ArrayList();
        for (MenuDO menuDO : menuDOs) {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menuDO, menuVO);
            menuVO.setId(Long.valueOf(menuDO.getId().intValue()));
            menuVOs.add(menuVO);
        }
        return menuVOs;
    }

    @Transactional
    public ErrorCode deleteFirstMenu(Integer id, String tmpId) {
        int count = this.roleMenuMapper.getCountByMenuId(id);
        if (count > 0) {
            return ErrorCode.FailedDeleteMenu;
        }
        MenuDO menuDO = this.menuMapper.getById(id);
        if ((menuDO != null) && (menuDO.getChannel().intValue() != 1)) {
            return ErrorCode.FailedDeleteFixedMenu;
        }
        int result = this.menuMapper.deleteByPrimaryKey(id);
        if (result > 0) {
            TmpMenuDO tmpMenuDO = new TmpMenuDO();
            tmpMenuDO.setTmpId(tmpId);
            tmpMenuDO.setMenuId(id);
            result = this.templateService.delTmpMenu(tmpMenuDO);
            if (result > 0) {
                return ErrorCode.Success;
            }
        }
        throw new AuthException(ErrorCode.Failure);
    }

    public ErrorCode updateFirstMenu(ConfigMenuVO configMenuVO) {
        MenuVO menuVO = configMenuVO.getMenuVO();

        MenuDO menuDO = new MenuDO();
        menuDO.setName(menuVO.getName());
        menuDO.setUrl(menuVO.getUrl());
        menuDO.setSeq(menuVO.getSeq());
        menuDO.setId(Integer.valueOf(menuVO.getId().intValue()));
        return this.menuMapper.updateByPrimaryKeySelective(menuDO) > 0 ? ErrorCode.Success : ErrorCode.Failure;
    }

    @Transactional
    public ErrorCode addFirstMenu(ConfigMenuVO configMenuVO) {
        Integer maxId = this.menuMapper.getMaxId();

        List<MenuDO> menuDOs = this.menuMapper.getCodeBySiteAndChannel(configMenuVO.getSite(), configMenuVO.getChannel().intValue());
        MenuVO menuVO = configMenuVO.getMenuVO();
        int tmpCode = 100;
        for (int i = 0; i < menuDOs.size(); i++) {
            if (((MenuDO) menuDOs.get(i)).getCode().equals(String.valueOf(tmpCode))) {
                menuVO.setCode(String.valueOf(tmpCode));
                break;
            }
            tmpCode++;
        }
        if (StringUtils.isBlank(menuVO.getCode())) {
            menuVO.setCode(String.valueOf(tmpCode));
        }
        menuVO.setChannel(menuVO.getChannel());
        menuVO.setLeaf(true);

        MenuDO menuDO = new MenuDO();
        BeanUtils.copyProperties(menuVO, menuDO);
        menuDO.setLeaf(Short.valueOf("0"));
        menuDO.setChannel(configMenuVO.getChannel());
        menuDO.setId(maxId = Integer.valueOf(maxId.intValue() + 1));
        menuDO.setShowFlag(Short.valueOf("1"));
        int result = this.menuMapper.insert(menuDO);
        if (result > 0) {
            TmpMenuDO tmpMenuDO = new TmpMenuDO();
            tmpMenuDO.setTmpId(configMenuVO.getTmpId());
            tmpMenuDO.setMenuId(menuDO.getId());
            result = this.templateService.insertTmpMenu(tmpMenuDO);
            if (result > 0) {
                return ErrorCode.Success;
            }
        }
        throw new AuthException(ErrorCode.Failure);
    }

    public MenuVO view(Integer id) {
        MenuDO menuDO = this.menuMapper.getById(id);
        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menuDO, menuVO);
        menuVO.setId(Long.valueOf(menuDO.getId().intValue()));
        return menuVO;
    }
}
