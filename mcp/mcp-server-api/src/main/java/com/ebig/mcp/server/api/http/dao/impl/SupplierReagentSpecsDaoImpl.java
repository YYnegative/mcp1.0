package com.ebig.mcp.server.api.http.dao.impl;


import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.entity.GoodsSupplierReagentSpecsEntity;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.utils.HttpUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.mcp.server.api.http.dao.SupplierReagentSpecsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 供应商药品规格持久化实现类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
@Repository
public class SupplierReagentSpecsDaoImpl implements SupplierReagentSpecsDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${seqCodeUrl}")
    private String seqCodeUrl;

    /**
     * 批量插入规格数据
     *
     * @param list
     */
    @Override
    public void insertSpecs(List<GoodsSupplierReagentSpecsEntity> list) {
        if (com.baomidou.mybatisplus.toolkit.CollectionUtils.isNotEmpty(list)) {
            jdbcTemplate.execute(new ConnectionCallback() {
                @Override
                public Object doInConnection(Connection conn) throws SQLException {
                    String insertSql = "insert into hdi_goods_supplier_reagent_specs(" +
                            "reagen_id,specs_code,specs,guid,status,create_id,create_time,sources_specs_id)" +
                            "values(?,?,?,?,?,?,?,?)";
                    PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    for (GoodsSupplierReagentSpecsEntity entity : list) {
                        ps.setLong(1, entity.getReagenId());
                        if (StringUtil.isEmpty(entity.getSpecsCode())) {
                            ps.setString(2, HttpUtils.doGet(seqCodeUrl + SequenceEnum.SUPPLIER_REAGENT_SPECS_CODE.getKey()));
                        } else {
                            ps.setString(2, entity.getSpecsCode());
                        }
                        ps.setString(3, entity.getSpecs());
                        ps.setString(4, entity.getGuid());
                        ps.setInt(5, entity.getStatus());
                        ps.setLong(6, entity.getCreateId());
                        ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                        ps.setString(8, entity.getSourcesSpecsId());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    return null;
                }

            });
        }
    }

    /**
     * 更新规格
     *
     * @param list
     */
    @Override
    public void updateSpecs(List<GoodsSupplierReagentSpecsEntity> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            jdbcTemplate.execute(new ConnectionCallback() {
                @Override
                public Object doInConnection(Connection conn) throws SQLException {
                    String insertSql = " update hdi_goods_supplier_reagent_specs " +
                            "set specs_code =?,guid =?,status =?,edit_id =?,create_time =?" +
                            " where reagen_id =? and specs =?";
                    PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    for (GoodsSupplierReagentSpecsEntity entity : list) {
                        if (StringUtil.isEmpty(entity.getSpecsCode())) {
                            ps.setString(1, HttpUtils.doGet(seqCodeUrl + SequenceEnum.SUPPLIER_REAGENT_SPECS_CODE.getKey()));
                        } else {
                            ps.setString(1, entity.getSpecsCode());
                        }
                        ps.setString(2, entity.getGuid());
                        ps.setInt(3, entity.getStatus());
                        ps.setLong(4, entity.getCreateId());
                        ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                        ps.setLong(6, entity.getReagenId());
                        ps.setString(7, entity.getSpecs());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    return null;
                }

            });
        }
    }

    /**
     * 获取不存在的规格数据
     *
     * @param list 查询条件集合
     * @return 不存在的规格数据
     */
    @Override
    public List<GoodsSupplierReagentSpecsEntity> getNotExistSpecs(List<GoodsSupplierReagentSpecsEntity> list) {
        List<GoodsSupplierReagentSpecsEntity> subList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            String sql = "SELECT specs" +
                    "  FROM hdi_goods_supplier_reagent_specs where reagen_id =? and specs =? ";
            for (GoodsSupplierReagentSpecsEntity vo : list) {
                List<String> specs = jdbcTemplate.queryForList(sql, new Object[]{vo.getReagenId(), vo.getSpecs()}, String.class);
                if (CollectionUtils.isEmpty(specs)) {
                    subList.add(vo);
                }

            }
        }
        return subList;
    }
}
