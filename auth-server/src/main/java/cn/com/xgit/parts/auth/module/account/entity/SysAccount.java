package cn.com.xgit.parts.auth.module.account.entity;

import cn.com.xgit.parts.common.base.entity.DataEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 账号信息表 DO
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_account")
@ApiModel(value = "SysAccount", description = "账号信息表")
public class SysAccount extends DataEntity {
    private static final long serialVersionUID = -1L;
    //登录名
    @ApiModelProperty(value = "登录名", name = "login_name")
    private String loginName;
    //最近登录时间
    @ApiModelProperty(value = "最近登录时间", name = "last_login_time")
    private Date lastLoginTime;
    //最近登录IP
    @ApiModelProperty(value = "最近登录IP", name = "last_login_ip")
    private String lastLoginIp;
    //0正常，1锁住
    @ApiModelProperty(value = "0正常，1锁住", name = "locked")
    private Integer locked;
    //姓名
    @ApiModelProperty(value = "姓名", name = "name")
    private String name;
    //手机号
    @ApiModelProperty(value = "手机号", name = "mobile")
    private String mobile;
    //联系电话
    @ApiModelProperty(value = "联系电话", name = "telephone")
    private String telephone;
    //性别（0女，1男）
    @ApiModelProperty(value = "性别（0女，1男）", name = "sex")
    private Integer sex;
    //头像
    @ApiModelProperty(value = "头像", name = "icon")
    private String icon;
    //昵称
    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname;
    //邮箱
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;
    //身份证号
    @ApiModelProperty(value = "身份证号", name = "id_number")
    private String idNumber;
    //地区编码
    @ApiModelProperty(value = "地区编码", name = "area_code")
    private String areaCode;
    //erp员工编码
    @ApiModelProperty(value = "erp员工编码", name = "erp_code")
    private String erpCode;
    //员工编码
    @ApiModelProperty(value = "员工编码", name = "code")
    private String code;
    //公司ID，为机构或其父节点
    @ApiModelProperty(value = "公司ID，为机构或其父节点", name = "org_id")
    private String orgId;
    //机构ID
    @ApiModelProperty(value = "机构ID", name = "dept_id")
    private String deptId;
    //备注
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    //
    @ApiModelProperty(value = "密码输错次数", name = "password_error_times")
    private Integer passwordErrorTimes;

    @ApiModelProperty(value = "已经存在的id,查询时候需要排除")
    @TableField(exist = false)
    private Long exId;
}
