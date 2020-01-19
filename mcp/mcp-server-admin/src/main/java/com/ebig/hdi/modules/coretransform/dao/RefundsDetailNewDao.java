package com.ebig.hdi.modules.coretransform.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 退货单明细信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-10-21 15:10:37
 */
@Component
public interface RefundsDetailNewDao extends BaseMapper<RefundsDetailEntity> {

    RefundsDetailEntity  selectByOrgdatadtlid(@Param("orgdatadtlid") String orgdatadtlid);
}
