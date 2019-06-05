package cn.com.xgit.parts.auth.module.menu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * VO类
 */
@Data
@ToString
public class SysAuthsVO implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "pk")
    private Integer id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "父节点id")
    private Integer parentId;
    @ApiModelProperty(value = "顺序")
    private Integer seq;
    @ApiModelProperty(value = "图标")
    private String icon;
    @ApiModelProperty(value = "状态，0无效，1生效")
    private Integer showFlag;
    @ApiModelProperty(value = "对应的url，若是菜单则为空")
    private String url;
    @ApiModelProperty(value = "频道，0:PC 1:PAD")
    private Integer channel;
    @ApiModelProperty(value = "是否叶子节点，默认否")
    private Integer leaf;
    @ApiModelProperty(value = "类型，1,菜单 2按钮")
    private Integer type;
    @ApiModelProperty(value = "创建人")
    private String createId;
    @ApiModelProperty(value = "创建日期")
    private Date createDate;
    @ApiModelProperty(value = "更新人")
    private String updateId;
    @ApiModelProperty(value = "更新日期")
    private Date updateDate;

    @ApiModelProperty(value = "是否选择")
    private boolean checked;

    @ApiModelProperty(value = "子节点是否部分选中")
    private boolean indeterminate;

    @ApiModelProperty(value = "子节点")
    private List<SysAuthsVO> children;

    private String state;
}
