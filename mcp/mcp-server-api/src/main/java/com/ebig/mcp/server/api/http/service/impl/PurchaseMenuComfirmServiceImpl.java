package com.ebig.mcp.server.api.http.service.impl;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.mcp.server.api.aspect.Log;
import com.ebig.mcp.server.api.http.dao.PurchaseMenuComfirmDao;
import com.ebig.mcp.server.api.http.service.IPurchaseMenuComfirmService;
import com.ebig.mcp.server.api.http.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
 * @date： 2019/10/22 15:01
 * @version： V1.0
 */
@Service
@Slf4j
public class PurchaseMenuComfirmServiceImpl implements IPurchaseMenuComfirmService {

    @Autowired
    private PurchaseMenuComfirmDao purchaseMenuComfirmDao;
    private final String comfirmStatus = "2";


    @Override
    @Log(title = "采购单确认模块",action = "采购单确认数据返回")
    public List<MasterDetailsCommonEntity> dealwithPurchaseComfirm(List<PurchaseComfirmVo> purchaseComfirmVoList) {
        List<MasterDetailsCommonEntity> masterDetailsCommonEntityList = new ArrayList<>();

        for (PurchaseComfirmVo purchaseComfirmVo : purchaseComfirmVoList) {
            //数据已确认
            if (purchaseComfirmVo.getPurchaseStatus().equals(comfirmStatus)){
                purchaseMenuComfirmDao.updatePurchaseMuneStatus(purchaseComfirmVo.getPurchaseStatus(),purchaseComfirmVo.getMasterId());
                Item items = purchaseMenuComfirmDao.findPurchaseMaster(purchaseComfirmVo.getMasterId(),purchaseComfirmVo.getSupplierCode());
                List<DetailItem> detailItemList = purchaseMenuComfirmDao.findPurchaseDetail(items.getPurchaseMasterId());

                MasterDetailsCommonEntity<PurchaseMenuVo, PurchaseMenuDetailVo> masterDetailsCommonEntity = new MasterDetailsCommonEntity<>();
                PurchaseMenuVo purchaseMenuVo = new PurchaseMenuVo();
                List<PurchaseMenuDetailVo> purchaseMenuDetailVoList = new ArrayList<>();
                itemsToPurchaseMenuVo(items,purchaseMenuVo);
                detailItemListToPurchaseMenuDetailVoList(detailItemList,purchaseMenuDetailVoList);
                masterDetailsCommonEntity.setMaster(purchaseMenuVo);
                masterDetailsCommonEntity.setDetails(purchaseMenuDetailVoList);
                masterDetailsCommonEntityList.add(masterDetailsCommonEntity);
            }else {
                Item items = purchaseMenuComfirmDao.findPurchaseMaster(purchaseComfirmVo.getMasterId(),purchaseComfirmVo.getSupplierCode());
                List<DetailItem> detailItemList = purchaseMenuComfirmDao.findPurchaseDetail(items.getPurchaseMasterId());

                MasterDetailsCommonEntity<PurchaseMenuVo, PurchaseMenuDetailVo> masterDetailsCommonEntity = new MasterDetailsCommonEntity<>();
                PurchaseMenuVo purchaseMenuVo = new PurchaseMenuVo();
                List<PurchaseMenuDetailVo> purchaseMenuDetailVoList = new ArrayList<>();
                itemsToPurchaseMenuVo(items,purchaseMenuVo);
                detailItemListToPurchaseMenuDetailVoList(detailItemList,purchaseMenuDetailVoList);
                masterDetailsCommonEntity.setMaster(purchaseMenuVo);
                masterDetailsCommonEntity.setDetails(purchaseMenuDetailVoList);
                masterDetailsCommonEntityList.add(masterDetailsCommonEntity);
            }

        }

        return masterDetailsCommonEntityList;
    }

    private void detailItemListToPurchaseMenuDetailVoList(List<DetailItem> detailItemList, List<PurchaseMenuDetailVo> purchaseMenuDetailVoList) {
        for (DetailItem detailItem : detailItemList) {
            PurchaseMenuDetailVo purchaseMenuDetailVo = new PurchaseMenuDetailVo();
            purchaseMenuDetailVo.setDetailId(detailItem.getPurchaseDetailId());
            purchaseMenuDetailVo.setGoodsSpecsCode(detailItem.getYhgoodsno());
            purchaseMenuDetailVo.setGoodsName(detailItem.getYhgoodstypename());
            purchaseMenuDetailVo.setGoodsUnit(detailItem.getHgoodsunit());
            purchaseMenuDetailVo.setGoodsAmount(detailItem.getHqty());
            purchaseMenuDetailVo.setGoodsPrice(detailItem.getHunitprice());
            purchaseMenuDetailVo.setDetailRemark(detailItem.getMemo());
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
    }
}