package cn.com.xgit.parts.auth.module.account.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
@ApiModel(value = "用户登录信息")
public class SysUserLoginInfoVO {
    //用户id
    private Long id;
    //登录名
    private String username;
    //姓名
    private String name;

    Set<Long> roleIds;

}
