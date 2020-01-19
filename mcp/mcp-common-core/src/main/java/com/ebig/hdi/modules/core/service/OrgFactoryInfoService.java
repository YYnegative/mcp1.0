package com.ebig.hdi.modules.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.param.OrgFactoryParam;


import java.util.List;
import java.util.Map;

/**
 * 厂商信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:40
 */
public interface OrgFactoryInfoService extends IService<OrgFactoryInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void save(OrgFactoryInfoEntity orgFactoryInfoApproval);

	void update(OrgFactoryInfoEntity orgFactoryInfo);

	List<OrgFactoryInfoEntity> queryByFactoryName(Map<String, Object> params);

	void delete(Long[] ids);

    List<Map<String, Object>> getList(String[] columns, OrgFactoryParam queryParam);

    void toggle(Map<String, Object> params);

	/**
	 * @Description: 批量导入
	 * @Param: [rows, userId, deptId]
	 * @return: java.util.Map
	 * @Author: ZhengHaiWen
	 * @Modified :
	 * @Date: 2019/11/8
	 */ ;
	 Map importData(String[][] rows, Long userId, Long deptId);

	void updateByFactoryInfoApproval(OrgFactoryInfoApprovalEntity orgFactoryInfoApproval);

	void insertByFactoryInfoApproval(OrgFactoryInfoApprovalEntity orgFactoryInfoApproval);
}

