package com.ebig.hdi.modules.org.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.common.entity.OrgHospitalInfoApprovalEntity;
import com.ebig.hdi.modules.org.param.OrgHospitalParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 医院信息待审批记录表
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-11 15:32:38
 */
public interface OrgHospitalInfoApprovalDao extends BaseMapper<OrgHospitalInfoApprovalEntity> {

    /**
     * 根据医院名查询
     * @param hospitalName 待审批表是否已存在医院名
     * @return
     */
    List<OrgHospitalInfoApprovalEntity> selectByHospitalName(@Param("hospitalName") String hospitalName);

    /**
     * 根据信用代码查询
     * @param creditCode 待审批表是否已存在该信用代码
     * @return
     */
    List<OrgHospitalInfoApprovalEntity> selectByCreditCode(@Param("creditCode")String creditCode);

    /**
     * @Description: 导出查询
     * @Param: [columns, orgHospitalParam]
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/20
     */
    List<Map<String, Object>> getList(@Param("columns")String[] columns, @Param("queryParam") OrgHospitalParam orgHospitalParam);


    Integer selectIfExist(@Param("id")Long id);
}
