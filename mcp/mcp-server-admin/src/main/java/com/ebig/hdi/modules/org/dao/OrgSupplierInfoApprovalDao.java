package com.ebig.hdi.modules.org.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoApprovalEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.org.param.OrgSupplierInfoParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 供应商信息待审批表
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-15 09:24:04
 */
public interface OrgSupplierInfoApprovalDao extends BaseMapper<OrgSupplierInfoApprovalEntity> {

    List<OrgSupplierInfoApprovalEntity> listForPage(Pagination page, OrgSupplierInfoApprovalEntity osie);

    List<OrgSupplierInfoApprovalEntity> selectBySupplierName(@Param("supplierName") String supplierName);

    List<OrgSupplierInfoApprovalEntity> selectByCreditCode(@Param("creditCode") String creditCode);

    List<OrgSupplierInfoApprovalEntity> selectByParentIdAndStatus(@Param("parentId") Long parentId, @Param("status") Integer status);

    List<OrgSupplierInfoApprovalEntity> selectParent(@Param("value") String value);

    Long selectIdByUserId(@Param("userId") Long userId);

    String selectSourceSupplierId(@Param("supplierId")Long supplierId,@Param("hospitalId")Long hospitalId);

    List<Long> selectSubIdsById(@Param("id") Long id);

    /**
     * 根据部门id查询供应商信息
     * @param deptId
     * @return
     */
    List<OrgSupplierInfoApprovalEntity> selectByDeptId(Long deptId);


    OrgSupplierInfoEntity selectBySupplierCode(String supplierCode);

    /**
     * @Description:导出查询
     * @Param: [columns, orgSupplierInfoParam]
     * @return: java.util.List<Map<String,Object>>
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/13
     */
    List<Map<String, Object>>getList(@Param("columns")String[] columns, @Param("queryParam") OrgSupplierInfoParam orgSupplierInfoParam);



    Integer selectIfExist(Long id);
}
