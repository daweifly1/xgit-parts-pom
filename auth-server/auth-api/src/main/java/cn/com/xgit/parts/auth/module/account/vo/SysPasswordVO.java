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
    @ApiModelProperty(value = "")
    private Integer id;
    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "用来区分密码安全,com.xgit.bj.auth.enums.PasswordType")
    private Integer type;
}
