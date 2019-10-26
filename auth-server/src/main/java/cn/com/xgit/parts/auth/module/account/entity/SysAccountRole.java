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

    //角色标识
    @ApiModelProperty(value = "平台数据ID，具体的角色作用对象（例如一个平台多个店铺，每个店铺权限不一样）", name = "dataId")
    private Long dataId;

    @ApiModelProperty(value = "平台id,冗余", name = "platform_id")
    private Long platformId;
}
