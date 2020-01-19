package com.ebig.hdi.modules.refunds.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsDetailVO;

/**
 * 退货单明细信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
public interface RefundsDetailDao extends BaseMapper<RefundsDetailEntity> {
	
	List<RefundsDetailVO> listForPage(Pagination page,@Param("id")Long id);
	
	List<RefundsDetailVO> selectByMasterId(@Param("id")Long id);
	
}
