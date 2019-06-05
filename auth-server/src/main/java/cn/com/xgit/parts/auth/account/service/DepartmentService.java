package cn.com.xgit.parts.auth.account.service;

import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.account.manager.UserContextManager;
import cn.com.xgit.parts.auth.VO.DepartmentVO;
import cn.com.xgit.parts.auth.account.dao.entity.DepartmentDO;
import cn.com.xgit.parts.auth.account.dao.mapper.DepartmentMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.ProfileMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.RoleMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    ProfileService profileService;
    @Autowired
    ProfileMapper profileMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserContextManager userContextManager;

    public List<DepartmentVO> listDept(String deptId, String userId) {
        String spaceId = this.userContextManager.getWorkspaceId(userId);
        if (StringUtils.isBlank(deptId)) {
            deptId = "0";
        }
        List<DepartmentVO> result = new ArrayList();
        List<DepartmentDO> list = this.departmentMapper.queryList(deptId, spaceId);
        for (DepartmentDO departmentDO : list) {
            DepartmentVO departmentVO = new DepartmentVO();

            BeanUtils.copyProperties(departmentDO, departmentVO);

            result.add(departmentVO);
        }
        return result;
    }

    public DepartmentVO item(String id) {
        DepartmentDO departmentDO = this.departmentMapper.selectById(id);
        if (departmentDO == null) {
            log.debug("部门信息返回为空！");
            return null;
        }
        DepartmentVO departmentVO = new DepartmentVO();
        BeanUtils.copyProperties(departmentDO, departmentVO);
        return departmentVO;
    }

    public ErrorCode insert(DepartmentVO departmentVO, String userId) {
        DepartmentDO departmentDO = BeanUtil.do2bo(departmentVO, DepartmentDO.class);
        if (StringUtils.isBlank(departmentDO.getParentId())) {
            departmentDO.setParentId("0");
        }
        departmentDO.setId(UUID.randomUUID().toString());
        departmentDO.setCode(departmentDO.getId());
        int exist = this.departmentMapper.checkName(departmentDO);
        if (0 < exist) {
            log.error("同级下部门名称“" + departmentDO.getName() + "”已存在！", ErrorCode.DeptNameExist.getCode());
            return ErrorCode.DeptNameExist;
        }
        this.departmentMapper.insert(departmentDO);
        departmentVO.setId(departmentDO.getId());
        departmentVO.setCode(departmentDO.getCode());
        return ErrorCode.Success;
    }

    private ErrorCode afterInsert(DepartmentDO departmentDO) {
        if ("0".equals(departmentDO.getParentId())) {
            log.debug("新增部门为一级部门！");
            return ErrorCode.Success;
        }
        DepartmentDO parentDO = this.departmentMapper.selectById(departmentDO.getParentId());
        if (parentDO.getLeaf().intValue() == 1) {
            DepartmentDO updateDO = new DepartmentDO();
            updateDO.setId(departmentDO.getParentId());
            updateDO.setLeaf(Integer.valueOf(0));
            int retUpdate = this.departmentMapper.update(updateDO);
            if (0 >= retUpdate) {
                log.error("新增部门成功，修改上级部门状态为父节点失败！", ErrorCode.FailedToUpdateRecord.getCode());
                return ErrorCode.FailedToUpdateRecord;
            }
        }
        return ErrorCode.Success;
    }

    public ErrorCode update(DepartmentVO departmentVO) {
        DepartmentDO beforeDO = this.departmentMapper.selectById(departmentVO.getId());
        if (null == beforeDO) {
            log.error("目标数据不存在！", ErrorCode.FailedToUpdateRecord.getCode());
            return ErrorCode.FailedToUpdateRecord;
        }
        if (beforeDO.getName().equals(departmentVO.getName())) {
            log.debug(departmentVO.getName() + "与原名称相同！");
            return ErrorCode.Success;
        }
        beforeDO.setName(departmentVO.getName());
        beforeDO.setSeq(departmentVO.getSeq());

        int exist = this.departmentMapper.checkName(beforeDO);
        if (0 < exist) {
            log.error("同级下部门名称“" + beforeDO.getName() + "”已存在！", ErrorCode.DeptNameExist.getCode());
            return ErrorCode.DeptNameExist;
        }
        this.departmentMapper.update(beforeDO);
        return ErrorCode.Success;
    }

    public ErrorCode removeByCode(String id, String userId) {
        //有子节点的，提示先删除子节点
        List<DepartmentDO> list = departmentMapper.queryListByParent(id);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new AuthException("请先删除子部门");
        }
        DepartmentDO departmentDO = this.departmentMapper.selectById(id);
        if (null == departmentDO) {
            log.debug("目标数据不存在！");
            return ErrorCode.Success;
        }

        this.departmentMapper.deleteById(id);
        return ErrorCode.Success;
