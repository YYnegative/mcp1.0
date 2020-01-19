package com.ebig.hdi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@EnableEurekaClient
@MapperScan(basePackages = {"com.ebig.hdi.modules.*.dao"})
public class HdiServerAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(HdiServerAdminApplication.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HdiServerAdminApplication.class);
    }
}
