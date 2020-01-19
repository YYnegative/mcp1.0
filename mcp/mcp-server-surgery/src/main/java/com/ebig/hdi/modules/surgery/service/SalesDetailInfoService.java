package com.ebig.hdi.modules.surgery.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.surgery.entity.SalesDetailInfoEntity;

/**
 * 销售明细表
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
public interface SalesDetailInfoService extends IService<SalesDetailInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

