package cn.com.xgit.parts.auth.account.web.sys;

import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.service.sys.SysAuthsService;
import cn.com.xgit.parts.auth.account.web.BasicController;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import com.xgit.bj.core.rsp.ResultMessage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * SysAuths Controller 实现类
 */
@Slf4j
@RestController
@RequestMapping("/sysAuths")
public class SysAuthsController extends BasicController {
    @Autowired
    private SysAuthsService sysAuthsService;

    @RequestMapping(value = "/treeList", method = RequestMethod.POST)
    @ApiOperation(value = "树形结构信息")
    public ResultMessage<List<SysAuthsVO>> treeList(@RequestBody SysAuthsVO condition) {
        List<SysAuthsVO> list = sysAuthsService.queryVOList(condition);
        return ResultMessage(list);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存")
    public ResultMessage<ErrorCode> save(@RequestBody SysAuthsVO sysAuthsVO, HttpServletRequest request) {
        if (null == sysAuthsVO) {
            return ResultMessage(ErrorCode.IllegalArument);
        }
        try {
            ErrorCode code = sysAuthsService.save(sysAuthsVO, getUserId(request));


            return ResultMessage(code);
        } catch (Exception e) {
            return new ResultMessage(ErrorCode.Failure.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ApiOperation(value = "删除")
    public ResultMessage<ErrorCode> remove(@RequestBody SysAuthsVO condition, HttpServletRequest request) {
        if (null == condition) {
            return ResultMessage(ErrorCode.IllegalArument);
        }
        try {
            ErrorCode code = sysAuthsService.remove(condition, getUserId(request));
            return ResultMessage(code);
        } catch (Exception e) {
            return new ResultMessage(ErrorCode.Failure.getCode(), e.getMessage());
        }
    }


}
