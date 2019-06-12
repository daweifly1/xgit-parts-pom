package cn.com.xgit.parts.auth.module.menu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 角色信息表 VO类
 */
@Data
@ToString
public class SysRoleVO implements Serializable {

    @ApiModelProperty(value = "Id", name = "id")
    private Long id;

    @ApiModelProperty(value = "平台id", name = "platform_id")
    private Long platformId;

    //角色名称
    @ApiModelProperty(value = "角色名称", name = "name")
    private String name;

    //1：系统管理员，2普通角色
    @ApiModelProperty(value = "1：系统分配（不允许删除，也不允许前台修改），0普通角色", name = "type")
    private Integer type;

    @ApiModelProperty(value = "备注信息", name = "remark")
    private String remark;
}
