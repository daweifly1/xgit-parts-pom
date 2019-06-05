package cn.com.xgit.parts.auth.account.dao.entity.sys;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * DO
 */
@Data
@ToString
public class SysAuthsDO implements Serializable {
    private static final long serialVersionUID = -1L;
    //pk
    private Integer id;
    //名称
    private String name;
    //编码
    private String code;
    //父节点id
    private Integer parentId;
    //顺序
    private Integer seq;
    //图标
    private String icon;
    //状态，0无效，1生效
    private Integer showFlag;
    //对应的url，若是菜单则为空
    private String url;
    //频道，保留字段
    private Integer channel;
    //是否叶子节点，默认否
    private Integer leaf;
    //类型，1,菜单 2按钮
    private Integer type;
    //创建用户
    private String createId;
    //
    private Date createDate;
    //
    private String updateId;
    //
    private Date updateDate;

}
