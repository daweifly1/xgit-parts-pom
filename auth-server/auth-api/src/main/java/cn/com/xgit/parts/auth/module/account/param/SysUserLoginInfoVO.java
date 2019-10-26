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

    //当前平台
    private Long platformId;
    //当前总店
    private Long storeId;
    //当前分店
    private Long shopId;

    //根据平台查询拥有的角色
    private Set<Long> roleIds;
}
