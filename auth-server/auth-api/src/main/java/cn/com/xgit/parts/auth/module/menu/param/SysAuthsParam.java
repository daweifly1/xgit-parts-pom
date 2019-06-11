package cn.com.xgit.parts.auth.module.menu.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * param类
 */
@Data
@ToString
public class SysAuthsParam implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "Id", name = "id")
    private Long id;

    @ApiModelProperty(value = "平台id", name = "platform_id")
    private Long platformId;

    //父节点id
    @ApiModelProperty(value = "父节点id", name = "parent_id")
    private Long parentId;

    @ApiModelProperty(value = "当前用户id", name = "parent_id")
    private Long userId;

}
