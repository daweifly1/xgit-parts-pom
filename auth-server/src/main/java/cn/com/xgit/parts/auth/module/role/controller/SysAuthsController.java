package cn.com.xgit.parts.auth.module.role.controller;

import cn.com.xgit.parts.auth.common.base.BasicController;
import cn.com.xgit.parts.auth.exception.code.ErrorCode;
import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.auth.module.role.entity.SysAuths;
import cn.com.xgit.parts.auth.module.role.service.SysAuthsService;
import cn.com.xgit.parts.common.result.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.util.List;

/**
 * SysAuths Controller 实现类
 */
@Slf4j
@RestController
@RequestMapping("/sysAuths")
@Api("系统权限资源管理操作Controller ")
public class SysAuthsController extends BasicController {
    @Autowired
    private SysAuthsService sysAuthsService;

    @GetMapping(value = "/treeList")
    @ApiOperation(value = "树形结构信息")
    public ResultMessage<List<SysAuthsVO>> treeList(SysAuths condition) {
        if (null == condition) {
            return ResultMessage.error("参数异常");
        }
        if (null == condition.getPlatformId()) {
            return ResultMessage.error("平台信息异常");
        }
        List<SysAuthsVO> list = sysAuthsService.treeList(condition, false);
        return ResultMessage(list);
    }


    @PutMapping("save")
    @ApiOperation(value = "保存")
    public ResultMessage<ErrorCode> save(@RequestBody SysAuths sysAuths, HttpServletRequest request) {
        if (null == sysAuths) {
            return ResultMessage(ErrorCode.IllegalArument);
        }
        if (null == sysAuths.getPlatformId()) {
            return ResultMessage.error("平台信息异常");
        }
        boolean r = false;
        if (null == sysAuths.getId()) {
            sysAuths.setCreatedBy(getUserId());
            r = sysAuthsService.save(sysAuths);
        } else {
            sysAuths.setUpdatedBy(getUserId());
            r = sysAuthsService.updateById(sysAuths);
        }
        if (!r) {
            return ResultMessage.error();
        }
        return ResultMessage.success();
    }


    @DeleteMapping("remove")
    @ApiOperation(value = "删除")
    public ResultMessage<ErrorCode> remove(Long id, HttpServletRequest request) {
        if (null == id) {
            return ResultMessage(ErrorCode.IllegalArument);
        }
        sysAuthsService.deleteById(id);
        return ResultMessage.success();
    }


    @PostMapping(value = "/exportTs")
    public void testDownload(@RequestBody SysAuths condition, HttpServletResponse response) {
        String fileName = "authCodes.ts";
//        response.setHeader("content-type", "application/octet-stream");
//        response.setContentType("application/octet-stream");
        List<SysAuthsVO> list = sysAuthsService.treeList(condition, false);
        StringBuilder sb = new StringBuilder();
        sb.append("export const ActionCode = {\n");
        collectAuthCodes(sb, list);
        sb.append("};");
        // 导出文件
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        BufferedOutputStream buff = null;
        StringBuffer write = new StringBuffer();
        String enter = "\r\n";
        ServletOutputStream outSTr = null;
        try {
            outSTr = response.getOutputStream(); // 建立
            buff = new BufferedOutputStream(outSTr);
            // 把内容写入文件
            write.append(sb);
            write.append(enter);
            buff.write(write.toString().getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            log.error("", e);
        } finally {
            try {
                buff.close();
                outSTr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void collectAuthCodes(StringBuilder sb, List<SysAuthsVO> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (SysAuthsVO vv : list) {
                //按钮权限
                if (StringUtils.isNoneBlank(vv.getUrl()) && 2 == vv.getType()) {
                    sb.append(genKey(vv.getUrl())).append("=").append(vv.getId()).append(";\n");
                }
                if (CollectionUtils.isNotEmpty(vv.getChildren())) {
                    collectAuthCodes(sb, vv.getChildren());
                }
            }
        }
    }

    private String genKey(String url) {
        if (url.contains("/")) {
            StringBuilder sb = new StringBuilder();
            String[] ss = url.split("/");
            boolean first = true;
            for (String s : ss) {
                if (StringUtils.isNoneBlank(s)) {
                    if (first) {
                        sb.append(s);
                        first = false;
                    } else {
                        sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
                    }
                }
            }
            return sb.toString();
        }
        return url;
    }


}
