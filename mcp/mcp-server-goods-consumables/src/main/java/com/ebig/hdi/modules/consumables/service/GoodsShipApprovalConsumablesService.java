package com.ebig.hdi.modules.consumables.service;

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
 * @date 2019-12-06 15:18:03
 */
public interface GoodsShipApprovalConsumablesService extends IService<UnicodeGoodsShipApprovalEntity> {
    /**
     * 查询医院耗材匹对记录
     * @param params
     * @return
     */
    PageUtils queryPageHospitalGoods(Map<String, Object> params);

    /**
     * 查询供应商耗材匹对记录
     * @param params
     * @return
     */
    PageUtils queryPageSupplierGoods(Map<String, Object> params);

    /**
     * 更新医院匹对记录
     * @param unicodeGoodsShipEntity
     */
    void updateHospitalPgoodsId(SysUserEntity userEntity,UnicodeGoodsShipApprovalEntity unicodeGoodsShipEntity);

    /**
    * 更新供应商匹对记录
    * @param unicodeGoodsShipEntity
    */
    void updateSupplierPgoodsId(SysUserEntity userEntity,UnicodeGoodsShipApprovalEntity unicodeGoodsShipEntity);
    
    PageUtils pGoodsList(Map<String, Object> params);


    List<UnicodeGoodsShipApprovalEntity>  listHospitalMatchGoods(Long torgId,Long pgoodsId,Long pspecsId,Long papprovalId);

    List<UnicodeGoodsShipApprovalEntity> selectListByColumn(Integer torgType,Integer tgoodsType,Long tgoodsId,Long tspecsId);


    Map<String, String> checkStatus(Map<String, String> params, SysUserEntity user);

    List<UnicodeGoodsShipApprovalEntity> listSupplierMatchGoods(Long torgId, Long pgoodsId, Long pspecsId, Long papprovalId);

    /**
     * @Description:医院耗材审批
     * @Author: ZhengHaiWen
     * @Date: 2019/12/27
     */
    Map<String, String> hospitalCheckstatus(Map<String, String> params, SysUserEntity user);
}

