package cn.com.xgit.parts.auth.account.web.sys;

import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.service.sys.SysRoleService;
import cn.com.xgit.parts.auth.module.base.BasicController;
import com.github.pagehelper.PageInfo;
import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import com.xgit.bj.core.rsp.ResultMessage;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * SysRole Controller 实现类
 */
@Slf4j
@RestController
@RequestMapping("/sysRole")
public class SysRoleController extends BasicController {
    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "角色信息表分页列表信息")
    public ResultMessage<PageInfo<SysRoleVO>> list(@RequestBody SearchCommonVO<SysRoleVO> condition) {
        PageCommonVO<SysRoleVO> pageCommonVO = sysRoleService.list(condition);
        return ResultMessage(pageCommonVO.getPageInfo());
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "角色信息表-保存")
    public ResultMessage<ErrorCode> save(@RequestBody SysRoleVO sysRoleVO, HttpServletRequest req) {
        if (null == sysRoleVO) {
            return ResultMessage(ErrorCode.IllegalArument);
        }
        try {
            ErrorCode code = sysRoleService.save(sysRoleVO, getUserId(req));
            return ResultMessage(code);
        } catch (Exception e) {
            return new ResultMessage(ErrorCode.Failure.getCode(), e.getMessage());
        }
    }


    @RequestMapping(value = "/item", method = RequestMethod.GET)
    @ApiOperation(value = "根据id查询角色信息表详情")
    public ResultMessage<SysRoleVO> item(String id) {
        SysRoleVO sysRoleVO = sysRoleService.queryById(id);
        return ResultMessage(sysRoleVO);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "根据id查询角色信息表详情")
    public ResultMessage<ErrorCode> delete(@RequestBody String[] ids) {
        try {
            int r = sysRoleService.batchDelete(new ArrayList<>(Arrays.asList(ids)));
            if (r > 0) {
                return ResultMessage(ErrorCode.Success);
            } else {
                return ResultMessage(ErrorCode.Failure);
            }
        } catch (Exception e) {
            return new ResultMessage<>(ErrorCode.Failure.getCode(), e.getMessage());
        }
    }
}
