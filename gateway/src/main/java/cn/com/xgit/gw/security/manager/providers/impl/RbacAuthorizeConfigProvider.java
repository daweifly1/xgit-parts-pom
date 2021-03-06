/**
 *
 */
package cn.com.xgit.gw.security.manager.providers.impl;

import cn.com.xgit.gw.security.manager.providers.AuthorizeConfigProvider;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
@Order(Integer.MAX_VALUE)
public class RbacAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

        config.antMatchers("/fonts/**", "/auth/**", "/connect/**").permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/**",
                        "/**/*.html", "login", "logout",
                        "/admin/me",
                        "/resource").authenticated()
                .anyRequest()
                .access("@rbacService.hasPermission(request, authentication)");
        return true;
    }

}
