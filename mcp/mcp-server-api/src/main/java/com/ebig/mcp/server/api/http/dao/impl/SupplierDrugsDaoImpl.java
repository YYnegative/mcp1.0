package com.ebig.mcp.server.api.http.dao.impl;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.utils.HttpUtils;
import com.ebig.mcp.server.api.http.dao.SupplierDrugsDao;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierDrugsVo;
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
public class SupplierDrugsDaoImpl implements SupplierDrugsDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${seqCodeUrl}")
    private String seqCodeUrl;

    /**
     * 查询已经存在的药品数据
     *
     * @param subList
     * @param supplierInfo
     * @return
     */
    @Override
    public List<GoodsSupplierDrugsVo> getList(List<GoodsSupplierDrugsVo> subList, OrgSupplierInfoEntity supplierInfo) {
        List<GoodsSupplierDrugsVo> reagentList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subList)) {
            String sql = "SELECT t1.id, t1.drugs_name,t3.supplier_code" +
                    "  FROM hdi_goods_supplier_drugs t1 LEFT JOIN hdi_org_supplier_info t3 " +
                    "  ON t1.supplier_id = t3.id AND t3.del_flag = 0 WHERE t1.del_flag = 0 " +
                    " AND t1.drugs_name =? AND t1.supplier_id =? ";
            for (GoodsSupplierDrugsVo vo : subList) {
                List<GoodsSupplierDrugsVo> list = jdbcTemplate.query(sql, new Object[]{vo.getDrugsName(), supplierInfo.getId()}, new GoodsSupplierDrugsVo());
                if (!CollectionUtils.isEmpty(list)) {
                    vo.setId(list.get(0).getId());
                    reagentList.add(vo);
                }
            }
        }
        return reagentList;
    }

    /**
     * 更新药品
     *
     * @param list
     */
    @Override
    public List<GoodsSupplierDrugsVo> update(List<GoodsSupplierDrugsVo> list, OrgSupplierInfoEntity supplierInfo, Map<String, Long> map) {
        if (CollectionUtils.isNotEmpty(list)) {
            jdbcTemplate.execute(new ConnectionCallback() {
                @Override
                public Object doInConnection(Connection conn) throws SQLException {
                    String updateSql = "UPDATE hdi_goods_supplier_drugs set supplier_id =?,dept_id =?,status =?" +
                            ",del_flag =?,is_match = ?,data_source =?,is_upload =?,edit_time =?,edit_id =?," +
                            "drugs_name =?,factory_id =?,goods_unit =?,supply_unit =?,convert_unit =?,goods_nature =?" +
                            ",type_name= ?,approvals =?,store_way =? where id =?";
                    PreparedStatement ps =
                            conn.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS);
                    for (GoodsSupplierDrugsVo entity : list) {
                        ps.setLong(1, supplierInfo.getId());
                        ps.setLong(2, supplierInfo.getDeptId());
                        ps.setInt(3, SupplierDataSource.ERP.getKey());
                        ps.setInt(4, DelFlagEnum.NORMAL.getKey());
                        ps.setInt(5, IsMatchEnum.NO.getKey());
                        ps.setInt(6, DataSourceEnum.PORT.getKey());
                        ps.setInt(7, SupplierIsUploadEnum.YES.getKey());
                        ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                        ps.setLong(9, supplierInfo.getId());
                        ps.setString(10, entity.getDrugsName());
                        ps.setLong(11, map.get(entity.getFactoryName()));
                        ps.setString(12, entity.getGoodsUnit());
                        ps.setString(13, entity.getSupplyUnit());
                        ps.setString(14, entity.getConvertUnit());
                        ps.setInt(15, entity.getGoodsNature());
                        ps.setString(16, entity.getTypeName());
                        ps.setString(17, entity.getApprovals());
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
        }
        return list;
    }


    /**
     * 批量插入
     *
     * @param list
     */
    @Override
    public List<GoodsSupplierDrugsVo> insert(List<GoodsSupplierDrugsVo> list, OrgSupplierInfoEntity supplierInfo, Map<String, Long> map) {
        if (CollectionUtils.isNotEmpty(list)) {
            jdbcTemplate.execute(new ConnectionCallback() {
                @Override
                public Object doInConnection(Connection conn) throws SQLException {
                    String insertSql = "insert into hdi_goods_supplier_drugs(supplier_id,dept_id,status,del_flag,is_match,data_source," +
                            "is_upload,create_time,create_id,drugs_code,drugs_name,factory_id,goods_unit,supply_unit,convert_unit," +
                            "goods_nature,type_name,approvals,store_way)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement ps =
                            conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    for (GoodsSupplierDrugsVo entity : list) {
                        ps.setLong(1, supplierInfo.getId());
                        ps.setLong(2, supplierInfo.getDeptId());
                        ps.setInt(3, SupplierDataSource.ERP.getKey());
                        ps.setInt(4, DelFlagEnum.NORMAL.getKey());
                        ps.setInt(5, IsMatchEnum.NO.getKey());
                        ps.setInt(6, DataSourceEnum.PORT.getKey());
                        ps.setInt(7, SupplierIsUploadEnum.YES.getKey());
                        ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                        ps.setLong(9, supplierInfo.getId());
                        ps.setString(10, HttpUtils.doGet(seqCodeUrl + SequenceEnum.SUPPLIER_DRUGS_CODE.getKey()));
                        ps.setString(11, entity.getDrugsName());
                        ps.setLong(12, map.get(entity.getFactoryName()));
                        ps.setString(13, entity.getGoodsUnit());
                        ps.setString(14, entity.getSupplyUnit());
                        ps.setString(15, entity.getConvertUnit());
                        ps.setInt(16, entity.getGoodsNature());
                        ps.setString(17, entity.getTypeName());
                        ps.setString(18, entity.getApprovals());
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
        }
        return list;
    }
}
