package cn.com.xgit.parts.auth.module.account.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SysUserLoginInfoVO {
    //用户id
    private Long id;
    //登录名
    private String loginName;
    //姓名
    private String name;

}
