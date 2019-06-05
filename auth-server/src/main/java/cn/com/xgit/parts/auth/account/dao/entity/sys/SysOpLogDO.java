package cn.com.xgit.parts.auth.account.dao.entity.sys;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统操作日志 DO
 */
@Data
@ToString
public class SysOpLogDO implements Serializable {
    private static final long serialVersionUID = -1L;
    //用户ID
    private String userId;
    //修改时间
    private Date updateTime;
    //备注
    private String remark;
    //日历类型，1普通登录，2短信密码登录,3.SYS相关操作日志，4业务操作日志
    private Integer type;

}
