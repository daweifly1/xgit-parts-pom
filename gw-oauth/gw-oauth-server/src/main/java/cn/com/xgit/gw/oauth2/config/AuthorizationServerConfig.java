package cn.com.xgit.gw.oauth2.config;


import cn.com.xgit.gw.security.userdetails.CommonUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;

/**
 * Created by fp295 on 2018/4/16.
 */
@Configuration
@Order(2)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private CommonUserDetailService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;


    @Bean("jdbcTokenStore")
    public JdbcTokenStore getJdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }


    @Bean("jdbcClientDetailsService")
    public JdbcClientDetailsService getJdbcClientDetailsService() {
        JdbcClientDetailsService service = new JdbcClientDetailsService(dataSource);
        service.setPasswordEncoder(passwordEncoder);
        return service;
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 使用JdbcClientDetailsService客户端详情服务
        clients.withClientDetails(getJdbcClientDetailsService());
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                // 配置JwtAccessToken转换器
                .accessTokenConverter(jwtAccessTokenConverter())
                // refresh_token需要userDetailsService
                .reuseRefreshTokens(false).userDetailsService(userDetailsService).tokenStore(getJdbcTokenStore());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                // 开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("permitAll()")
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
    }

    /**
     * 使用非对称加密算法来对Token进行签名
     *
     * @return
     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//
//        final JwtAccessTokenConverter converter = new JwtAccessToken();
//        // 导入证书
//        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray());
//        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("test"));
//
//        return converter;
//    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    /**
     * 跨域, 开发环境使用 vue-cli 代理，正式用nginx
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
