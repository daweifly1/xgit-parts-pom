package cn.com.xgit.parts.auth.module.menu.vo;

import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息表 VO类
 */
@Data
@ToString
public class SysRoleVO implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "角色ID")
    private String id;
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ApiModelProperty(value = "9:系统默认，用户不能删除")
    private String remark;
    @ApiModelProperty(value = "1：系统管理员，2普通角色")
    private Long type;
    @ApiModelProperty(value = "渠道（0初始化，1平台录入）")
    private Long channel;
    @ApiModelProperty(value = "所属工作空间ID")
    private String spaceId;
    @ApiModelProperty(value = "所属部门ID（0表示角色与部门不关联）")
    private String deptId;
    @ApiModelProperty(value = "顺序")
    private Long seqNo;

    @ApiModelProperty(value = "拥有权限的树形结构")
    private List<SysAuthsVO> treeAuthList;

    private List<String> idList;
}
