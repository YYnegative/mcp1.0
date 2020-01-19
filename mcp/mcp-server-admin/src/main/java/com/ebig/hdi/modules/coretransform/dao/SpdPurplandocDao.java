package com.ebig.hdi.modules.coretransform.dao;

import com.ebig.hdi.modules.coretransform.entity.SpdPurplandocEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-08-19 17:46:38
 */
@Component
public interface SpdPurplandocDao extends BaseMapper<SpdPurplandocEntity> {

    void BatchDelPurplandoc(@Param("set") Set<String> set);
}
