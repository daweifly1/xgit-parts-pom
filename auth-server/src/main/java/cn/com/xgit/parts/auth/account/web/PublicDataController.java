package cn.com.xgit.parts.auth.account.web;

import cn.com.xgit.parts.auth.account.service.DepartmentService;
import cn.com.xgit.parts.auth.account.service.ProfileService;
import cn.com.xgit.parts.auth.VO.DepartmentVO;
import com.xgit.bj.core.rsp.ResultMessage;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/publicData"})
public class PublicDataController extends BasicController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private DepartmentService departmentService;
    private String manageSpaceId = "1";

    @RequestMapping(value = {"/departmentList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage departmentList(@RequestParam("parentId") String parentId, @RequestParam("name") String name) {
        DepartmentVO departmentVO = new DepartmentVO();
        departmentVO.setParentId(parentId);
        departmentVO.setName(name);
        departmentVO.setSpaceId(this.manageSpaceId);
        return ResultMessage(this.departmentService.queryDeptList(departmentVO));
    }

    @RequestMapping(value = {"/departmentDetail"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResultMessage departmentDetail(@RequestParam("id") String id) {
        return ResultMessage(this.departmentService.item(id));
    }

    @RequestMapping(value = {"/userDetail"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ApiOperation("查询用户列表详细信息")
    public ResultMessage userDetail(@RequestParam("userId") String userId)
            throws Exception {
        return ResultMessage(this.profileService.item(userId));
    }
}
