package com.ebig.hdi.modules.sys.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.sys.entity.SysSequenceEntity;

/**
 * 系统序列
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-24 13:06:34
 */
public interface SysSequenceDao extends BaseMapper<SysSequenceEntity> {

	SysSequenceEntity selectBySeqCode(@Param("seqCode") String seqCode);
	
}
