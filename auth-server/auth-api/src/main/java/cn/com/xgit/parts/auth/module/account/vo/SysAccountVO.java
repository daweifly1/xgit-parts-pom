package cn.com.xgit.parts.auth.module.account.vo;

import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import cn.com.xgit.parts.common.base.entity.CommEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 账号信息表 VO类
 */
@Data
@ToString
public class SysAccountVO extends CommEntity implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "登录名")
    private String username;
    @ApiModelProperty(value = "最近登录时间")
    private Date lastLoginTime;
    @ApiModelProperty(value = "最近登录IP")
    private String lastLoginIp;
    @ApiModelProperty(value = "0正常，1锁住")
    private Integer locked;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "联系电话")
    private String telephone;
    @ApiModelProperty(value = "性别（0女，1男）")
    private Integer sex;
    @ApiModelProperty(value = "头像")
    private String icon;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
    @ApiModelProperty(value = "地区编码")
    private String areaCode;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "状态（0有效，1无效）")
    private Integer status;
    //============非本表数据================================================
    @ApiModelProperty(value = "拥有角色信息")
    private List<SysRoleVO> roles;

    @ApiModelProperty(value = "拥有角色ID信息")
    private List<Long> roleIds;

    @ApiModelProperty(value = "数据范围id，例如分店id", name = "dataId")
    private Long dataId;

}
