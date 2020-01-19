package com.ebig.hdi.tool.generator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.ebig.hdi.tool.generator.dao.GeneratorDao;
import com.ebig.hdi.tool.generator.dao.MySQLGeneratorDao;
import com.ebig.hdi.tool.generator.dao.OracleGeneratorDao;
import com.ebig.hdi.tool.generator.dao.PostgreSQLGeneratorDao;
import com.ebig.hdi.tool.generator.dao.SQLServerGeneratorDao;
import com.ebig.hdi.tool.generator.utils.HdiException;

/**
 * 类功能说明：数据库配置<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 公司名称：信宜市新屋乐网络科技有限公司 <br/>
 * 作者：luorx <br/>
 * 创建时间：2019年1月19日 下午7:44:35 <br/>
 * 版本：V1.0 <br/>
 */
@Configuration
public class DbConfig {
    @Value("${xinwule.database: mysql}")
    private String database;
    @Autowired
    private MySQLGeneratorDao mySQLGeneratorDao;
    @Autowired
    private OracleGeneratorDao oracleGeneratorDao;
    @Autowired
    private SQLServerGeneratorDao sqlServerGeneratorDao;
    @Autowired
    private PostgreSQLGeneratorDao postgreSQLGeneratorDao;

    @Bean
    @Primary
    public GeneratorDao getGeneratorDao(){
        if("mysql".equalsIgnoreCase(database)){
            return mySQLGeneratorDao;
        }else if("oracle".equalsIgnoreCase(database)){
            return oracleGeneratorDao;
        }else if("sqlserver".equalsIgnoreCase(database)){
            return sqlServerGeneratorDao;
        }else if("postgresql".equalsIgnoreCase(database)){
            return postgreSQLGeneratorDao;
        }else {
            throw new HdiException("不支持当前数据库：" + database);
        }
    }
}
