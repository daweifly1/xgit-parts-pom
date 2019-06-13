package cn.com.xgit.parts.auth.module.role.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 根据角色ID集合、平台id查询参数
 */
@Data
@ToString
public class AuthRolePlatformParam implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "平台id", required = false)
    private Long platformId;
    @ApiModelProperty(value = "角色id集合")
    private List<Long> roleIdList;

}
