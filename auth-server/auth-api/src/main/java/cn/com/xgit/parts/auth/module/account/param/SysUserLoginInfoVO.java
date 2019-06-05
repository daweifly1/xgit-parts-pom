package cn.com.xgit.parts.auth.module.account.param;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class SysUserLoginInfoVO {
    //用户id
    private String userId;
    //登录名
    private String loginName;
    //姓名
    private String name;

}
