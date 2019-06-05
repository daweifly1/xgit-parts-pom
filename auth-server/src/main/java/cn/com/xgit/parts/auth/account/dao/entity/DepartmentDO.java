package cn.com.xgit.parts.auth.account.dao.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DepartmentDO {
    private String id;
    private String spaceId;
    private String name;
    private String code;
    private String parentId;
    private Integer seq;
    private Integer leaf;
}
