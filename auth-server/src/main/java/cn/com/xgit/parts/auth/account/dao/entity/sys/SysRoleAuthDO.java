package cn.com.xgit.parts.auth.account.dao.entity.sys;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 角色权限关联关系表 DO
 */
@Data
@ToString
public class SysRoleAuthDO  implements Serializable{
    private static final long serialVersionUID = -1L;
    //
    private Integer id;
    //角色ID
    private String roleId;
    //权限ID
    private Integer authId;
    //状态
    private Integer status;

}
