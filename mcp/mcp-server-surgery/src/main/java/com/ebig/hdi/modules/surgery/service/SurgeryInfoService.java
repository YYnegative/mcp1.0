package com.ebig.hdi.modules.surgery.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.surgery.entity.SurgeryInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryInfoVO;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageInfoVO;

/**
 * 手术信息表
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
public interface SurgeryInfoService extends IService<SurgeryInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    SurgeryInfoVO selectById(Long id);
    
    List<SurgeryInfoEntity> querySurgeryNo(Map<String, Object> params);
    
    SurgeryStageInfoVO createStage(Long id);
}

