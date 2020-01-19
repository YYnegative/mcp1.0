package com.ebig.mcp.server.api.http.dao.impl;

import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.mcp.server.api.http.dao.PurchaseMenuDao;
import com.ebig.mcp.server.api.http.vo.DetailItem;
import com.ebig.mcp.server.api.http.vo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/10/28 0028 下午 19:31
 * @version： V1.0
 */
@Repository
public class PurchaseMenuDaoImpl implements PurchaseMenuDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Item> findPurchaseMaster(String supplierCode, String beginTime, String endTime) {
        String sql = "SELECT pm.purchase_master_id, pm.purplanno, pm.sources_supplier_code," +
                "pm.sources_supplier_name ,pm.supplier_code,pm.supplier_id,pm.sources_hospital_name,pm.sources_hospital_code,pm.storehouse_no," +
                "pm.storehouse_name,sh.shaddress,pm.purplantime,pm.expecttime,pm.credate,pm.purchasestatus,pm.memo FROM " +
                "hdi_core_purchase_master pm " +
                "left join  hdi_core_storehouse sh on pm.storehouseid = sh.storehouseid  " +
                "where pm.purchasestatus=1 and pm.del_flag=0 and pm.supplier_code = ? and pm.credate between  ? and ? ";
        List<Item> masterItemList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Item>(Item.class), supplierCode,beginTime,endTime);
        return masterItemList;
    }

    @Override
    public List<DetailItem> findPurchaseDetail(Long purchaseMasterId, OrgSupplierInfoEntity supplierInfo) {
        String sql = "SELECT" +
                "  pd.purchase_detail_id," +
                "  pd.goodsclass," +
                "  pd.yhgoodsno," +
                "  pd.yhgoodstypename," +
                "  pd.hgoodsunit," +
                "  pd.hgoodsid," +
                "  pd.hgoodstypeid," +
                "  pd.hqty," +
                "  pd.hunitprice," +
                "  pd.memo," +
                "  gsm.supplier_goods_name," +
                "  gsm.supplier_goods_specs_code," +
                "  gsm.supplier_factory_name" +
                "  FROM" +
                "  hdi_core_purchase_detail pd" +
                "  LEFT JOIN view_hdi_goods_specs_match gsm" +
                "    ON pd.goodsclass = gsm.goods_type" +
                "    AND pd.hgoodsid = gsm.hospital_goods_id" +
                "    AND pd.hgoodstypeid = gsm.hospital_goods_specs_id" +
                "   where pd.purchase_master_id = ? and gsm.supplier_id= ? ";
        List<DetailItem> detailItemList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DetailItem.class),purchaseMasterId,supplierInfo.getId());
        return  detailItemList;
    }

    @Override
    public List<Map<String, Object>> getSupplyGoodsByHospitalGoods(DetailItem detailItem, Item purchaseMaster) {
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();
        sql.append("select supplier_goods_id,supplier_goods_code,supplier_goods_name,supplier_goods_specs_id, supplier_goods_specs_code,supplier_goods_specs_name,hospital_factory_name ");
        sql.append(" from view_hdi_goods_specs_match where supplier_id=? and goods_type=? and hospital_goods_id=? and hospital_goods_specs_id= ?");
        condition.add(purchaseMaster.getSupplierId());
        condition.add(detailItem.getGoodsclass());
        condition.add(detailItem.getHgoodsid());
        condition.add(detailItem.getHgoodstypeid());
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql.toString(), condition.toArray());
        return maps;
    }
}