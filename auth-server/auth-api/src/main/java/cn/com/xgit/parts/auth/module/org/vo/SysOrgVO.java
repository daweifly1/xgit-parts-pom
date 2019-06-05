package cn.com.xgit.parts.auth.module.org.vo;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 组织结构信息表 VO类
 */
@Data
@ToString
public class SysOrgVO  implements Serializable{
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "机构名称")
    private String name;
    @ApiModelProperty(value = "code")
    private String code;
    @ApiModelProperty(value = "erp部门编码")
    private String erpCode;
    @ApiModelProperty(value = "上级机构ID")
    private String parentId;
    @ApiModelProperty(value = "排序")
    private Integer seq;
    @ApiModelProperty(value = "层级节点，0开始")
    private Long level;
    @ApiModelProperty(value = "添加时间")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
