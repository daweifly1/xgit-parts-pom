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

}
