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
}
