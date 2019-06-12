package cn.com.xgit.parts.auth.module.account.param;

import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@ApiModel(value = "注册信息")
public class UserRegistVO implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "登录密码相关信息")
    private UserLoginVO userLoginVO;

    @ApiModelProperty(value = "账户信息")
    private SysAccountVO sysAccountVO;


}
