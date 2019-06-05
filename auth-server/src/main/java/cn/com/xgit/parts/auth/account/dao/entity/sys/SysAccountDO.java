package cn.com.xgit.parts.auth.account.dao.entity.sys;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 账号信息表 DO
 */
@Data
@ToString
public class SysAccountDO implements Serializable {
    private static final long serialVersionUID = -1L;
    //用户id
    private String userId;
    //登录名
    private String loginName;
    //最近登录时间
    private Date lastLoginTime;
    //最近登录IP
    private String lastLoginIp;
    //0正常，1锁住
    private Integer locked;
    //姓名
    private String name;
    //手机号
    private String mobile;
    //联系电话
    private String telephone;
    //性别（0女，1男）
    private Integer sex;
    //头像
    private String icon;
    //昵称
    private String nickname;
    //邮箱
    private String email;
    //身份证号
    private String idNumber;
    //地区编码
    private String areaCode;
    //erp员工编码
    private String erpCode;
    //员工编码
    private String code;
    //公司ID，为机构或其父节点
    private String orgId;
    //机构ID
    private String deptId;
    //备注
    private String remark;
    //状态（0有效，1无效）
    private Integer status;
    //添加时间
    private Date createTime;
    //密码输入错误次数
    private Integer passwordErrorTimes;
    //修改时间
    private Date updateTime;

}
