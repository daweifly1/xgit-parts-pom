package cn.com.xgit.parts.auth.module.role.entity;

import cn.com.xgit.parts.auth.module.menu.vo.SysAuthsVO;
import cn.com.xgit.parts.common.base.entity.CommEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@ApiModel(value = "SysRole", description = "角色信息表")
public class SysRole extends CommEntity {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "平台id", name = "platform_id")
    private Long platformId;

    @ApiModelProperty(value = "添加单位,例如某个店铺的主店，默认0", name = "org_id")
    private Long orgId;

    //角色名称
    @ApiModelProperty(value = "角色名称", name = "name")
    private String name;

    //1：系统管理员，2普通角色
    @ApiModelProperty(value = "1：系统分配（不允许删除，也不允许前台修改），0普通角色", name = "type")
    private Integer type;

    @ApiModelProperty(value = "备注信息", name = "remark")
    private String remark;

    @ApiModelProperty(value = "角色所拥有的权限信息")
    @TableField(exist = false)
    private List<SysAuthsVO> treeAuthList;
}
