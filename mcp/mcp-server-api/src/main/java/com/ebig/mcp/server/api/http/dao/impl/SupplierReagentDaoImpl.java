package com.ebig.mcp.server.api.http.dao.impl;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.utils.HttpUtils;
import com.ebig.mcp.server.api.http.dao.SupplierReagentDao;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierReagentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 供应商药品持久化实现类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
@Repository
public class SupplierReagentDaoImpl implements SupplierReagentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${seqCodeUrl}")
    private String seqCodeUrl;

    /**
     * 查询已经存在的试剂数据
     *
     * @param subList
     * @param supplierInfo
     * @return
     */
    @Override
    public List<GoodsSupplierReagentVo> getSupplierReagent(List<GoodsSupplierReagentVo> subList, OrgSupplierInfoEntity supplierInfo) {
        List<GoodsSupplierReagentVo> reagentList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subList)) {
            String sql = "SELECT t1.id, t1.reagent_name,t3.supplier_code" +
                    "  FROM hdi_goods_supplier_reagent t1  LEFT JOIN hdi_org_supplier_info t3 " +
                    "  ON t1.supplier_id = t3.id AND t3.del_flag = 0 WHERE t1.del_flag = 0 " +
                    " AND t1.reagent_name =? AND t1.supplier_id =? ";
            for (GoodsSupplierReagentVo vo : subList) {
                List<GoodsSupplierReagentVo> list = jdbcTemplate.query(sql, new Object[]{vo.getReagentName(), supplierInfo.getId()}, new GoodsSupplierReagentVo());
                if (!CollectionUtils.isEmpty(list)) {
                    vo.setId(list.get(0).getId());
                    reagentList.add(vo);
                }
            }
        }
        return reagentList;
    }

    /**
     * 更新试剂
     *
     * @param list
     */
    @Override
    public List<GoodsSupplierReagentVo> update(final List<GoodsSupplierReagentVo> list, OrgSupplierInfoEntity supplierInfo, Map<String, Long> map) {
        jdbcTemplate.execute(new ConnectionCallback() {
            @Override
            public Object doInConnection(Connection conn) throws SQLException {
                String updateSql = "UPDATE hdi_goods_supplier_reagent set supplier_id =?,dept_id =?,status =?" +
                        ",del_flag =?,is_match = ?,data_source =?,is_upload =?,edit_time =?,edit_id =?," +
                        "reagent_name =?,factory_id =?,goods_unit =?,supply_unit =?,convert_unit =?,approvals = ?," +
                        "goods_nature =?,type_name =?,store_way=? where id =?";
                PreparedStatement ps =
                        conn.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS);
                for (GoodsSupplierReagentVo entity : list) {
                    ps.setLong(1, supplierInfo.getId());
                    ps.setLong(2, supplierInfo.getDeptId());
                    ps.setInt(3, SupplierDataSource.ERP.getKey());
                    ps.setInt(4, DelFlagEnum.NORMAL.getKey());
                    ps.setInt(5, IsMatchEnum.NO.getKey());
                    ps.setInt(6, DataSourceEnum.PORT.getKey());
                    ps.setInt(7, SupplierIsUploadEnum.YES.getKey());
                    ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                    ps.setLong(9, supplierInfo.getId());
                    ps.setString(10, entity.getReagentName());
                    ps.setLong(11, map.get(entity.getFactoryName()));
                    ps.setString(12, entity.getGoodsUnit());
                    ps.setString(13, entity.getSupplyUnit());
                    ps.setString(14, entity.getConvertUnit());
                    ps.setString(15, entity.getApprovals());
                    ps.setInt(16, entity.getGoodsNature());
                    ps.setString(17, entity.getTypeName());
                    ps.setString(18, entity.getStoreWay());
                    ps.setLong(19, entity.getId());
                    ps.addBatch();
                }
                ps.executeBatch();
                ResultSet rs = ps.getGeneratedKeys();
                int rowNum = 0;
                while (rs.next()) {
                    list.get(rowNum).setId(rs.getLong(1));
                    rowNum++;
                }
                return null;
            }

        });
        return list;
    }


    /**
     * 批量插入
     *
     * @param list
     */
    @Override
    public List<GoodsSupplierReagentVo> insert(final List<GoodsSupplierReagentVo> list, OrgSupplierInfoEntity supplierInfo, Map<String, Long> map) {
        jdbcTemplate.execute(new ConnectionCallback() {
            @Override
            public Object doInConnection(Connection conn) throws SQLException {
                String insertSql = "insert into hdi_goods_supplier_reagent(supplier_id,dept_id,status,del_flag,is_match,data_source," +
                        "is_upload,create_time,create_id,reagent_code,reagent_name,factory_id,goods_unit,supply_unit,convert_unit,approvals" +
                        ",goods_nature,type_name,store_way)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps =
                        conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                for (GoodsSupplierReagentVo entity : list) {
                    ps.setLong(1, supplierInfo.getId());
                    ps.setLong(2, supplierInfo.getDeptId());
                    ps.setInt(3, SupplierDataSource.ERP.getKey());
                    ps.setInt(4, DelFlagEnum.NORMAL.getKey());
                    ps.setInt(5, IsMatchEnum.NO.getKey());
                    ps.setInt(6, DataSourceEnum.PORT.getKey());
                    ps.setInt(7, SupplierIsUploadEnum.YES.getKey());
                    ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                    ps.setLong(9, supplierInfo.getId());
                    ps.setString(10, HttpUtils.doGet(seqCodeUrl + SequenceEnum.SUPPLIER_REAGENT_CODE.getKey()));
                    ps.setString(11, entity.getReagentName());
                    ps.setLong(12, map.get(entity.getFactoryName()));
                    ps.setString(13, entity.getGoodsUnit());
                    ps.setString(14, entity.getSupplyUnit());
                    ps.setString(15, entity.getConvertUnit());
                    ps.setString(16, entity.getApprovals());
                    ps.setInt(17, entity.getGoodsNature());
                    ps.setString(18, entity.getTypeName());
                    ps.setString(19, entity.getStoreWay());
                    ps.addBatch();
                }
                ps.executeBatch();
                ResultSet rs = ps.getGeneratedKeys();
                int rowNum = 0;
                while (rs.next()) {
                    list.get(rowNum).setId(rs.getLong(1));
                    rowNum++;
                }
                return null;
            }

        });
        return list;
    }

}
