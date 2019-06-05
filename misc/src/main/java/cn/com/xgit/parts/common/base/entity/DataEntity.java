package cn.com.xgit.parts.common.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author f00lish
 * @version 2018/1/21
 * Created By IntelliJ IDEA.
 * Qun:530350843
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataEntity extends CommEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @ApiModelProperty(value="租户ID",name="tenantId", example = "1")
    private Long tenantId;

}
