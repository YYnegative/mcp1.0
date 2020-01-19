package com.ebig.hdi.modules.coretransform.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.coretransform.entity.SpdPurbackEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 采购退货单
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-10-21 15:10:37
 */
@Component
public interface SpdPurbackDao extends BaseMapper<SpdPurbackEntity> {

     void BatchDelPurBack(@Param("set")Set<String> set);
}
