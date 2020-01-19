package com.ebig.hdi.modules.coretransform.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.refunds.entity.RefundsMasterEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 退货单信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-10-21 15:10:37
 */
@Component
public interface RefundsMasterNewDao extends BaseMapper<RefundsMasterEntity> {

   RefundsMasterEntity selectByOrgdataid(@Param("orgdataid") String orgdataid);
}
