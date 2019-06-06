package cn.com.xgit.parts.auth.module.account.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserLoginVO {
    private String loginName;
    private String password;
    private String code;
    private String authId;
    //登录方式，默认0普通登录 （2手机动态密码，cn.com.xgit.parts.auth.enums.PasswordType）
    private Integer pswType;

}
