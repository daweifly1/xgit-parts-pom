package cn.com.xgit.parts.auth.module.account.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UpdatePasswordVO {
    private String oldPassword;
    private String newPassword;
}
