package cn.com.xgit.gw.authorization.config;


import cn.com.xgit.gw.authorization.filter.CommonUsernamePasswordFilter;
import cn.com.xgit.gw.authorization.filter.PhoneLoginAuthenticationFilter;
import cn.com.xgit.gw.authorization.filter.QrLoginAuthenticationFilter;
import cn.com.xgit.gw.authorization.handler.CommonLoginAuthSuccessHandler;
import cn.com.xgit.gw.authorization.provider.PhoneAuthenticationProvider;
import cn.com.xgit.gw.authorization.provider.QrAuthenticationProvider;
import cn.com.xgit.gw.authorization.userdetails.CommonUserDetailService;
import cn.com.xgit.gw.authorization.userdetails.PhoneUserDetailService;
import cn.com.xgit.gw.authorization.userdetails.QrUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@Order
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 自动注入UserDetailsService
    @Autowired
    private CommonUserDetailService commonUserDetailService;

    @Autowired
    private PhoneUserDetailService phoneUserDetailService;

    @Autowired
    private QrUserDetailService qrUserDetailService;


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterAt(getUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(getPhoneLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(getQrLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 配置登陆页/login并允许访问
                .formLogin().permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/login").permitAll()
                // 登出页
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/backReferer")
                // 其余所有请求全部需要鉴权认证
                .and().authorizeRequests().anyRequest().authenticated()
                .and()
                .authorizeRequests().anyRequest().access("@rbacService.hasPermission(request, authentication)")
                // 由于使用的是JWT，我们这里不需要csrf
                .and().csrf().disable();


    }


    /**
     * 用户验证
     *
     * @param auth
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(phoneAuthenticationProvider());
        auth.authenticationProvider(daoAuthenticationProvider());
        auth.authenticationProvider(qrAuthenticationProvider());
    }

    /**
     * configure(WebSecurity)用于影响全局安全性(配置资源，设置调试模式，通过实现自定义防火墙定义拒绝请求)的配置设置。
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/favicon.ico")
                .antMatchers("/connect/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/webjars/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/v2/**");
    }

    @Bean
    public BCryptPasswordEncoder myEncoder() {
        return new BCryptPasswordEncoder(6);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider1 = new DaoAuthenticationProvider();
        // 设置userDetailsService
        provider1.setUserDetailsService(commonUserDetailService);
        // 禁止隐藏用户未找到异常
        provider1.setHideUserNotFoundExceptions(false);
        // 使用BCrypt进行密码的hash
        provider1.setPasswordEncoder(myEncoder());
        return provider1;
    }


    @Bean
    public PhoneAuthenticationProvider phoneAuthenticationProvider() {
        PhoneAuthenticationProvider provider = new PhoneAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(phoneUserDetailService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public QrAuthenticationProvider qrAuthenticationProvider() {
        QrAuthenticationProvider provider = new QrAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(qrUserDetailService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }


    /**
     * 手机验证码登陆过滤器
     *
     * @return
     */
    @Bean
    public PhoneLoginAuthenticationFilter getPhoneLoginAuthenticationFilter() {
        PhoneLoginAuthenticationFilter filter = new PhoneLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            log.error("", e);
        }
        filter.setAuthenticationSuccessHandler(new CommonLoginAuthSuccessHandler());
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"));
        return filter;
    }

    @Bean
    public QrLoginAuthenticationFilter getQrLoginAuthenticationFilter() {
        QrLoginAuthenticationFilter filter = new QrLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            log.error("", e);
        }
        filter.setAuthenticationSuccessHandler(new CommonLoginAuthSuccessHandler());
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"));
        return filter;
    }

    @Bean
    public CommonUsernamePasswordFilter getUsernamePasswordAuthenticationFilter() {
        CommonUsernamePasswordFilter filter = new CommonUsernamePasswordFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            log.error("", e);
        }
        filter.setAuthenticationSuccessHandler(new CommonLoginAuthSuccessHandler());
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"));
        return filter;
    }
}
