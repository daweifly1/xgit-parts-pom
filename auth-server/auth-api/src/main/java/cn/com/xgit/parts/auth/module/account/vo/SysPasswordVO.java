package cn.com.xgit.parts.auth.module.account.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 密码信息表 VO类
 */
@Data
@ToString
public class SysPasswordVO implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "登录名称")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "登录方式,仅登录时候使用，默认0普通登录 （2手机动态密码，cn.com.xgit.parts.auth.enums.PasswordType）")
    private Integer type;

    @ApiModelProperty(value = "原来密码，若原密码忘记有绑定手机号的可以通过手机号码（或考虑email）可以通过动态码来修改密码")
    private String oldPassword;

}
