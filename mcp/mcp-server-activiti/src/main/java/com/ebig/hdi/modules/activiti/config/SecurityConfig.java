package com.ebig.hdi.modules.activiti.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 自定义认证类
 *
 * @author wenchao
 * @date 2019/09/29
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.authorizeRequests()
                .anyRequest().permitAll().and().logout().permitAll();//配置不需要登录验证
        http.csrf().disable();
    }
}
