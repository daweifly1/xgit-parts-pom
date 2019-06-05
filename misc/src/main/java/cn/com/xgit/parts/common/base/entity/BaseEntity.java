package cn.com.xgit.parts.common.base.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author f00lish
 * @version 2018/1/21
 * Created By IntelliJ IDEA.
 * Qun:530350843
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value="Id",name="id")
    private Long id;


}
