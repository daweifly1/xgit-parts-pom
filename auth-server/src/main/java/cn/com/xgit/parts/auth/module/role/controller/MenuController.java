package cn.com.xgit.parts.auth.module.role.controller;

import cn.com.xgit.parts.auth.common.base.BasicController;
import cn.com.xgit.parts.auth.module.menu.param.SysAuthsParam;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.module.role.entity.SysAuths;
import cn.com.xgit.parts.auth.module.role.facade.MenuFacade;
import cn.com.xgit.parts.common.result.ResultMessage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping({"/menu"})
public class MenuController extends BasicController {

    @Autowired
    private MenuFacade menuFacade;

    @GetMapping("/tree")
    @ApiOperation("查询菜单集合（树形结构）")
    public ResultMessage<List<SysAuthsVO>> menuTreeList(SysAuths condition, HttpServletRequest request) {
        if (null == condition) {
            return ResultMessage.error("参数异常");
        }
        if (null == condition.getPlatformId()) {
            return ResultMessage.error("平台信息异常");
        }

        List<SysAuthsVO> menuDisplayVOList = menuFacade.menuTreeList(condition, getUserId());
        return ResultMessage(menuDisplayVOList);
    }

    @PostMapping("/getAuthIds")
    @ApiOperation("查询权限码集合--feign接口")
    public ResultMessage<List<Long>> getAuthIds(@RequestBody SysAuthsParam sysAuthsParam, HttpServletRequest request) {
        if (null == sysAuthsParam) {
            return ResultMessage.error("参数异常");
        }
        if (null == sysAuthsParam.getPlatformId()) {
            return ResultMessage.error("平台信息异常");
        }
        List<Long> authIds = menuFacade.getAuthIds(sysAuthsParam);
        return ResultMessage(authIds);
    }
}
