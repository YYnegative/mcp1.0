package com.ebig.hdi.modules.core.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.entity.CorePurchaseDetailEntity;
import com.ebig.hdi.modules.core.entity.CorePurchaseMasterEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyDetailEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyMasterEntity;
import com.ebig.hdi.modules.core.param.CorePurchaseParam;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-27 11:12:34
 */
public interface CorePurchaseMasterService extends IService<CorePurchaseMasterEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    PageUtils bedingungenQueryPage(Map<String, Object> params);
    
    void updatePurchaseStatus(CorePurchaseMasterEntity corePurchaseMasterEntity,SysUserEntity user);
    
    void save(MasterDetailsCommonEntity<CorePurchaseMasterEntity, CorePurchaseDetailEntity> masterdetailsCommonEntity,Long deptId,Long userId,String username,SysUserEntity user);

    void deleteMaster(List<CorePurchaseMasterEntity> listEntity,SysUserEntity user);	
    
    MasterDetailsCommonEntity<CorePurchaseMasterEntity, CorePurchaseDetailEntity> editList(CorePurchaseMasterEntity entity,SysUserEntity user);
   
    void edit(MasterDetailsCommonEntity<CorePurchaseMasterEntity, CorePurchaseDetailEntity> masterdetailsCommonEntity,Long deptId,Long userId,String username);
    
    MasterDetailsCommonEntity<CoreSupplyMasterEntity,CorePurchaseDetailEntity> supplyList(CorePurchaseMasterEntity corePurchaseMaster,SysUserEntity user);

    void saveSupply(MasterDetailsCommonEntity<CoreSupplyMasterEntity,CoreSupplyDetailEntity> masterdetailsCommonEntity,Long deptId,Long userId,String username) throws IOException;
    
    //HDI转换用   查询是否存在此原始数据标识对应的主单
    CorePurchaseMasterEntity selectByOrgdataid(String orgdataid);
    
    //HDI转换用   生成主单
    void saveCorePurchaseMaster(CorePurchaseMasterEntity entity);

	void updatePurchasestatus(Long purchaseMasterId);

	List<CorePurchaseMasterEntity> getInfoByPurplanno(String purplanno, Long supplierId);

    /**
     * 查询指定列的数据集合
     * @param columns  列名
     * @param queryParam 查询条件
     * @return
     */
    List<Map<String,Object>>  getList(String[] columns, CorePurchaseParam queryParam);

    Map<String,String> importData(String[][] rows, SysUserEntity user);
}

