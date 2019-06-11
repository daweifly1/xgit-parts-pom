package cn.com.xgit.parts.auth.module.role.entity;

import cn.com.xgit.parts.common.base.entity.CommEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_auths")
@ApiModel(value = "SysAuths", description = "权限资源信息")
public class SysAuths extends CommEntity {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "平台id", name = "platform_id")
    private Long platformId;
    //名称
    @ApiModelProperty(value = "名称", name = "name")
    private String name;
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
}