//        return afterRemove(departmentDO);
    }

    private boolean checkRolesReferenced(String id, String spaceId) {
        int count = this.roleMapper.selectRolesReferencedCount(id, spaceId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    private ErrorCode afterRemove(DepartmentDO departmentDO) {
        if ("0".equals(departmentDO.getParentId())) {
            log.debug("被删除部门为一级部门！");
            return ErrorCode.Success;
        }
        List<DepartmentDO> departmentDOs = this.departmentMapper.queryList(departmentDO.getParentId(), null);
        if (CollectionUtils.isNotEmpty(departmentDOs)) {
            DepartmentDO parent = new DepartmentDO();
            parent.setId(departmentDO.getParentId());
            parent.setLeaf(Integer.valueOf(1));
            this.departmentMapper.update(parent);
        }
        return ErrorCode.Success;
    }

    public List<String> beforeRemove(String code, String spaceId) {
        DepartmentDO record = new DepartmentDO();
        record.setCode(code);
        record.setSpaceId(spaceId);
        List<String> names = new ArrayList();

        List<DepartmentDO> deptDOs = this.departmentMapper.selectDeptNotEmpty(record);
        if (CollectionUtils.isEmpty(deptDOs)) {
            return new ArrayList();
        }
        for (DepartmentDO departmentDO : deptDOs) {
            if (null != departmentDO) {
                names.add(departmentDO.getName());
            }
        }
        return names;
    }

    public List<DepartmentVO> queryDeptList(DepartmentVO departmentVO) {
        DepartmentDO departmentDO = new DepartmentDO();
        List<DepartmentVO> result = new ArrayList();

        BeanUtils.copyProperties(departmentVO, departmentDO);

        List<DepartmentDO> list = this.departmentMapper.queryDeptList(departmentDO);
        for (DepartmentDO tempDO : list) {
            DepartmentVO tempVO = new DepartmentVO();

            BeanUtils.copyProperties(tempDO, tempVO);

            result.add(tempVO);
        }
        return result;
    }

    public String queryFullDeptName(String deptId) {
        if ("0".equals(deptId)) {
            return "";
        }
        StringBuffer deptName = new StringBuffer();
        for (; ; ) {
            DepartmentVO departmentVO = item(deptId);
            if (null == departmentVO) {
                return "";
            }
            deptName.insert(0, departmentVO.getName());
            if (("0".equals(departmentVO.getParentId())) || ("".equals(departmentVO.getParentId()))) {
                break;
            }
            deptName.insert(0, "-");
            deptId = departmentVO.getParentId();
        }
        return deptName.toString();
    }

    public String createDeptCode(String parentId, String spaceId) {
        DepartmentDO departmentDO = this.departmentMapper.selectById(parentId);

        String lastCode = this.departmentMapper.queryLastCode(parentId, spaceId);
        if (StringUtils.isBlank(lastCode)) {
            if ("0".equals(parentId)) {
                return "001";
            }
            return departmentDO.getCode() + "001";
        }
        int codeLength = lastCode.length();

        String begin = lastCode.substring(0, codeLength - 3);
        String end = lastCode.substring(codeLength - 3);
        if ("999".equals(end)) {
            return "";
        }
        String newCode = String.valueOf(Integer.valueOf(end).intValue() + 1);

        int mark = end.length() - newCode.length();
        if (mark > 0) {
            for (; mark > 0; mark--) {
                newCode = "0" + newCode;
            }
        }
        return begin + newCode;
    }

    public Map<String, DepartmentDO> queryUserDeptMapByIds(Set<String> deptSet) {
        Map<String, DepartmentDO> result = new HashMap<>(deptSet.size());
        //不考虑缓存数据
        List<DepartmentDO> ll = departmentMapper.queryUserDeptMapByIds(new ArrayList<String>(deptSet));
        if (CollectionUtils.isNotEmpty(ll)) {
            for (DepartmentDO departmentDO : ll) {
                result.put(departmentDO.getId(), departmentDO);
            }
        }
        return result;
    }
}
