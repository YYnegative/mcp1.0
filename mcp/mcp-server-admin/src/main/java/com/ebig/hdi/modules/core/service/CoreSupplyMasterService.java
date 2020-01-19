package com.ebig.hdi.modules.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.entity.CoreAcceptDetailEntity;
import com.ebig.hdi.modules.core.entity.CoreAcceptMasterEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyDetailEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyMasterEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-26 18:26:03
 */
public interface CoreSupplyMasterService extends IService<CoreSupplyMasterEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
	PageUtils bedingungenQueryPage(Map<String, Object> params);
	
	void updateSupplyStatus(CoreSupplyMasterEntity coreSupplyMaster,SysUserEntity user);
	
	void deleteMaster(List<CoreSupplyMasterEntity> listEntity,SysUserEntity user);
	
	void save(MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterdetailsCommonEntity,Long deptId, Long userId, String username,SysUserEntity user) throws IOException;
    
	MasterDetailsCommonEntity<CoreAcceptMasterEntity, CoreAcceptDetailEntity> supplyAcceptList(CoreSupplyMasterEntity coreSupplyMaster,SysUserEntity user);
	
	MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> editList(CoreSupplyMasterEntity coreSupplyMaster,SysUserEntity user);
	
	void editSave(MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> entity, Long userId, String username,Long deptId);
	
	void supplyAcceptSave( MasterDetailsCommonEntity<CoreAcceptMasterEntity, CoreAcceptDetailEntity> masterdetailsCommonEntity,Long deptId, Long userId, String username);

	Long supplyLabelSave(MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterdetailsCommonEntity,Long deptId, Long userId, String username) throws IOException;
	
	MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> supplyLabelList(CoreSupplyMasterEntity coreSupplyMaster,SysUserEntity user);
	
	//提交医院
	void submitToHospital(CoreSupplyMasterEntity entity,SysUserEntity user);
	
	//HDI转换用  生成转换的供货主单
	void saveMaster(CoreSupplyMasterEntity coreSupplyMasterEntity);
	
	//根据原数据标识 查询是否存在此主单
	CoreSupplyMasterEntity selectByOrgdataid(String orgdataid);
	
    /**
     *功能：获取打印的批次码PDF文件
     *@author frink
     */
	void getBatchCodePdf(HttpServletResponse response, List<CoreSupplyDetailEntity> entityList,Long userId) throws Exception;

	void updateSupplyStatus(Long supplyMasterId);

	Map importData(String[][] rows, Long userId, Long deptId) throws IOException;

	/**
	 * 根据状态查询指定数量的供货主单
	 * @param params
	 * @return
	 */
	List<CoreSupplyMasterEntity> selectListByMap(Map<String, Object> params);

	/**
	 *下发供货单数据
	 * @param entity
     */
	 void sendCoreSupplyData(CoreSupplyMasterEntity entity);

	/**
	 * 校验供货单编号是否存在
	 * @param supplyNo
	 * @return
     */
	 boolean existSupplyNo(String supplyNo);

	Map<String, Object> autoSupplyLabelSave(MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterdetailsCommonEntity, Long deptId, Long userId, String username) throws IOException;


	 /**
	 * @Description:  供货单批量导出
	 * @Author: ZhengHaiWen
	 * @Date: 2020/1/2
	 */
    List<Map<String, Object>> getList(HashMap<String, Object> map);
}

