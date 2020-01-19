package com.ebig.hdi.modules.org.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.modules.org.param.OrgFactoryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 厂商信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:40
 */
public interface OrgFactoryInfoDao extends BaseMapper<OrgFactoryInfoEntity> {

	List<OrgFactoryInfoEntity> selectByFactoryName(@Param("factoryName") String factoryName);

	List<OrgFactoryInfoEntity> selectByCreditCode(@Param("creditCode") String creditCode);

    List<Map<String, Object>> getList(@Param("columns")String[] columns, @Param("queryParam")OrgFactoryParam queryParam);

	List<OrgFactoryInfoEntity> selectByCreditCodeOrFactoryName(@Param("factoryName") String factoryName,@Param("creditCode") String creditCode);
}
