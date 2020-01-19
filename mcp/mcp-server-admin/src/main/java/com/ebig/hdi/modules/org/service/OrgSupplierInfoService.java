package com.ebig.hdi.modules.org.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 供应商信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:38
 */
public interface OrgSupplierInfoService extends IService<OrgSupplierInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void save(OrgSupplierInfoEntity orgSupplierInfo);

	void update(OrgSupplierInfoEntity orgSupplierInfo);
	
	void bingding(OrgSupplierInfoEntity orgSupplierInfo);
	
	/**
	 * 判断子机构数量是否已超过集团机构数量
	 * @param orgSupplierInfo
	 * @return
	 */
	boolean judgeChildNumber(OrgSupplierInfoEntity orgSupplierInfo);
	
	List<OrgSupplierInfoEntity> queryTree(Map<String, Object> map);
	
	/**
	 * 通过用户id查询供应商id
	 * @param userId 用户id
	 * @return 供应商id
	 */
	Long selectIdByUserId(Long userId);
	
	/**
	 * 通过供应商名称查询供应商List
	 * @param params
	 * @return
	 */
	List<OrgSupplierInfoEntity> queryBySupplierName(Map<String, Object> params);
	
	/**
	 * 批量删除供应商
	 * @param ids
	 */
	void deleteBatchIds(List<Long> ids);
	
	/**
	 * 通过供应商id和医院id查询原供应商id
	 * @param supplierId
	 * @return
	 */
	String selectSourceSupplierId(Long supplierId,Long hospitalId);

	/**
	 * 根据部门id查询供应商信息
	 * @param deptId
	 * @return
     */
	List<OrgSupplierInfoEntity> selectByDeptId(Long deptId);
}

