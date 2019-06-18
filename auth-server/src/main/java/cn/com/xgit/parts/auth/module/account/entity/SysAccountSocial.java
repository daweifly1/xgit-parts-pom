package cn.com.xgit.parts.auth.module.account.entity;

import cn.com.xgit.parts.common.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_account_social")
@ApiModel(value = "SysAccountSocial", description = "社交账号关系表")
public class SysAccountSocial extends BaseEntity {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "账号ID", name = "account_id")
    private Long accountId;

    @ApiModelProperty(value = "社交账号", name = "social_account")
    private String socialAccount;

    @ApiModelProperty(value = "社交账号类型", name = "type")
    private String type;
}
