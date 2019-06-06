package cn.com.xgit.parts.auth.module.account.entity;

import cn.com.xgit.parts.common.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户角色表 DO
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_account_role")
@ApiModel(value = "SysAccountRole", description = "账户角色关系")
public class SysAccountRole extends BaseEntity {
    private static final long serialVersionUID = -1L;
    //用户标识
    @ApiModelProperty(value = "用户ID", name = "user_id")
    private Long userId;
    //角色标识
    @ApiModelProperty(value = "角色ID", name = "role_id")
    private Long roleId;
}
