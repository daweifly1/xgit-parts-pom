package cn.com.xgit.parts.auth.account.dao.entity.sys;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户角色表 DO
 */
@Data
@ToString
public class SysUserRolesDO implements Serializable {
    private static final long serialVersionUID = -1L;
    //用户标识
    private String userId;
    //角色标识
    private String roleId;
    //是否主角色（1主角色，0普通角色，由业务决定）
    private Integer roleFlag;

    public SysUserRolesDO(String userId, String roleId, Integer roleFlag) {
        this.userId = userId;
        this.roleId = roleId;
        this.roleFlag = roleFlag;
    }
}
