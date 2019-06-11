package cn.com.xgit.parts.auth.module.role.controller;

import cn.com.xgit.parts.auth.exception.code.ErrorCode;
import cn.com.xgit.parts.auth.common.base.BasicController;
import cn.com.xgit.parts.auth.module.role.entity.SysRole;
import cn.com.xgit.parts.auth.module.role.service.SysRoleService;
import cn.com.xgit.parts.common.result.ResultMessage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/list")
    @ApiOperation("角色信息表分页列表信息")
    public ResultMessage<IPage<SysRole>> list(SysRole condition) {
        return ResultMessage.success(sysRoleService.page(getPagination(), new QueryWrapper<>(condition)));
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "角色信息表-保存")
    public ResultMessage<ErrorCode> save(@RequestBody SysRole sysRole, HttpServletRequest req) {
        if (null == sysRole) {
            return ResultMessage(ErrorCode.IllegalArument);
        }
        Long uid = getUserId();
        if (null == sysRole.getId()) {
            sysRole.setCreatedBy(uid);
        } else {
            sysRole.setUpdatedBy(uid);
        }
        boolean r = sysRoleService.save(sysRole);
        if (r) {
            return ResultMessage.success();
        }
        return ResultMessage.error();
    }


    @RequestMapping(value = "/item", method = RequestMethod.GET)
    @ApiOperation(value = "根据id查询角色信息表详情")
    public ResultMessage<SysRole> item(Long id) {
        SysRole sysRole = sysRoleService.selectById(id);
        return ResultMessage(sysRole);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "根据id查询角色信息表详情")
    public ResultMessage<String> delete(@RequestBody String[] ids) {
        return sysRoleService.deleteByIds(new ArrayList(Arrays.asList(ids)));
    }
}
