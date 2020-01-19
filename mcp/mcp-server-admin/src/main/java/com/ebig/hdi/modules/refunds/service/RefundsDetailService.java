package com.ebig.hdi.modules.refunds.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsDetailVO;

/**
 * 退货单明细信息
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
public interface RefundsDetailService extends IService<RefundsDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<RefundsDetailVO> selectByMasterId(Long id);
    
    List<RefundsDetailVO> selectByApplyNo(String no);


}

