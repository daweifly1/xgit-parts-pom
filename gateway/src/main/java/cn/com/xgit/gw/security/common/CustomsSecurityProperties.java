package cn.com.xgit.gw.security.common;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "parts")
@Data
public class CustomsSecurityProperties {
    //跳转的url
    private String destinationUrl;

    private String signInPage;

    private String signOutPage;

    private String signOutSuccessUrl;
}
