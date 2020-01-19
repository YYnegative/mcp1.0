package com.ebig.hdi.modules.drugs.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wenchao
 * @email 280310627@qq.com
 * @date 2019-12-06 15:28:23
 */
public interface GoodsShipApprovalDrugsService extends IService<UnicodeGoodsShipApprovalEntity> {

    PageUtils queryPageHospitalGoods(Map<String, Object> params);

    PageUtils queryPageSupplierGoods(Map<String, Object> params);
    
    void updateHospitalPgoodsId(SysUserEntity sysUserEntity, UnicodeGoodsShipApprovalEntity unicodeGoodsShipEntity);
    
    void updateSupplierPgoodsId(SysUserEntity sysUserEntity,UnicodeGoodsShipApprovalEntity unicodeGoodsShipEntity);
    
    PageUtils pGoodsList(Map<String, Object> params);

    List<UnicodeGoodsShipApprovalEntity> listHospitalMatchGoods(Long torgId, Long pGoodsId, Long pSpecsId);

    List<UnicodeGoodsShipApprovalEntity> listSupplierMatchGoods(Long torgId, Long pGoodsId, Long pSpecsId);

    Map<String, String> checkStatus(Map<String, String> params, SysUserEntity user);

    /**
    * @Description:医院药品审核
    * @Author: ZhengHaiWen
    * @Date: 2019/12/29
    */
    Map<String, String> hospitalCheckstatus(Map<String, String> params, SysUserEntity user);
}

