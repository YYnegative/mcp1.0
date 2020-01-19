package com.ebig.hdi.modules.surgery.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.surgery.entity.SalesInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SalesInfoVO;

import java.util.Map;

/**
 * 销售表
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
public interface SalesInfoService extends IService<SalesInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    void save(SalesInfoVO salesInfoVO);
    
    void update(SalesInfoVO salesInfoVO);
}

