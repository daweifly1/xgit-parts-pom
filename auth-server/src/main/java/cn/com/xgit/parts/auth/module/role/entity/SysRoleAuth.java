package cn.com.xgit.parts.auth.module.role.entity;

import cn.com.xgit.parts.common.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 角色权限关联关系表 实体类
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_auth")
@ApiModel(value = "SysRoleAuth", description = "角色权限关联关系表")
public class SysRoleAuth extends BaseEntity {
    private static final long serialVersionUID = -1L;

    //角色ID
    @ApiModelProperty(value = "角色ID", name = "role_id")
    private Long roleId;
    //权限ID
    @ApiModelProperty(value = "权限ID", name = "auth_id")
    private Long authId;

    @ApiModelProperty(value = "平台id,冗余", name = "platform_id")
    private Long platformId;
}
