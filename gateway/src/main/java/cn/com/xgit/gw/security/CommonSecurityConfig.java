/**
 *
 */
package cn.com.xgit.gw.security;

import cn.com.xgit.gw.security.filter.JWTAuthenticationFilter;
import cn.com.xgit.gw.security.manager.XgAuthorizeConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author jy88
 */
@Configuration
public class CommonSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private XgAuthorizeConfigManager authorizeConfigManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        http.csrf().disable().authorizeRequests()
                .antMatchers("/auth/**", "/non/**").permitAll()
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
                .addFilterBefore(new com.xiaojun.auth.filter.JWTLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTAuthenticationFilter(), com.xiaojun.auth.filter.JWTLoginFilter.class);
        authorizeConfigManager.config(http.authorizeRequests());
    }
}
