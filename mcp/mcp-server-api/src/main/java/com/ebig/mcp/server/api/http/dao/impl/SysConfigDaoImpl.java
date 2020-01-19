package com.ebig.mcp.server.api.http.dao.impl;

import com.ebig.mcp.server.api.http.dao.SysConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SysConfigDaoImpl implements SysConfigDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public String getBatchSize(String key) {
        List<String> strings = jdbcTemplate.queryForList("select param_value from sys_config where param_key=? ", String.class, new Object[]{key});
        return strings.get(0);
    }




}
