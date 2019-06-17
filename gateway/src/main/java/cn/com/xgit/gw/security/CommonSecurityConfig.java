/**
 *
 */
package cn.com.xgit.gw.security;

import cn.com.xgit.gw.security.jwt.JWTAuthenticationFilter;
import cn.com.xgit.gw.security.manager.XgAuthorizeConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class CommonSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private XgAuthorizeConfigManager authorizeConfigManager;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable().authorizeRequests()
                .antMatchers("/ius/auth/**","/connect/**").permitAll()
                .anyRequest().authenticated()
                .and()
//                //记住我配置，如果想在'记住我'登录时记录日志，可以注册一个InteractiveAuthenticationSuccessEvent事件的监听器
//                .rememberMe()
//                .tokenRepository(persistentTokenRepository())
//                .tokenValiditySeconds(60 * 60 * 24)
//                .userDetailsService(userDetailsService)
//                .and()
//                .sessionManagement()
//                .invalidSessionStrategy(invalidSessionStrategy())
//                .maximumSessions(5)
//                .maxSessionsPreventsLogin(true)
//                .expiredSessionStrategy(expiredSessionStrategy())
//                .and()
//                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        authorizeConfigManager.config(http.authorizeRequests());//授权
    }

    /**
     * configure(WebSecurity)用于影响全局安全性(配置资源，设置调试模式，通过实现自定义防火墙定义拒绝请求)的配置设置。
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/favicon.ico")
                .antMatchers("/connect/**")
                .antMatchers(HttpMethod.OPTIONS, "/ius/auth/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/webjars/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/v2/**");
    }
}
