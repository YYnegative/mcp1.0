package com.ebig.hdi.modules.surgery.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.surgery.entity.SalesInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SalesInfoVO;

/**
 * 销售表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
public interface SalesInfoDao extends BaseMapper<SalesInfoEntity> {
	List<SalesInfoVO> listForPage(Pagination page,SalesInfoVO salesInfoVo);
}
