package cn.com.xgit.parts.auth.account.web.sys;

import cn.com.xgit.parts.auth.account.facade.sys.UserAuthFacade;
import cn.com.xgit.parts.auth.module.base.BasicController;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import com.xgit.bj.core.rsp.ResultMessage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/menu"})
public class MenuController extends BasicController {

    @Autowired
    private UserAuthFacade userAuthFacade;
//    @Autowired
//    ScepterService scepterService;
//    @Autowired
//    MenuService menuService;

    @RequestMapping(value = {"/authorized"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ApiOperation("查询按钮集合（树形结构）")
    public ResultMessage<List<SysAuthsVO>> authorizedList(@RequestHeader("x-user-id") String userId) {
        List<SysAuthsVO> menuDisplayVOList = userAuthFacade.queryMenuByUserId(userId);
        return ResultMessage(menuDisplayVOList);
    }

//
//    @RequestMapping(value = {"/shown"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
//    public ResultMessage shownList(@RequestHeader("x-user-id") String userId) {
//        List<MenuDisplayVO> menuDisplayVOList = this.menuService.getShownMenuTree(userId);
//        return ResultMessage(menuDisplayVOList);
//    }
//
//    @RequestMapping(value = {"/menuAuth"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
//    public ResultMessage listAuthByMenu(@RequestHeader("x-user-id") String userId) {
//        List<MenuAuthListVO> menuAuthListVOList = this.menuService.getAuthListByMenu(userId);
//        return ResultMessage(menuAuthListVOList);
//    }
//
//    @RequestMapping(value = {"/listUserAuth"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
//    @FastMappingInfo(needLogin = true)
//    public ResultMessage listAuthByUser(@RequestHeader("x-user-id") String userId) {
//        List<AuthVO> authVOs = this.menuService.listAuthByUser(userId);
//        return ResultMessage(authVOs);
//    }
//
//    @RequestMapping(value = {"/listMenuBySite"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
//    @FastMappingInfo(needLogin = true)
//    @ApiOperation("查询权限模板配置页面所需数据")
//    public ResultMessage listMenuBySite(@RequestParam("site") Integer site) {
//        List<MenuDisplayVO> menuDisplayVOList = this.menuService.listMenuBySite(site);
//        return ResultMessage(menuDisplayVOList);
//    }
//
//    @RequestMapping(value = {"/listFirstMenu"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
//    @FastMappingInfo(needLogin = true, code = 1704L)
//    public ResultMessage getFirstMenu(@RequestBody MenuConditionVO menuConditionVO) {
//        List<MenuVO> menuVOs = this.menuService.listMenus(menuConditionVO);
//        return ResultMessage(menuVOs);
//    }
//
//    @RequestMapping(value = {"/deleteFirstMenu"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
//    @FastMappingInfo(needLogin = true, code = 1702L)
//    public ResultMessage deleteFirstMenu(Integer id, String tmpId) {
//        ErrorCode errorCode = this.menuService.deleteFirstMenu(id, tmpId);
//        return ResultMessage(errorCode);
//    }
//
//    @RequestMapping(value = {"/updateFirstMenu"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
//    @FastMappingInfo(needLogin = true, code = 1703L)
//    public ResultMessage updateFirstMenu(@RequestBody ConfigMenuVO configMenuVO) {
//        ErrorCode errorCode = this.menuService.updateFirstMenu(configMenuVO);
//        return ResultMessage(errorCode);
//    }
//
//    @RequestMapping(value = {"/addFirstMenu"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
//    @FastMappingInfo(needLogin = true, code = 1701L)
//    public ResultMessage addFirstMenu(@RequestBody ConfigMenuVO configMenuVO) {
//        ErrorCode errorCode = this.menuService.addFirstMenu(configMenuVO);
//        return ResultMessage(errorCode);
//    }
//
//    @RequestMapping(value = {"/view"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
//    @FastMappingInfo(needLogin = true, code = 1705L)
//    public ResultMessage view(Integer id) {
//        MenuVO menuVO = this.menuService.view(id);
//        return ResultMessage(menuVO);
//    }
}
