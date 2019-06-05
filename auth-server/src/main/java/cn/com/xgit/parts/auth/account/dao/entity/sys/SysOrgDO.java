package cn.com.xgit.parts.auth.account.dao.entity.sys;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 组织结构信息表 DO
 */
@Data
@ToString
public class SysOrgDO  implements Serializable{
    private static final long serialVersionUID = -1L;
    //ID
    private String id;
    //机构名称
    private String name;
    //code
    private String code;
    //erp部门编码
    private String erpCode;
    //上级机构ID
    private String parentId;
    //排序
    private Integer seq;
    //层级节点，0开始
    private Long level;
    //添加时间
    private Date createTime;
    //修改时间
    private Date updateTime;

}
