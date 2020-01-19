package com.ebig.mcp.server.api.http.dao.impl;

import com.ebig.mcp.server.api.http.dao.PurchaseMenuComfirmDao;
import com.ebig.mcp.server.api.http.vo.DetailItem;
import com.ebig.mcp.server.api.http.vo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/10/28 0028 下午 19:38
 * @version： V1.0
 */
@Repository
public class PurchaseMenuComfirmDaoImpl implements PurchaseMenuComfirmDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void updatePurchaseMuneStatus(String purchaseStatus, Long purchaseMasterId) {
        String sql = "update hdi_core_purchase_master set purchasestatus = ? where purchase_master_id = ? ";
        jdbcTemplate.update(sql,purchaseStatus,purchaseMasterId);
    }

    @Override
    public Item findPurchaseMaster(Long masterId, String supplierCode) {
        String sql = "SELECT pm.purchase_master_id, pm.purplanno, pm.sources_supplier_code," +
                "pm.sources_supplier_name ,pm.supplier_code,pm.sources_hospital_name,pm.storehouse_no," +
                "pm.storehouse_name,sh.shaddress,pm.purplantime,pm.expecttime,pm.credate,pm.purchasestatus,pm.memo FROM " +
                "hdi_core_purchase_master pm " +
                "join  hdi_core_storehouse sh on pm.storehouseid = sh.storehouseid  " +
                "where purchase_master_id = ? and supplier_code = ?";
        Item masterItem = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Item>(Item.class), masterId,supplierCode);
        return masterItem;
    }

    @Override
    public List<DetailItem> findPurchaseDetail(Long purchaseMasterId) {
        String sql = "SELECT pd.purchase_detail_id,pd.yhgoodsno,pd.yhgoodstypename,pd.hgoodsunit,pd.hqty,pd.hunitprice,pd.memo" +
                " FROM hdi_core_purchase_detail pd where purchase_master_id = ?";
        List<DetailItem> detailItemList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<DetailItem>(DetailItem.class), purchaseMasterId);
        return detailItemList;
    }
}