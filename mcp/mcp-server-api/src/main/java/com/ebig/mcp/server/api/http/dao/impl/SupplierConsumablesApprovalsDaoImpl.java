package com.ebig.mcp.server.api.http.dao.impl;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.entity.GoodsSupplierConsumablesApprovalsEntity;
import com.ebig.mcp.server.api.http.dao.SupplierConsumablesApprovalsDao;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierConsumablesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 供应商耗材批文持久化实现类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
@Repository
public class SupplierConsumablesApprovalsDaoImpl implements SupplierConsumablesApprovalsDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取不存在的批文数据
     *
     * @param updateConsumables
     */
    @Override
    public List<GoodsSupplierConsumablesVo> getApprovals(List<GoodsSupplierConsumablesVo> updateConsumables) {
        List<GoodsSupplierConsumablesVo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(updateConsumables)) {

            String sql = "SELECT approvals" +
                    "  FROM hdi_goods_supplier_consumables_approvals where consumables_id =? and approvals =? ";
            for (GoodsSupplierConsumablesVo vo : updateConsumables) {
                List<String> approvals = jdbcTemplate.queryForList(sql, new Object[]{vo.getId(), vo.getApprovals()}, String.class);
                if (CollectionUtils.isEmpty(approvals)) {
                    list.add(vo);
                }
            }
        }
        return list;
    }

    /**
     * 批量插入批文
     *
     * @param approvalsList
     */
    @Override
    public void insertBatchApprovals(final List<GoodsSupplierConsumablesApprovalsEntity> approvalsList) {
        if (CollectionUtils.isNotEmpty(approvalsList)) {
            jdbcTemplate.execute(new ConnectionCallback() {
                @Override
                public Object doInConnection(Connection conn) throws SQLException {
                    String insertSql = "insert into hdi_goods_supplier_consumables_approvals(" +
                            "consumables_id,approvals,status,create_id,create_time)" +
                            "values(?,?,?,?,?)";
                    PreparedStatement ps =
                            conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    for (GoodsSupplierConsumablesApprovalsEntity entity : approvalsList) {
                        ps.setLong(1, entity.getConsumablesId());
                        ps.setString(2, entity.getApprovals());
                        ps.setInt(3, entity.getStatus());
                        ps.setLong(4, entity.getCreateId());
                        ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    return null;
                }

            });
        }
    }
}
