package com.ebig.hdi.modules.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.entity.CoreLotEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:14
 */
public interface CoreLotService extends IService<CoreLotEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
	PageUtils bedingungenQueryPage(Map<String, Object> params);
	
	void save(CoreLotEntity coreLot,SysUserEntity user);
	
	void updateLot(CoreLotEntity coreLot,SysUserEntity user);
	
	void deleteLot(List<CoreLotEntity> coreLotList,SysUserEntity user);
	
	CoreLotEntity saveLot(Long goodsid,Integer goodsclass,Long goodstypeid,String lotno,Long deptId,Date proddate,Date invadate);
	
	CoreLotEntity selectLot(Long lotid);

	/**
	 * 导入批号数据
	 * @param rows
	 * @param userId
	 * @param deptId
     * @return
     */
	Map<String,String> importData(String[][] rows, Long userId, Long deptId);
}

