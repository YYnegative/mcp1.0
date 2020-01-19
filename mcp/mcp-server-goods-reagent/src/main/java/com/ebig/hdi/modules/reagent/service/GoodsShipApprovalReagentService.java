package com.ebig.hdi.modules.reagent.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wenchao
 * @email 280310627@qq.com
 * @date 2019-12-09 10:30:25
 */
public interface GoodsShipApprovalReagentService extends IService<UnicodeGoodsShipApprovalEntity> {

    PageUtils queryPageHospitalGoods(Map<String, Object> params);

    PageUtils queryPageSupplierGoods(Map<String, Object> params);
	
    void updateHospitalPgoodsId(SysUserEntity entity,UnicodeGoodsShipApprovalEntity unicodeGoodsShipEntity);
    
    void updateSupplierPgoodsId(SysUserEntity sysUserEntity,UnicodeGoodsShipApprovalEntity unicodeGoodsShipEntity);

    PageUtils pGoodsList(Map<String, Object> params);

    List<UnicodeGoodsShipApprovalEntity> listHospitalMatchGoods(Long torgId, Long pgoodsId, Long pspecsId);

    List<UnicodeGoodsShipApprovalEntity> listSupplierMatchGoods(Long torgId, Long pgoodsId, Long pspecsId);


    Map<String, String> checkStatus(Map<String, String> params, SysUserEntity user);

    Map<String, String> hospitalCheckstatus(Map<String, String> params, SysUserEntity user);
}

