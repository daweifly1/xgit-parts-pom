package cn.com.xgit.parts.auth.module.account.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 用户角色表 VO类
 */
@Data
@ToString
public class SysUserRolesVO implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "用户标识")
    private String userId;
    @ApiModelProperty(value = "角色标识")
    private String roleId;
    @ApiModelProperty(value = "是否主角色（1主角色，0普通角色，由业务决定）")
    private Integer roleFlag;

    private List<String> userIdList;
}
