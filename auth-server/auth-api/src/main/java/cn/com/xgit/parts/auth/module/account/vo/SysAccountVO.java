package cn.com.xgit.parts.auth.module.account.vo;

import cn.com.xgit.parts.auth.module.menu.vo.SysRoleVO;
import cn.com.xgit.parts.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 账号信息表 VO类
 */
@Data
@ToString
public class SysAccountVO extends BaseEntity implements Serializable{
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "公司ID，为机构或其父节点")
    private Long orgId;
    @ApiModelProperty(value = "机构ID")
    private Long deptId;

    @ApiModelProperty(value = "登录名")
    private String loginName;
    @ApiModelProperty(value = "最近登录时间")
    private Date lastLoginTime;
    @ApiModelProperty(value = "最近登录IP")
    private String lastLoginIp;
    @ApiModelProperty(value = "0正常，1锁住")
    private Integer locked;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "联系电话")
    private String telephone;
    @ApiModelProperty(value = "性别（0女，1男）")
    private Integer sex;
    @ApiModelProperty(value = "头像")
    private String icon;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
    @ApiModelProperty(value = "地区编码")
    private String areaCode;
    @ApiModelProperty(value = "erp员工编码")
    private String erpCode;
    @ApiModelProperty(value = "员工编码")
    private String code;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "状态（0有效，1无效）")
    private Integer status;
    @ApiModelProperty(value = "添加时间")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    //============非本表数据================================================
    @ApiModelProperty(value = "要排除的用户id")
    private String exUserId;
    @ApiModelProperty(value = "机构名称")
    private String deptName;
    private String roleNames;
    private List<SysRoleVO> roleVOs;
    private List<String> roleIds;
    private List<String> authIds;

}
