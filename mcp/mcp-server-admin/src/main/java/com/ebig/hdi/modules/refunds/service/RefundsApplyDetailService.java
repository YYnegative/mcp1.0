package com.ebig.hdi.modules.refunds.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyDetailEntity;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsApplyDetailVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsDetailVO;

/**
 * 退货申请单明细信息
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
public interface RefundsApplyDetailService extends IService<RefundsApplyDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<RefundsApplyDetailVO> selectByMasterId(@Param("id") Long id);
    
    /**
     * 通过退货验收单id生成退货单详情信息
     * @param id
     * @return
     */
    List<RefundsDetailVO> changeDetail(Long id);
    
    List<RefundsApplyDetailVO> selectByApplyNo(String no);
    
}

