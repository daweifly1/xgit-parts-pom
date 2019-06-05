package cn.com.xgit.parts.common.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 树结构实体
 * @author f00lish
 * @version 2018/1/21
 * Created By IntelliJ IDEA.
 * Qun:530350843
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TreeEntity extends CommEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 父级ID
     */
    @ApiModelProperty(value="父级ID",name="parentId", example = "1")
    private Long parentId;

    /**
     * 父级IDs
     */
    @ApiModelProperty(value="父级IDs",name="parentIds", example = "1/2")
    private Long parentIds;

}
