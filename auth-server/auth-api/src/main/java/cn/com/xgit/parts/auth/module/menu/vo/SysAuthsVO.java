package cn.com.xgit.parts.auth.module.menu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * VO类
 */
@Data
@ToString
public class SysAuthsVO implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "Id", name = "id")
    private Long id;

    @ApiModelProperty(value = "平台id", name = "platform_id")
    private Long platformId;
    //名称
    @ApiModelProperty(value = "名称", name = "name")
    private String name;
    //编码
    @ApiModelProperty(value = "编码，唯一索引", name = "code")
    private String code;
    //父节点id
    @ApiModelProperty(value = "父节点id", name = "parent_id")
    private Long parentId;
    //顺序
    @ApiModelProperty(value = "顺序", name = "seq")
    private Long seq;
    //图标
    @ApiModelProperty(value = "图标", name = "icon")
    private String icon;
    //状态，0无效，1生效
    @ApiModelProperty(value = "状态，0无效，1生效", name = "show_flag")
    private Integer showFlag;
    //对应的url，若是菜单则为空,也可以是前端权限按钮对应的资源
    @ApiModelProperty(value = "对应的url，若是菜单则为空,也可以是前端权限按钮对应的资源", name = "url")
    private String url;
    //类型，1,菜单 2按钮
    @ApiModelProperty(value = "类型，1,菜单 2按钮", name = "type")
    private Integer type;

    @ApiModelProperty(value = "子节点")
    private List<SysAuthsVO> children;
}
