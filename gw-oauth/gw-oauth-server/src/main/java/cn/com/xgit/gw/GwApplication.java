package cn.com.xgit.gw;

import cn.com.xgit.gw.graceful.GracefulShutdownUndertowWrapper;
import io.undertow.UndertowOptions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.util.HashSet;
import java.util.Set;

@SpringCloudApplication
@EnableAuthorizationServer
@EnableFeignClients(basePackages = {"cn.com.xgit.parts.auth.feign"})
@EnableZuulProxy
public class GwApplication {
    public static void main(String[] args) {
        SpringApplication.run(GwApplication.class, args);
    }

//    @Autowired
//    private GracefulShutdownUndertowWrapper gracefulShutdownUndertowWrapper;
//
//    @Bean
//    public UndertowServletWebServerFactory servletWebServerFactory() {
//        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
//        factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addOuterHandlerChainWrapper(gracefulShutdownUndertowWrapper));
//        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_STATISTICS, true));
//        return factory;
//    }

//    @Bean
//    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(@Value("${fast.webfront.error-page.notfound: }") final String a) {
//        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
//            @Override
//            public void customize(ConfigurableWebServerFactory factory) {
//                if (StringUtils.isNotBlank(String.valueOf(a))) {
//                    ErrorPage localErrorPage = new ErrorPage(HttpStatus.NOT_FOUND, a);
//                    Set<ErrorPage> s = new HashSet<ErrorPage>();
//                    s.add(localErrorPage);
//                    factory.setErrorPages(s);
//                }
//
//            }
//        };
//    }
}
