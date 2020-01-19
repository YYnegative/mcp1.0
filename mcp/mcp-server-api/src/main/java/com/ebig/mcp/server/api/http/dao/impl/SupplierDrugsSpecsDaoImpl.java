package com.ebig.mcp.server.api.http.dao.impl;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.entity.GoodsSupplierDrugsSpecsEntity;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.utils.HttpUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.mcp.server.api.http.dao.SupplierDrugsSpecsDao;
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
public class SupplierDrugsSpecsDaoImpl implements SupplierDrugsSpecsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${seqCodeUrl}")
    private String seqCodeUrl;

    /**
     * 更新规格数据
     *
     * @param list
     */
    @Override
    public void updateSpecs(List<GoodsSupplierDrugsSpecsEntity> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            jdbcTemplate.execute(new ConnectionCallback() {
                @Override
                public Object doInConnection(Connection conn) throws SQLException {
                    String updateSql = " update hdi_goods_supplier_drugs_specs  " +
                            "set specs_code =?,guid =?,status =?,edit_id =?,edit_time =? " +
                            "where drugs_id =? and specs =?";
                    PreparedStatement ps = conn.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS);
                    for (GoodsSupplierDrugsSpecsEntity entity : list) {
                        if (StringUtil.isEmpty(entity.getSpecsCode())) {
                            ps.setString(1, HttpUtils.doGet(seqCodeUrl + SequenceEnum.SUPPLIER_REAGENT_SPECS_CODE.getKey()));
                        } else {
                            ps.setString(1, entity.getSpecsCode());
                        }
                        ps.setString(2, entity.getGuid());
                        ps.setInt(3, entity.getStatus());
                        ps.setLong(4, entity.getEditId());
                        ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                        ps.setLong(6, entity.getDrugsId());
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
     * 批量插入规格数据
     *
     * @param list
     */
    @Override
    public void insertSpecs(List<GoodsSupplierDrugsSpecsEntity> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            jdbcTemplate.execute(new ConnectionCallback() {
                @Override
                public Object doInConnection(Connection conn) throws SQLException {
                    String insertSql = "insert into hdi_goods_supplier_drugs_specs(" +
                            "drugs_id,specs_code,specs,guid,status,create_id,create_time,sources_specs_id)" +
                            "values(?,?,?,?,?,?,?,?)";
                    PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    for (GoodsSupplierDrugsSpecsEntity entity : list) {
                        ps.setLong(1, entity.getDrugsId());
                        if (StringUtil.isEmpty(entity.getSpecsCode())) {
                            ps.setString(2, HttpUtils.doGet(seqCodeUrl + SequenceEnum.SUPPLIER_DRUGS_SPECS_CODE.getKey()));
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
     * 获取不存在的规格数据
     *
     * @param list 查询条件集合
     * @return 不存在的规格数据
     */
    @Override
    public List<GoodsSupplierDrugsSpecsEntity> getNotExistSpecs(List<GoodsSupplierDrugsSpecsEntity> list) {
        List<GoodsSupplierDrugsSpecsEntity> subList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            String sql = "SELECT specs" +
                    "  FROM hdi_goods_supplier_drugs_specs where drugs_id =? and specs =? ";
            for (GoodsSupplierDrugsSpecsEntity vo : list) {
                List<String> specs = jdbcTemplate.queryForList(sql, new Object[]{vo.getDrugsId(), vo.getSpecs()}, String.class);
                if (CollectionUtils.isEmpty(specs)) {
                    subList.add(vo);
                }

            }
        }
        return subList;
    }

}
