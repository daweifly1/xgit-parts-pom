package cn.com.xgit.parts.auth.module.account.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

@Data
@ToString
@ApiModel(value = "用户登录信息")
public class SysUserLoginInfoVO implements Serializable {

    private static final long serialVersionUID = -1L;
    //用户id
    private Long id;
    //登录名
    private String username;
    //姓名
    private String name;

    //密碼
    private String password;

    Set<Long> roleIds;

}
