package cn.com.xgit.parts.auth.account.dao.entity.sys;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 角色信息表 DO
 */
@Data
@ToString
public class SysRoleDO  implements Serializable{
    private static final long serialVersionUID = -1L;
    //角色ID
    private String id;
    //角色名称
    private String name;
    //9:系统默认，用户不能删除
    private String remark;
    //1：系统管理员，2普通角色，系统管理员内置不允许界面更改
    private Long type;
    //渠道（0初始化，1平台录入）
    private Long channel;
    //所属工作空间ID
    private String spaceId;
    //所属部门ID（0表示角色与部门不关联）
    private String deptId;
    //
    private Long seqNo;

}
