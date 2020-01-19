package com.ebig.mcp.server.api.http.service.impl;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.FactoryStatusEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.utils.HttpUtils;
import com.ebig.mcp.server.api.http.service.OrgFactoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:供应商商品通用服务实现类
 * @author: wenchao
 * @time: 2019-10-22 14:14
 */
@Service
public class OrgFactoryInfoServiceImpl implements OrgFactoryInfoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${seqCodeUrl}")
    private String seqCodeUrl;

    @Override
    public List<OrgFactoryInfoEntity> getExistedFactoryInfo(List<String> factoryNameList) {
        String sql = "SELECT id,factory_name FROM hdi_org_factory_info WHERE del_flag = 0 and factory_name in (:factoryNames)";
        Map<String, Object> args = new HashMap<>(1);
        args.put("factoryNames", factoryNameList);
        NamedParameterJdbcTemplate givenParamJdbcTemp = new NamedParameterJdbcTemplate(jdbcTemplate);
        return givenParamJdbcTemp.query(sql, args, new OrgFactoryInfoEntity());
    }

    @Override
    public List<OrgFactoryInfoEntity> insertBatch(final List<OrgFactoryInfoEntity> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            jdbcTemplate.execute(new ConnectionCallback() {
                @Override
                public Object doInConnection(Connection conn) throws SQLException {
                    String insertSql = "insert into hdi_org_factory_info(factory_code,factory_name,status,create_time,del_flag) " +
                            "values(?,?,?,?,?)";
                    PreparedStatement pstmt =
                            conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    for (OrgFactoryInfoEntity entity : list) {
                        pstmt.setString(1, HttpUtils.doGet(seqCodeUrl + SequenceEnum.FACTORY_CODE.getKey()));
                        pstmt.setString(1, String.valueOf(System.currentTimeMillis()));

                        pstmt.setString(2, entity.getFactoryName());
                        pstmt.setInt(3, FactoryStatusEnum.DRAFT.getKey());
                        pstmt.setDate(4, new Date(System.currentTimeMillis()));
                        pstmt.setInt(5, DelFlagEnum.NORMAL.getKey());
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                    ResultSet rs = pstmt.getGeneratedKeys();
                    int rowNum = 0;
                    while (rs.next()) {
                        list.get(rowNum).setId(rs.getLong(1));
                        rowNum++;
                    }
                    return null;
                }

            });
        }
        return list;
    }

    @Override
    public List<OrgFactoryInfoEntity> structOrgFactoryInfoList(List<String> list) {

        List<OrgFactoryInfoEntity> factoryInfoList = new ArrayList<>(list.size());
        OrgFactoryInfoEntity entity;
        for (String str : list) {
            entity = new OrgFactoryInfoEntity();
            entity.setFactoryName(str);
            factoryInfoList.add(entity);
        }
        return factoryInfoList;
    }
}
