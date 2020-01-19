package com.ebig.hdi.modules.job.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.coretransform.entity.TempPubCompanyEntity;

/**
 * 临时机构关系表
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-06-12 02:40:51
 */
public interface TempPubCompanyDao extends BaseMapper<TempPubCompanyEntity> {

	List<TempPubCompanyEntity> selectByTime(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
}
