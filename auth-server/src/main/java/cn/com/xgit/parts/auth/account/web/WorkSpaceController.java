package cn.com.xgit.parts.auth.account.web;

import cn.com.xgit.parts.auth.account.service.WorkspaceService;
import cn.com.xgit.parts.auth.VO.WorkspaceVO;
import com.xgit.bj.core.rsp.ResultMessage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/workspace"})
public class WorkSpaceController extends BasicController {
    @Autowired
    WorkspaceService workspaceService;

    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ApiOperation("查询所有工作空间")
    public ResultMessage listSpace() {
        return ResultMessage(this.workspaceService.queryList());
    }

    @RequestMapping(value = {"/add"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("增加工作空间")
    public ResultMessage insert(@RequestBody WorkspaceVO workspaceVO)
            throws Exception {
        return ResultMessage(this.workspaceService.insert(workspaceVO));
    }

    @RequestMapping(value = {"/remove"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("批量删除工作空间")
    public ResultMessage remove(@RequestBody List<String> ids) {
        return ResultMessage(this.workspaceService.removeByIds(ids));
    }

    @RequestMapping(value = {"/update"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("修改工作空间信息")
    public ResultMessage update(@RequestBody WorkspaceVO workspaceVO) {
        return ResultMessage(this.workspaceService.update(workspaceVO));
    }

    @RequestMapping(value = {"/item"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ApiOperation("查看工作空间信息")
    public ResultMessage item(@RequestParam("id") String id) {
        return ResultMessage(this.workspaceService.item(id));
    }

    @RequestMapping(value = {"/updateTemplate"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("修改工作空间对应的权限模板ID")
    public ResultMessage updateTemplate(@RequestParam("workspaceId") String workspaceId, @RequestParam("templateId") String templateId) {
        return ResultMessage(this.workspaceService.updateTemplate(workspaceId, templateId));
    }

    @RequestMapping(value = {"/remove"}, method = {org.springframework.web.bind.annotation.RequestMethod.DELETE})
    @ApiOperation("删除企业工作空间")
    public ResultMessage remove(@RequestParam("workspaceId") String workspaceId) {
        return ResultMessage(this.workspaceService.remove(workspaceId));
    }

    @RequestMapping(value = {"/lock"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("冻结锁定企业工作空间")
    public ResultMessage lock(@RequestParam("workspaceId") String workspaceId) {
        return ResultMessage(this.workspaceService.lock(workspaceId));
    }

    @RequestMapping(value = {"/unlock"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("解锁企业工作空间")
    public ResultMessage unlock(@RequestParam("workspaceId") String workspaceId) {
        return ResultMessage(this.workspaceService.unlock(workspaceId));
    }

}
