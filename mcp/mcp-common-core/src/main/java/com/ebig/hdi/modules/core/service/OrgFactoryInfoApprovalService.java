package com.ebig.hdi.modules.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.utils.PageUtils;

import com.ebig.hdi.modules.core.param.OrgFactoryParam;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;


import java.util.List;
import java.util.Map;

/**
 * 厂商信息待审批记录表
 *
 * @author yzh
 * @date 2019-11-07 09:51:34
 */
public interface OrgFactoryInfoApprovalService extends IService<OrgFactoryInfoApprovalEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Description: 启用/关闭接口
     * @Param: [params]
     * @return: void
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/11
     */
    void toggle(Map<String, Object> params);

    void update(OrgFactoryInfoApprovalEntity orgFactoryInfoApproval,SysUserEntity entity);

    Map<String,Object> save(OrgFactoryInfoApprovalEntity orgFactoryInfoApproval,SysUserEntity entity);

    void delete(Long[] ids);

    List<OrgFactoryInfoApprovalEntity> queryByFactoryName(Map<String, Object> params);

    /**
     * @Description: 导出接口
     * @Param: [columns, queryParam]
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/11
     */
    List<Map<String, Object>> getList(String[] columns, OrgFactoryParam queryParam);

    Map<String,Object> checkStatus(Map<String,Object> params, SysUserEntity user);

    /**
     * @Description: 批量导入
     * @Param: [rows, userId, deptId]
     * @return: java.util.Map
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/8
     */ ;
    Map importData(String[][] rows, SysUserEntity sysUserEntity);
}

