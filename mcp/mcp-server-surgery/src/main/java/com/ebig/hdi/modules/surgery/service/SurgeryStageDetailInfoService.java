package com.ebig.hdi.modules.surgery.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageDetailInfoVO;

import java.util.List;
import java.util.Map;

/**
 * 手术跟台目录明细表
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
public interface SurgeryStageDetailInfoService extends IService<SurgeryStageDetailInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<SurgeryStageDetailInfoVO> selectToSave(Map<String,Object> params);
    
    List<SurgeryStageDetailInfoVO> selectBySurgeryStageId(Long id);
}

