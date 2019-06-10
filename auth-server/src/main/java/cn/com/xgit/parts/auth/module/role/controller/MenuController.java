package cn.com.xgit.parts.auth.module.role.controller;

import cn.com.xgit.parts.auth.common.base.BasicController;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.module.role.entity.SysAuths;
import cn.com.xgit.parts.auth.module.role.facade.MenuFacade;
import cn.com.xgit.parts.common.result.ResultMessage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

        List<SysAuthsVO> menuDisplayVOList = menuFacade.menuTreeList(condition, getUserId(request));
        return ResultMessage(menuDisplayVOList);
    }

}
