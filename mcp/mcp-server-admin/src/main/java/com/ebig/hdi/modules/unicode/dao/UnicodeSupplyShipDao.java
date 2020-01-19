package com.ebig.hdi.modules.unicode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import com.ebig.hdi.modules.unicode.vo.UnicodeSupplyShipEntityVO;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-17 10:56:27
 */
public interface UnicodeSupplyShipDao extends BaseMapper<UnicodeSupplyShipEntity> {
	
	List<Map<String, Object>> selectMatchSupplier(Pagination page, Map<String, Object> params);

	List<UnicodeSupplyShipEntityVO> selectView(Page<UnicodeSupplyShipEntityVO> page, Map<String, Object> params);

	UnicodeSupplyShipEntity selectBySourcesSupplierIdAndSourcesHospitalId(@Param("sourcesSupplierId") String sourcesSupplierId, @Param("sourcesHospitalId") String sourcesHospitalId);

	/**
	 * 查询供应商医院关系
	 * @param supplierId 平台供应商id
	 * @param hospitalId  平台医院id
     * @return
     */
	UnicodeSupplyShipEntity selectBySupplierIdAndHospitalId(@Param("supplierId")Long supplierId,@Param("hospitalId") Long hospitalId);
}
