package cn.com.xgit.parts.common.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

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
public class CommEntity extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    @TableField(value = "created_by",fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建者ID",name="createdBy", example = "1")
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间",name="createdTime")
    private Date createdTime;

    /**
     * 更新者
     */
    @TableField(value = "updated_by",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="更新者",name="updatedBy")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="更新时间",name="updatedTime")
    private Date updatedTime;

    /**
     * 删除标记
     */
    @TableLogic
    @Length(max = 1)
    @ApiModelProperty(value="删除标记",name="delFlag")
    private Integer delFlag;

}
