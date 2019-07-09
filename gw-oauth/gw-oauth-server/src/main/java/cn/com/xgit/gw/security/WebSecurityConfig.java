package cn.com.xgit.gw.security;


import cn.com.xgit.gw.module.CustomsSecurityProperties;
import cn.com.xgit.gw.security.filter.CommonUsernamePasswordFilter;
import cn.com.xgit.gw.security.filter.JwtAuthenticationTokenFilter;
import cn.com.xgit.gw.security.filter.PhoneLoginAuthenticationFilter;
import cn.com.xgit.gw.security.filter.QrLoginAuthenticationFilter;
import cn.com.xgit.gw.security.handler.CommonLoginFailHandler;
import cn.com.xgit.gw.security.handler.CommonLoginSuccessHandler;
import cn.com.xgit.gw.security.handler.JwtAuthenticationEntryPoint;
import cn.com.xgit.gw.security.handler.JwtLogoutSuccessHandler;
import cn.com.xgit.gw.security.provider.PhoneAuthenticationProvider;
import cn.com.xgit.gw.security.provider.QrAuthenticationProvider;
import cn.com.xgit.gw.security.userdetails.CommonUserDetailService;
import cn.com.xgit.gw.security.userdetails.PhoneUserDetailService;
import cn.com.xgit.gw.security.userdetails.QrUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Slf4j
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 自动注入UserDetailsService
    @Autowired
    private CommonUserDetailService commonUserDetailService;

    @Autowired
    private PhoneUserDetailService phoneUserDetailService;

    @Autowired
    private QrUserDetailService qrUserDetailService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private CommonLoginSuccessHandler commonLoginSuccessHandler;


    @Autowired
    private CommonLoginFailHandler commonLoginFailHandler;

    @Resource(name = "restAuthenticationAccessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;


    @Autowired
    private CustomsSecurityProperties customsSecurityProperties;


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，使用session 但是没有用
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and();
        //可以配置读取
        String[] permitUrls = {"/login", "/phoneLogin", "/api/v1/auth", "/api/v1/signout", "/error/**", "/api/**"};
        if (null != customsSecurityProperties.getPermitAllUrls() && customsSecurityProperties.getPermitAllUrls().length > 0) {
            permitUrls = customsSecurityProperties.getPermitAllUrls();
        }
        //登陆页url
        String signInPage = StringUtils.isBlank(customsSecurityProperties.getSignInPage()) ? "/login" : customsSecurityProperties.getSignInPage();
        //退出的链接
        String signOutPage = StringUtils.isBlank(customsSecurityProperties.getSignOutPage()) ? "/logout" : customsSecurityProperties.getSignOutPage();
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().antMatchers(permitUrls).permitAll().antMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginProcessingUrl(signInPage).permitAll()
//                .and().openidLogin().loginProcessingUrl("/openIdLogin").permitAll().successHandler(commonLoginSuccessHandler)
//                .and().oauth2Login().loginProcessingUrl("/authLogin").permitAll().successHandler(commonLoginSuccessHandler)
                // 登出页
                .and().logout().logoutUrl(signOutPage).logoutSuccessHandler(jwtLogoutSuccessHandler)
                .and().authorizeRequests()
                .anyRequest().access("@rbacService.hasPermission(request, authentication)");
        // 禁用缓存
        http.headers().cacheControl();

        // 添加JWT filter
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(getUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(getPhoneLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(getQrLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    /**
     * 用户验证
     *
     * @param auth
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(commonUserDetailService).passwordEncoder(passwordEncoder());
//
//        auth.authenticationProvider(phoneAuthenticationProvider());
//        auth.authenticationProvider(daoAuthenticationProvider());
//        auth.authenticationProvider(qrAuthenticationProvider());
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
                .antMatchers("/connect/**", "/actuator/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/webjars/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/v2/**");
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider1 = new DaoAuthenticationProvider();
        // 设置userDetailsService
        provider1.setUserDetailsService(commonUserDetailService);
        // 禁止隐藏用户未找到异常
        provider1.setHideUserNotFoundExceptions(false);
        // 使用BCrypt进行密码的hash
        provider1.setPasswordEncoder(passwordEncoder());
        return provider1;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public PhoneAuthenticationProvider phoneAuthenticationProvider() {
        PhoneAuthenticationProvider provider = new PhoneAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(phoneUserDetailService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        // 使用BCrypt进行密码的hash
        provider.setPasswordEncoder(passwordEncoder());
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
        filter.setAuthenticationSuccessHandler(commonLoginSuccessHandler);
        SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler("/login?error");

        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
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
        filter.setAuthenticationSuccessHandler(commonLoginSuccessHandler);
        SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler("/login?error");

        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
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
        filter.setAuthenticationSuccessHandler(commonLoginSuccessHandler);
        SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler("/login?error");

        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }

}
