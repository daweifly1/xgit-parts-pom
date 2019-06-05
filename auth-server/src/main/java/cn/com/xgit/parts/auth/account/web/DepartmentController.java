package cn.com.xgit.parts.auth.account.web;

import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.service.DepartmentService;
import cn.com.xgit.parts.common.result.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/department"})
@RestController
public class DepartmentController extends BasicController {
    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage list(@RequestParam("id") String id, @RequestHeader("x-user-id") String userId) {
        return ResultMessage(this.departmentService.listDept(id, userId));
    }

    @RequestMapping(value = {"/item"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage item(@RequestParam("id") String id) {
        return ResultMessage(this.departmentService.item(id));
    }

    @RequestMapping(value = {"/insert"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResultMessage insert(@RequestBody DepartmentVO departmentVO, @RequestHeader("x-user-id") String userId) {
        ErrorCode ret = this.departmentService.insert(departmentVO, userId);
        return ResultMessage(ret);
    }

    @RequestMapping(value = {"/update"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResultMessage update(@RequestBody DepartmentVO departmentVO) {
        return ResultMessage(this.departmentService.update(departmentVO));
    }

    @RequestMapping(value = {"/remove"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResultMessage delete(@RequestBody DepartmentVO departmentVO, @RequestHeader("x-user-id") String userId) {
        return ResultMessage(this.departmentService.removeByCode(departmentVO.getId(), userId));
    }
}
