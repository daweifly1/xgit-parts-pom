package cn.com.xgit.parts.auth.module.account.entity;

import cn.com.xgit.parts.common.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 密码信息表 DO
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_password")
@ApiModel(value = "SysPassword", description = "密码信息表")
public class SysPassword extends BaseEntity {
    private static final long serialVersionUID = -1L;
    //用户ID
    @ApiModelProperty(value = "用户ID", name = "user_id")
    private Long userId;
    //密码
    @ApiModelProperty(value = "密码", name = "password")
    private String password;
    //用来区分密码安全,1.普通登录密码
    @ApiModelProperty(value = "用来区分密码安全,1.普通登录密码", name = "type")
    private Integer type;

    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间", name = "updatedTime")
    private Date updatedTime;
}

