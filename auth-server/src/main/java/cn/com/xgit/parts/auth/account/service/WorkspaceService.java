package cn.com.xgit.parts.auth.account.service;

import cn.com.xgit.parts.auth.VO.ProfileVO;
import cn.com.xgit.parts.auth.VO.TemplateVO;
import cn.com.xgit.parts.auth.VO.WorkspaceVO;
import cn.com.xgit.parts.auth.account.dao.entity.DepartmentDO;
import cn.com.xgit.parts.auth.account.dao.entity.WorkspaceDO;
import cn.com.xgit.parts.auth.account.dao.mapper.DepartmentMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.WorkspaceMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.core.Ref;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WorkspaceService {
    @Autowired
    WorkspaceMapper workspaceMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private RoleService roleService;
    @Value("${role.admin.roleId}")
    private String roleAdminId;

    public List<WorkspaceVO> queryList() {
        List<WorkspaceVO> workspaceVOs = new ArrayList();

        List<WorkspaceDO> workspaceDOs = this.workspaceMapper.queryList();
        for (WorkspaceDO workspaceDO : workspaceDOs) {
            WorkspaceVO workspaceVO = new WorkspaceVO();
            BeanUtils.copyProperties(workspaceDO, workspaceVO);
            workspaceVOs.add(workspaceVO);
        }
        return workspaceVOs;
    }

    public WorkspaceVO item(String id) {
        WorkspaceVO workspaceVO = new WorkspaceVO();

        WorkspaceDO workspaceDO = this.workspaceMapper.selectById(id);

        BeanUtils.copyProperties(workspaceDO, workspaceVO);

        return workspaceVO;
    }

    public ErrorCode insert(WorkspaceVO workspaceVO)
            throws Exception {
        WorkspaceDO workspaceDO = new WorkspaceDO();

        BeanUtils.copyProperties(workspaceVO, workspaceDO);

        int ret = this.workspaceMapper.insert(workspaceDO);
        if (0 >= ret) {
            log.error("添加工作空间步骤错误！", ErrorCode.FailedToInsertRecord.getCode());
            return ErrorCode.FailedToInsertRecord;
        }
        return addDefaultUser(workspaceVO);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ErrorCode removeByIds(List<String> ids) {
        List<String> result = checkWorkspace(ids);
        if (CollectionUtils.isEmpty(result)) {
            result = new ArrayList();
            for (String id : ids) {
                int ret = this.workspaceMapper.removeById(id);
                if (0 >= ret) {
                    result.add(id);
                }
            }
        }
        if (CollectionUtils.isEmpty(result)) {
            return ErrorCode.Success;
        }
        log.debug("工作空间删除失败！");
        return ErrorCode.FailedToRemoveRecord;
    }

    public ErrorCode update(WorkspaceVO workspaceVO) {
        WorkspaceDO workspaceDO = new WorkspaceDO();

        BeanUtils.copyProperties(workspaceVO, workspaceDO);

        int ret = this.workspaceMapper.updateById(workspaceDO);
        if (0 >= ret) {
            return ErrorCode.FailedToUpdateRecord;
        }
        return ErrorCode.Success;
    }

    private List<String> checkWorkspace(List<String> ids) {
        List<String> result = new ArrayList();
        for (String id : ids) {
            DepartmentDO departmentDO = new DepartmentDO();
            departmentDO.setSpaceId(id);
            List<DepartmentDO> departmentDOs = this.departmentMapper.queryDeptList(departmentDO);
            if (!CollectionUtils.isEmpty(departmentDOs)) {
                result.add(id);
            }
        }
        return result;
    }

    public List<Integer> queryAuthsByWorkspace(String workspaceId) {
        WorkspaceDO workspaceDO = this.workspaceMapper.selectById(workspaceId);
        if (null == workspaceDO) {
            return new ArrayList();
        }
        TemplateVO templateVO = this.templateService.selectById(workspaceDO.getTempId());
        if (null == templateVO) {
            return new ArrayList();
        }
        List<Integer> auths = this.templateService.queryAuthByTemplate(templateVO.getId());

        return auths;
    }

    private ErrorCode addDefaultUser(WorkspaceVO workspaceVO)
            throws Exception {
        ProfileVO profileVO = new ProfileVO();
        List<String> roleIds = new ArrayList();

        roleIds.add(this.roleAdminId);

        profileVO.setName("管理员");
        if (StringUtils.isBlank(workspaceVO.getAccount())) {
            profileVO.setLoginName(workspaceVO.getId());
        } else {
            profileVO.setLoginName(workspaceVO.getAccount());
        }
        profileVO.setRoleIds(roleIds);
        profileVO.setPassword(workspaceVO.getPassword());
        profileVO.setSpaceId(workspaceVO.getId());
        profileVO.setDeptId("0");
        ErrorCode addUser = this.profileService.insert(profileVO, new Ref(""));
        if (addUser.getCode() != 0) {
            return addUser;
        }
        return ErrorCode.Success;
    }

    public ErrorCode updateTemplate(String workspaceId, String templateId) {
        WorkspaceVO workspaceVO = new WorkspaceVO();

        workspaceVO.setId(workspaceId);
        workspaceVO.setTempId(templateId);

        ErrorCode updateCode = update(workspaceVO);
        if (updateCode != ErrorCode.Success) {
            log.error("修改工作空间的权限模板操作失败！", updateCode.getCode());
            return updateCode;
        }
        return this.roleService.releaseByWorkspace(workspaceId);
    }

    public ErrorCode remove(String workspaceId) {
        this.workspaceMapper.removeById(workspaceId);
        return ErrorCode.Success;
    }

    public ErrorCode lock(String workspaceId) {
        WorkspaceDO workspaceDO = new WorkspaceDO();
        workspaceDO.setId(workspaceId);
        workspaceDO.setStatus("1");
        this.workspaceMapper.updateById(workspaceDO);
        return ErrorCode.Success;
    }

    public ErrorCode unlock(String workspaceId) {
        WorkspaceDO workspaceDO = new WorkspaceDO();
        workspaceDO.setId(workspaceId);
        workspaceDO.setStatus("0");
        this.workspaceMapper.updateById(workspaceDO);
        return ErrorCode.Success;
    }
}
