package cn.com.xgit.parts.auth.module.log.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统操作日志 VO类
 */
@Data
@ToString
public class SysOpLogVO implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "类型，1普通登录，2短信密码登录,3.SYS相关操作日志，4业务操作日志")
    private Integer type;
}
