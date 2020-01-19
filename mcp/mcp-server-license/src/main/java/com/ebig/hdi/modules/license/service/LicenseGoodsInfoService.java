package com.ebig.hdi.modules.license.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.license.entity.LicenseFactoryInfoEntity;
import com.ebig.hdi.modules.license.entity.LicenseGoodsInfoEntity;
import com.ebig.hdi.modules.license.entity.vo.LicenseGoodsInfoVO;

/**
 * 商品证照信息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
public interface LicenseGoodsInfoService extends IService<LicenseGoodsInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    LicenseGoodsInfoVO selectById(Long id);
    
    /**
     * 根据供应商id查询所有商品
     * @param supplierId
     * @return
     */
    List<Map<String,Object>> allGoods(Long supplierId);
    
    List<LicenseGoodsInfoVO> details(Long id);
    
    void save(LicenseGoodsInfoVO licenseGoodsInfoVO);
    
    void update(LicenseGoodsInfoVO licenseGoodsInfoVO);
    
    void replace(LicenseGoodsInfoVO licenseGoodsInfoVO);
    
    PageUtils examineInfo(Map<String, Object> params);

	void examine(Map<String, Object> params);
	
	List<LicenseGoodsInfoEntity> selectBySupplierIdAndTime(Long supplierId, ScheduleJobEntity scheduleJob);
}

