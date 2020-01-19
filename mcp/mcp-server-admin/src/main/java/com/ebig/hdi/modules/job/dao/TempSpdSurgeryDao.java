package com.ebig.hdi.modules.job.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.job.entity.TempSpdSurgeryEntity;
import com.ebig.hdi.modules.surgery.entity.SurgeryInfoEntity;

/**
 * 手术申请单
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-07-11 20:04:25
 */
public interface TempSpdSurgeryDao extends BaseMapper<TempSpdSurgeryEntity> {
	List<SurgeryInfoEntity> selectAll();
}
