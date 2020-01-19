package com.ebig.hdi.modules.org.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.modules.org.param.OrgFactoryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 厂商信息待审批记录表
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-07 09:51:34
 */
public interface OrgFactoryInfoApprovalDao extends BaseMapper<OrgFactoryInfoApprovalEntity> {

    List<OrgFactoryInfoApprovalEntity> selectByFactoryName(String factoryName);

    List<OrgFactoryInfoApprovalEntity> selectByCreditCode(String creditCode);

    /**
     * @Description: 导出
     * @Param: [columns, queryParam]
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/18
     */
    List<Map<String,Object>> getList(String[] columns, OrgFactoryParam queryParam);


    List<OrgFactoryInfoEntity> selectListByFactoryCode(String factoryCode);

    void updateFactoryInfoById(@Param("id") Long id,@Param("status") Integer status);

    List<OrgFactoryInfoEntity> selectFactoryInfoById(Long id);
}
