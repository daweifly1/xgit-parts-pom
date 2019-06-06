package cn.com.xgit.parts.auth.account.dao.entity.sys;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 密码信息表 DO
 */
@Data
@ToString
public class SysPasswordDO implements Serializable {
    private static final long serialVersionUID = -1L;
    //
    private Long id;
    //用户ID
    private Long userId;
    //密码
    private String password;
    //修改时间
    private Date updateTime;
    //用来区分密码安全,1.普通登录密码
    private Integer type;

}
