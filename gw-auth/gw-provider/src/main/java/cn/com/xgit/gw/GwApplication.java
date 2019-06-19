package cn.com.xgit.gw;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringCloudApplication
@EnableAuthorizationServer
@EnableFeignClients(basePackages = {"cn.com.xgit.parts.auth.feign"})
@EnableZuulProxy
public class GwApplication {
    public static void main(String[] args) {
        SpringApplication.run(GwApplication.class, args);
    }
}
