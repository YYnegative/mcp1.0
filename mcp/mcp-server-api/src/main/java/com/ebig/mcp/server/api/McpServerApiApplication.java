package com.ebig.mcp.server.api;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Hello world!
 *
 */
@SpringBootApplication
//@ComponentScan({"com.ebig.mcp.server.api","com.github.tobato.fastdfs"})
//@Import(FdfsClientConfig.class)
public class McpServerApiApplication {
	
    public static void main( String[] args ){
    	SpringApplication.run(McpServerApiApplication.class, args);
    }


}
