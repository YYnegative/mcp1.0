package com.ebig.hdi.modules.refunds.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyMasterEntity;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsApplyDetailVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsApplyMasterVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsMasterVO;

/**
 * 退货申请单信息
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
public interface RefundsApplyMasterService extends IService<RefundsApplyMasterEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    RefundsApplyMasterVO selectById(Long id);
    
    void save(RefundsApplyMasterVO ramVO);
    
    void update(RefundsApplyMasterVO ramVO);
    
    List<RefundsApplyDetailVO> selectToSave(Map<String,Object> params);
    
    List<String> selectRefundsApplyNo(Map<String,Object> params);
    
    RefundsMasterVO change(Long id);
    
    void determine(Long[] ids,Long userId);
    
}
    

