package com.ebig.hdi.modules.coretransform.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.coretransform.entity.SpdPurbackdtlEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 采购退货明细
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-10-21 15:10:37
 */
@Component
public interface SpdPurbackdtlDao extends BaseMapper<SpdPurbackdtlEntity> {

    /**
     * 获取1000条数据
     * @param row
     * @return
     */
    List<SpdPurbackdtlEntity> queryList(@Param("row") int row);

    Map<String,Object> getSupplyMasterIdAndSupplyDetailId(@Param("purbackdtlid") String purbackdtlid);
}
