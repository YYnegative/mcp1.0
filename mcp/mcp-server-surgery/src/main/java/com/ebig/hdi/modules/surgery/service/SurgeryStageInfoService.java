package com.ebig.hdi.modules.surgery.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageInfoVO;

/**
 * 手术跟台目录表
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:31
 */
public interface SurgeryStageInfoService extends IService<SurgeryStageInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    void save(SurgeryStageInfoVO stageInfoVO);
    
    void update(SurgeryStageInfoVO stageInfoVO);
    
    void submit(List<Long> idList);
    
    SurgeryStageInfoVO selectById(Long id);
}

