package com.ebig.mcp.server.api.http.service.impl;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.mcp.server.api.aspect.Log;
import com.ebig.mcp.server.api.http.dao.PurchaseMenuDao;
import com.ebig.mcp.server.api.http.service.IPurchaseMenuService;
import com.ebig.mcp.server.api.http.service.OrgSupplierService;
import com.ebig.mcp.server.api.http.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @date： 2019/10/21 14:26
 * @version： V1.0
 */
@Service
@Slf4j
public class PurchaseMenuServiceImpl implements IPurchaseMenuService {

    @Autowired
    private PurchaseMenuDao purchaseMenuDao;
    @Autowired
    private OrgSupplierService supplierService;


    @Override
    @Log( title = "采购单数据",action = "采购单数据下发")
    public List<MasterDetailsCommonEntity> dealwithPurchaseMenu(List<PurchaseMenuCriteria> purchaseMenuCriteriaList) {
        List<MasterDetailsCommonEntity> masterDetailsCommonEntityList = new ArrayList<>();
        for (PurchaseMenuCriteria purchaseMenuCriteria : purchaseMenuCriteriaList) {
            String supplierCode = purchaseMenuCriteria.getSupplierCode();
            OrgSupplierInfoEntity supplierInfo = null;
            try {
                supplierInfo = supplierService.getSupplierInfo(supplierCode);
            } catch (Exception e) {
               throw  new HdiException("没有此编码的供应商信息");
            }
            String beginTime = purchaseMenuCriteria.getBeginTime();
            String endTime = purchaseMenuCriteria.getEndTime();
            List<Item> masterItemList = purchaseMenuDao.findPurchaseMaster(supplierCode, beginTime, endTime);
            for (Item item : masterItemList) {
                List<DetailItem> detailItemList = purchaseMenuDao.findPurchaseDetail(item.getPurchaseMasterId(),supplierInfo);
                MasterDetailsCommonEntity<PurchaseMenuVo, PurchaseMenuDetailVo> masterDetailsCommonEntity = new MasterDetailsCommonEntity<>();
                PurchaseMenuVo purchaseMenuVo = new PurchaseMenuVo();
                List<PurchaseMenuDetailVo> purchaseMenuDetailVoList = new ArrayList<>();
                itemsToPurchaseMenuVo(item,purchaseMenuVo);
                detailItemListToPurchaseMenuDetailVoList(detailItemList,purchaseMenuDetailVoList,item);
                masterDetailsCommonEntity.setMaster(purchaseMenuVo);
                masterDetailsCommonEntity.setDetails(purchaseMenuDetailVoList);
                masterDetailsCommonEntityList.add(masterDetailsCommonEntity);
            }
        }

        return masterDetailsCommonEntityList;
    }


    private void detailItemListToPurchaseMenuDetailVoList(List<DetailItem> detailItemList, List<PurchaseMenuDetailVo> purchaseMenuDetailVoList,Item purchaseMaster) {
        for (DetailItem detailItem : detailItemList) {
            PurchaseMenuDetailVo purchaseMenuDetailVo = new PurchaseMenuDetailVo();
            purchaseMenuDetailVo.setDetailId(detailItem.getPurchaseDetailId());
            purchaseMenuDetailVo.setGoodsSpecsCode(detailItem.getSupplierGoodsSpecsCode());
            purchaseMenuDetailVo.setGoodsName(detailItem.getSupplierGoodsName());
            purchaseMenuDetailVo.setGoodsUnit(detailItem.getHgoodsunit());
            purchaseMenuDetailVo.setGoodsAmount(detailItem.getHqty());
            purchaseMenuDetailVo.setGoodsPrice(detailItem.getHunitprice());
            purchaseMenuDetailVo.setDetailRemark(detailItem.getMemo());
            purchaseMenuDetailVo.setFactoryName(detailItem.getSupplierFactoryName());
            purchaseMenuDetailVoList.add(purchaseMenuDetailVo);
        }
    }
    private void itemsToPurchaseMenuVo(Item items, PurchaseMenuVo purchaseMenuVo) {
        purchaseMenuVo.setMasterId(items.getPurchaseMasterId());
        purchaseMenuVo.setPurplanNo(items.getPurplanno());
        purchaseMenuVo.setSupplierCode(items.getSupplierCode());
        purchaseMenuVo.setHospitalName(items.getSourcesHospitalName());
        purchaseMenuVo.setStoreHouseAddress(items.getShaddress());
        purchaseMenuVo.setPurplanTime(items.getPurplantime());
        purchaseMenuVo.setExpectTime(items.getExpecttime());
        purchaseMenuVo.setCreateTime(items.getCredate());
        purchaseMenuVo.setPurchaseStatus(items.getPurchasestatus());
        purchaseMenuVo.setMasterRemark(items.getMemo());
        purchaseMenuVo.setHospitalCode(items.getSourcesHospitalCode());
    }

}