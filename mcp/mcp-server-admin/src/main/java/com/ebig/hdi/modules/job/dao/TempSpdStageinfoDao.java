package com.ebig.hdi.modules.job.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.job.entity.TempSpdStageinfoEntity;

/**
 * SpdStageInfo
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-07-12 10:34:59
 */
public interface TempSpdStageinfoDao extends BaseMapper<TempSpdStageinfoEntity> {
	
	public List<TempSpdStageinfoEntity> selectAll();
}
