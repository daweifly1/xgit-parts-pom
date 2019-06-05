package cn.com.xgit.parts.auth.module.menu.vo;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 角色权限关联关系表 VO类
 */
@Data
@ToString
public class SysRoleAuthVO  implements Serializable{
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "")
    private Integer id;
    @ApiModelProperty(value = "角色ID")
    private String roleId;
    @ApiModelProperty(value = "权限ID")
    private Integer authId;
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "角色ID集合")
    private List<String> roleIdList;
}
