package com.ebig.mcp.server.api.http.dao;

import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.mcp.server.api.http.vo.DetailItem;
import com.ebig.mcp.server.api.http.vo.Item;

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
 * @Date： 2019/10/28 0028 下午 19:27
 * @version： V1.0
 */
public interface PurchaseMenuDao {

    List<Item> findPurchaseMaster(String supplierCode,String  beginTime,String  endTime);

    List<DetailItem> findPurchaseDetail(Long purchaseMasterId,OrgSupplierInfoEntity supplierInfo);

    List<Map<String,Object>> getSupplyGoodsByHospitalGoods(DetailItem detailItem,Item purchaseMaster);


}