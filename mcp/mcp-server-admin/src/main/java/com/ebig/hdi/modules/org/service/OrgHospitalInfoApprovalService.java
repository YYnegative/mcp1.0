package com.ebig.hdi.modules.org.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.entity.OrgHospitalInfoApprovalEntity;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.org.param.OrgHospitalParam;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;


import java.util.List;
import java.util.Map;

/**
 * 医院信息待审批记录表
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-11 15:32:38
 */
public interface OrgHospitalInfoApprovalService extends IService<OrgHospitalInfoApprovalEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String,Object>  save(OrgHospitalInfoApprovalEntity hospitalInfoApprovalEntity, SysUserEntity user);

    void update(OrgHospitalInfoApprovalEntity hospitalInfoApprovalEntity ,SysUserEntity entity );

    void delete(Long[] ids);

    /**
     * @Description: 启用/关闭
     * @Param: [params]
     * @return: void
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/11
     */
    void toggle(Map<String, Object> params);

    /**
     * @Description: 批量导出
     * @Param: [columns, orgHospitalParam]
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/11
     */
    List<Map<String, Object>> getList(String[] columns, OrgHospitalParam orgHospitalParam);

    /**
     * @Description: 批量导入
     * @Param: [rows, sysUserEntity]
     * @return: java.util.Map
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/7
     */
    Map<String,String> importData(String[][] rows, SysUserEntity sysUserEntity);

    Map<String,Object>  checkStatus(Map<String,Object> params, SysUserEntity user);
}

