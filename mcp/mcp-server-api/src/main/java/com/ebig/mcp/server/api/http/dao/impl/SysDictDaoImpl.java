package com.ebig.mcp.server.api.http.dao.impl;

import com.ebig.hdi.common.utils.HttpUtils;
import com.ebig.mcp.server.api.http.dao.SysDictDao;
import com.ebig.mcp.server.api.http.entity.SysDictEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

/**
 * @description: 字典持久化实现类
 * @author: wenchao
 * @time: 2019-10-26 15:36
 */
@Repository
public class SysDictDaoImpl implements SysDictDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Value("${seqCodeUrl}")
    private String seqCodeUrl;

    @Override
    public List<SysDictEntity> selectDictByType(String type) {
        String sql = "select t.id,t.code,t.type,t.value from sys_dict t where t.type =?";
        Object[] obj = {type};
        return jdbcTemplate.query(sql, obj, new SysDictEntity());
    }

    @Override
    public String insert(SysDictEntity entity) {
        if (entity != null) {
            jdbcTemplate.execute(new ConnectionCallback() {
                @Override
                public Object doInConnection(Connection conn) throws SQLException {
                    String insertSql = "insert into sys_dict(" +
                            "`name`,`type`,parent_code,code,`value`,del_flag,remark)" +
                            "values(?,?,?,?,?,?,?)";
                    int count = 0;
                    String code = HttpUtils.doGet(seqCodeUrl +"GOODS_UNIT_CODE");
                    PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(++count, entity.getName());
                    ps.setString(++count, entity.getType());
                    ps.setString(++count, entity.getParentCode());
                    ps.setString(++count,code );
                    ps.setString(++count, entity.getValue());
                    ps.setInt(++count, entity.getDelFlag());
                    ps.setString(++count,entity.getRemark());
                    ps.execute();
                    return code;
                }

            });
        }
        return null;
    }
}
