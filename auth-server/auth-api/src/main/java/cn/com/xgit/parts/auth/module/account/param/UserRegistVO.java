package cn.com.xgit.parts.auth.module.account.param;

import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@ApiModel(value = "注册信息")
public class UserRegistVO extends SysAccountVO implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码，登录（可以配置超过一定次数后需要），注册时候需要")
    private String code;
    @ApiModelProperty(value = "验证码对应的key，登录注册时候配合code使用")
    private String authId;

    @ApiModelProperty(value = "登录方式,仅登录时候使用，默认0普通登录 （2手机动态密码，cn.com.xgit.parts.auth.enums.PasswordType）")
    private Integer pswType;


}
