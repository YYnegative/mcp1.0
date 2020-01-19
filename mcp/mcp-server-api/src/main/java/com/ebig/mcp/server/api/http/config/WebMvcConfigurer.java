package com.ebig.mcp.server.api.http.config;

import com.ebig.mcp.server.api.http.interceptor.SignInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(signInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);

    }

    @Bean
    public SignInterceptor signInterceptor() {

        return new SignInterceptor();

    }
}
