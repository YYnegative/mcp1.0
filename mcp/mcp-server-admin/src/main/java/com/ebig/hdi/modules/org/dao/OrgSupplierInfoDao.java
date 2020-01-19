package com.ebig.hdi.modules.org.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;

/**
 * 供应商信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:38
 */
public interface OrgSupplierInfoDao extends BaseMapper<OrgSupplierInfoEntity> {
	
	List<OrgSupplierInfoEntity> listForPage(Pagination page,OrgSupplierInfoEntity osie);

	List<OrgSupplierInfoEntity> selectBySupplierName(@Param("supplierName") String supplierName);

	List<OrgSupplierInfoEntity> selectByCreditCode(@Param("creditCode") String creditCode);
	
	List<OrgSupplierInfoEntity> selectByParentIdAndStatus(@Param("parentId") Long parentId, @Param("status") Integer status);
	
	List<OrgSupplierInfoEntity> selectParent(@Param("value") String value);
	
	Long selectIdByUserId(@Param("userId") Long userId);
	
	String selectSourceSupplierId(@Param("supplierId")Long supplierId,@Param("hospitalId")Long hospitalId);
	
	List<Long> selectSubIdsById(@Param("id") Long id);

	/**
	 * 根据部门id查询供应商信息
	 * @param deptId
	 * @return
     */
	List<OrgSupplierInfoEntity> selectByDeptId(Long deptId);
}
