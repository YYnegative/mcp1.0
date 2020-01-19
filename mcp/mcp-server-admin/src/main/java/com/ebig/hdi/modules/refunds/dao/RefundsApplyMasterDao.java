package com.ebig.hdi.modules.refunds.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
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
public interface RefundsApplyMasterDao extends BaseMapper<RefundsApplyMasterEntity> {
	
	List<RefundsApplyMasterVO> listForPage(Pagination page,RefundsApplyMasterVO ramVO);
	
	RefundsApplyMasterVO selectRefundsApplyById(@Param("id") Long id);
	
	List<RefundsApplyDetailVO> selectToSave(@Param("params") Map<String,Object> params);
	
	List<String> selectRefundsApplyNo(@Param("params") Map<String,Object> params);
	
	RefundsMasterVO changeMaster(@Param("id") Long id);
}
