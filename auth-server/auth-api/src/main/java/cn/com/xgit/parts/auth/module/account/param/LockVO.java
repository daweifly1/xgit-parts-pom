package cn.com.xgit.parts.auth.module.account.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class LockVO {
    @ApiModelProperty("要锁的用户集合")
    private List<Long> userIds;
    @ApiModelProperty("设置锁状态（0启用，1禁用）")
    private Integer lock;
}
