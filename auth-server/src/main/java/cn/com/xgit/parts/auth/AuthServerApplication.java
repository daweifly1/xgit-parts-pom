package cn.com.xgit.parts.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }


    @Bean
    public PasswordEncoder myEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
