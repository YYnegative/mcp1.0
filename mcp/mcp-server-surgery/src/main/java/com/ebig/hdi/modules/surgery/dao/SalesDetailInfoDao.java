package com.ebig.hdi.modules.surgery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.surgery.entity.SalesDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SalesDetailInfoVO;

/**
 * 销售明细表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
public interface SalesDetailInfoDao extends BaseMapper<SalesDetailInfoEntity> {
	
	List<SalesDetailInfoVO> listForPage(Pagination page,@Param("id")Long id);
}
