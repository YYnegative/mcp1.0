package com.ebig.hdi.modules.org.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoApprovalEntity;
import com.ebig.hdi.modules.org.param.OrgSupplierInfoParam;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 供应商信息待审批表
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-15 09:24:04
 */
public interface OrgSupplierInfoApprovalService extends IService<OrgSupplierInfoApprovalEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String,Object> save(OrgSupplierInfoApprovalEntity orgSupplierInfo,SysUserEntity entity);

    void update(OrgSupplierInfoApprovalEntity orgSupplierInfo, SysUserEntity entity);

    void bingding(OrgSupplierInfoApprovalEntity orgSupplierInfo);

    /**
     * 判断子机构数量是否已超过集团机构数量
     * @param orgSupplierInfo
     * @return
     */
    boolean judgeChildNumber(OrgSupplierInfoApprovalEntity orgSupplierInfo);

    List<OrgSupplierInfoApprovalEntity> queryTree(Map<String, Object> map);

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
    List<OrgSupplierInfoApprovalEntity> queryBySupplierName(Map<String, Object> params);

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
    List<OrgSupplierInfoApprovalEntity> selectByDeptId(Long deptId);

    Map<String,Object> checkStatus(Map<String, Object> params,SysUserEntity entity);


    /**
     * @Description: 批量导入
     * @Param: [rows, SysUserEntity]
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/9
     */
    Map importData(String[][] rows, SysUserEntity sysUserEntity);

    /**
     * @Description:批量导出
     * @Param: [columns, orgSupplierInfoParam]
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/12
     */
    List<Map<String, Object>> getList(String[] columns, OrgSupplierInfoParam orgSupplierInfoParam);

    /**
     * @Description: 启用/关闭
     * @Param: [params]
     * @return: void
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/12
     */
    void toggle(Map<String, Object> params);

}

