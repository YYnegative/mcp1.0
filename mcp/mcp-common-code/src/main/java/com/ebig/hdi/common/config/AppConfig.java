package com.ebig.hdi.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppConfig {
	@Value("${fdfs.resHost}")
    private String resHost;
	
	@Value("${fdfs.storagePort}")
    private String storagePort;
}
