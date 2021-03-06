package cn.com.xgit.gw.module;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "xgxx.customs")
@Data
@RefreshScope
public class CustomsSecurityProperties {
    //jwt创建者信息
    private String claimKeyCreated;
    //jwt秘钥
    private String claimKeySecret;
    //jwt超时时间
    private Long jwtExpiration;

    //登陆页url
    private String signInPage;

    //自定义登陆页url
    private String customSignInPage;

    //退出的链接
    private String signOutPage;

    //不要认证就可以访问的链接
    private String[] permitAllUrls;
    //认证后就可以访问的链接
    private String[] permitAccessUrls;

    //登录成功跳转页面
    private String loginSuccRedirectUrl;

    //退出后的链接
    private String signOutSuccessUrl;

    //未登录跳转页面
    private String noLoginRedirectUrl;

    private String redirectErrorPage403;

    private String redirectErrorPage404;

    private String redirectErrorPage500;

    //超级管理员
    private String[] superAdmins;
    //网管的i地址
    private String gwUrl;
    //对称加密密钥
    private String aesSeed;
}
