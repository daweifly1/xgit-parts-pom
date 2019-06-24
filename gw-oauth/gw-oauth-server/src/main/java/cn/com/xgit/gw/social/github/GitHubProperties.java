package cn.com.xgit.gw.social.github;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "xgxx.social.github")
@Data
@RefreshScope
public class GitHubProperties {
    //appId
    private String appId;
    //appSecret
    private String appSecret;

    private String callBack;

    //去绑定github页面
    private String bindGitHubUrl;
    //绑定成功后跳转页面
    private String bindGitHubSuccessUrl;

    //绑定失败后跳转页面
    private String bindGitHubFaileUrl;

    //登录成功后跳转页面
    private String loginGitHubSuccessUrl;

    //登录失败后跳转页面
    private String loginGitHubFailUrl;

}
