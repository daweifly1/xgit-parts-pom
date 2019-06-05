package cn.com.xgit.parts.auth.account.service;

import cn.com.xgit.parts.auth.VO.TemplateConfigVO;
import cn.com.xgit.parts.auth.VO.TemplateVO;
import cn.com.xgit.parts.auth.VO.TmpAuthsVO;
import cn.com.xgit.parts.auth.VO.TmpMenusVO;
import cn.com.xgit.parts.auth.account.dao.entity.ProfileDO;
import cn.com.xgit.parts.auth.account.dao.entity.TemplateDO;
import cn.com.xgit.parts.auth.account.dao.entity.TmpAuthDO;
import cn.com.xgit.parts.auth.account.dao.entity.TmpMenuDO;
import cn.com.xgit.parts.auth.account.dao.entity.WorkspaceDO;
import cn.com.xgit.parts.auth.account.dao.mapper.ProfileMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.TemplateMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.TmpAuthMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.TmpMenuMapper;
import cn.com.xgit.parts.auth.account.dao.mapper.WorkspaceMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.core.Ref;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TemplateService {
    @Autowired
    TemplateMapper templateMapper;
    @Autowired
    TmpAuthMapper tmpAuthMapper;
    @Autowired
    TmpMenuMapper tmpMenuMapper;
    @Autowired
    ProfileMapper profileMapper;
    @Autowired
    WorkspaceMapper workspaceMapper;
    @Autowired
    MenuService menuService;
    @Autowired
    AuthService authService;

    public List<TemplateVO> queryList() {
        List<TemplateVO> templateVOs = new ArrayList();

        List<TemplateDO> templateDOs = this.templateMapper.queryList();
        for (TemplateDO templateDO : templateDOs) {
            if (!templateDO.getId().equals("1")) {
                TemplateVO templateVO = new TemplateVO();
                BeanUtils.copyProperties(templateDO, templateVO);
                templateVOs.add(templateVO);
            }
        }
        return templateVOs;
    }

    public ErrorCode insert(TemplateVO templateVO) {
        TemplateDO templateDO = new TemplateDO();

        BeanUtils.copyProperties(templateVO, templateDO);

        int ret = this.templateMapper.insert(templateDO);
        if (0 >= ret) {
            log.error("新增权限模板操作失败！", ErrorCode.FailedToInsertRecord.getCode());
            return ErrorCode.FailedToInsertRecord;
        }
        return ErrorCode.Success;
    }

    public TemplateVO selectById(String id) {
        TemplateVO templateVO = new TemplateVO();
        TemplateDO templateDO = this.templateMapper.selectById(id);
        if (null == templateDO) {
            log.debug(id + "权限模板信息不存在！");
            return null;
        }
        BeanUtils.copyProperties(templateDO, templateVO);

        return templateVO;
    }

    public ErrorCode update(TemplateVO templateVO) {
        TemplateDO templateDO = new TemplateDO();
        BeanUtils.copyProperties(templateVO, templateDO);

        int ret = this.templateMapper.updateById(templateDO);
        if (0 >= ret) {
            log.error("修改权限模板操作失败！", ErrorCode.FailedToUpdateRecord.getCode());
            return ErrorCode.FailedToUpdateRecord;
        }
        return ErrorCode.Success;
    }

    public ErrorCode removeByIds(List<String> ids, Ref<List> tmpRef) {
        List<String> templateNames = checkTempDeleteCondition(ids);
        if (!CollectionUtils.isEmpty(templateNames)) {
            tmpRef.set(templateNames);
            return ErrorCode.TemplateInUse;
        }
        for (String id : ids) {
            this.templateMapper.removeById(id);

            this.tmpAuthMapper.removeByTemplate(id);
            this.tmpMenuMapper.removeByTemplate(id);
        }
        return ErrorCode.Success;
    }

    private List<String> checkTempDeleteCondition(List<String> ids) {
        List<String> templateNames = new ArrayList();
        for (String id : ids) {
            int ret = this.workspaceMapper.queryCountByTempId(id);
            if (0 < ret) {
                log.debug(id + "权限模板没有被释放，无法删除！");
                templateNames.add(selectById(id).getName());
            }
        }
        return templateNames;
    }

    public ErrorCode configTmpAuth(TmpAuthsVO tmpAuthsVO) {
        List<Integer> auths = tmpAuthsVO.getAuthIds();
        for (Integer authId : auths) {
            TmpAuthDO tmpAuthDO = new TmpAuthDO();
            tmpAuthDO.setAuthId(authId);
            tmpAuthDO.setTmpId(tmpAuthsVO.getTemplateId());

            this.tmpAuthMapper.insert(tmpAuthDO);
        }
        return ErrorCode.Success;
    }

    public ErrorCode configTemplate(TemplateConfigVO templateConfigVO) {
        List<Integer> auths = templateConfigVO.getAuthIds();
        List<Integer> menuIds = templateConfigVO.getMenuIds();

        this.tmpAuthMapper.removeByTemplate(templateConfigVO.getTempId());
        if (!CollectionUtils.isEmpty(auths)) {
            for (Integer authId : auths) {
                TmpAuthDO tmpAuthDO = new TmpAuthDO();
                tmpAuthDO.setAuthId(authId);
                tmpAuthDO.setTmpId(templateConfigVO.getTempId());

                this.tmpAuthMapper.insert(tmpAuthDO);
            }
        }
        this.tmpMenuMapper.removeByTemplate(templateConfigVO.getTempId());
        if (!CollectionUtils.isEmpty(menuIds)) {
            for (Integer menuId : menuIds) {
                TmpMenuDO tmpMenuDO = new TmpMenuDO();
                tmpMenuDO.setMenuId(menuId);
                tmpMenuDO.setTmpId(templateConfigVO.getTempId());

                this.tmpMenuMapper.insert(tmpMenuDO);
            }
        }
        return ErrorCode.Success;
    }

    public List<Integer> queryAuthByTemplate(String tmpId) {
        return this.tmpAuthMapper.queryList(tmpId);
    }

    public ErrorCode removeAuthByTemplate(String tmpId) {
        this.tmpAuthMapper.removeByTemplate(tmpId);
        return ErrorCode.Success;
    }

    public ErrorCode removeAuths(TmpAuthsVO tmpAuthsVO) {
        List<Integer> auths = tmpAuthsVO.getAuthIds();
        for (Integer authId : auths) {
            TmpAuthDO tmpAuthDO = new TmpAuthDO();
            tmpAuthDO.setAuthId(authId);
            tmpAuthDO.setTmpId(tmpAuthsVO.getTemplateId());

            this.tmpAuthMapper.removeAuth(tmpAuthDO);
        }
        return ErrorCode.Success;
    }

    @Transactional
    public ErrorCode configTmpMenu(TmpMenusVO tmpMenusVO) {
        List<Integer> menuIds = tmpMenusVO.getMenuIds();
        if (StringUtils.isBlank(tmpMenusVO.getTemplateId())) {
            log.error("权限模板ID为空！", ErrorCode.TemplateIsNull.getCode());
            return ErrorCode.TemplateIsNull;
        }
        this.tmpMenuMapper.removeByTemplate(tmpMenusVO.getTemplateId());
        for (Integer menuId : menuIds) {
            TmpMenuDO tmpMenuDO = new TmpMenuDO();
            tmpMenuDO.setMenuId(menuId);
            tmpMenuDO.setTmpId(tmpMenusVO.getTemplateId());

            this.tmpMenuMapper.insert(tmpMenuDO);
        }
        return ErrorCode.Success;
    }

    public List<Integer> queryMenuByTemplate(String tmpId) {
        return this.tmpMenuMapper.queryList(tmpId);
    }

    public TemplateConfigVO queryTemplateConfigById(String tmpId) {
        TemplateConfigVO templateConfigVO = new TemplateConfigVO();
        List<Integer> menus = this.tmpMenuMapper.queryList(tmpId);
        List<Integer> auths = this.tmpAuthMapper.queryList(tmpId);

        templateConfigVO.setTempId(tmpId);
        templateConfigVO.setAuthIds(auths);
        templateConfigVO.setMenuIds(menus);
        return templateConfigVO;
    }

    public ErrorCode removeMenuByTemplate(String tmpId) {
        this.tmpMenuMapper.removeByTemplate(tmpId);
        return ErrorCode.Success;
    }

    public ErrorCode removeMenus(TmpMenusVO tmpMenusVO) {
        List<Integer> Menus = tmpMenusVO.getMenuIds();
        for (Integer menu : Menus) {
            TmpMenuDO tmpMenuDO = new TmpMenuDO();
            tmpMenuDO.setMenuId(menu);
            tmpMenuDO.setTmpId(tmpMenusVO.getTemplateId());

            this.tmpMenuMapper.removeMenu(tmpMenuDO);
        }
        return ErrorCode.Success;
    }

    public TemplateVO queryTempByUserId(String userId) {
        ProfileDO profileDO = this.profileMapper.selectById(userId);
        if (null == profileDO) {
            log.debug(userId + "用户信息不存在！");
            return null;
        }
        WorkspaceDO workspaceDO = this.workspaceMapper.selectById(profileDO.getSpaceId());
        if (null == workspaceDO) {
            log.debug(profileDO.getSpaceId() + "工作空间信息不存在！");
            return null;
        }
        return selectById(workspaceDO.getTempId());
    }

    public List<TemplateVO> queryTemplateBySite(Integer site) {
        List<TemplateDO> templateDOs = this.templateMapper.queryTemplateBySite(site);
        List<TemplateVO> templateVOs = new ArrayList();
        for (TemplateDO templateDO : templateDOs) {
            if (!templateDO.getId().equals("1")) {
                TemplateVO templateVO = new TemplateVO();
                BeanUtils.copyProperties(templateDO, templateVO);
                templateVOs.add(templateVO);
            }
        }
        return templateVOs;
    }

    public int insertTmpMenu(TmpMenuDO tmpMenuDO) {
        return this.tmpMenuMapper.insert(tmpMenuDO);
    }

    public int delTmpMenu(TmpMenuDO tmpMenuDO) {
        return this.tmpMenuMapper.removeMenu(tmpMenuDO);
    }
}
