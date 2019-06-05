package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.VO.MenuConditionVO;
import cn.com.xgit.parts.auth.account.dao.entity.AuthDO;
import cn.com.xgit.parts.auth.account.dao.entity.MenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public abstract interface MenuMapper {
    public abstract int deleteByPrimaryKey(Integer paramInteger);

    public abstract int insert(MenuDO paramMenuDO);

    public abstract int insertSelective(MenuDO paramMenuDO);

    public abstract MenuDO selectByPrimaryKey(Integer paramInteger);

    public abstract int updateByPrimaryKeySelective(MenuDO paramMenuDO);

    public abstract int updateByPrimaryKey(MenuDO paramMenuDO);

    public abstract List<MenuDO> selectByMenuIds(List<Integer> paramList);

    public abstract List<MenuDO> selectMenus(@Param("all") Boolean paramBoolean, @Param("channel") int paramInt);

    public abstract List<MenuDO> getParentList();

    public abstract List<MenuDO> getAllList();

    public abstract List<MenuDO> getDisplayedList(Integer paramInteger);

    public abstract Integer checkIsExitChild(Integer paramInteger);

    public abstract List<MenuDO> getChildList(Integer paramInteger);

    public abstract List<MenuDO> getChildrenBySite(@Param("parentId") Integer paramInteger1, @Param("site") Integer paramInteger2);

    public abstract List<MenuDO> queryListMenuForConfig(@Param("parentId") Integer paramInteger1, @Param("site") Integer paramInteger2);

    public abstract List<MenuDO> getMenuByRoleIds(List<String> paramList);

    public abstract List<Integer> listShownMenu();

    public abstract List<Integer> listDisplayMenuId();

    public abstract List<Integer> getAuthIdList(Integer paramInteger);

    public abstract List<AuthDO> selectAuthListByMenu(Integer paramInteger);

    public abstract Integer getAuthIdByMenu(Integer paramInteger);

    public abstract List<AuthDO> selectAuthListByRole(String paramString);

    public abstract List<Integer> selectBySite(Integer paramInteger);

    public abstract List<MenuDO> listMenus(MenuConditionVO paramMenuConditionVO);

    public abstract MenuDO getById(Integer paramInteger);

    public abstract Integer getMaxId();

    public abstract List<MenuDO> getCodeBySiteAndChannel(@Param("site") Integer paramInteger, @Param("channel") int paramInt);

    List<AuthDO> selectAuthListByMenuIds(@Param(value = "menuList") List<Integer> menuList);
}
