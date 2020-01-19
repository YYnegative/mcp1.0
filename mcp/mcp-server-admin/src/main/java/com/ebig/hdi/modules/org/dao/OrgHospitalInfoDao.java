package com.ebig.hdi.modules.org.dao;

import java.util.List;
import java.util.Map;

import com.ebig.hdi.common.entity.OrgHospitalInfoApprovalEntity;
import com.ebig.hdi.modules.org.param.OrgFactoryParam;
import com.ebig.hdi.modules.org.param.OrgHospitalParam;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;

/**
 * 医院信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:40
 */
public interface OrgHospitalInfoDao extends BaseMapper<OrgHospitalInfoEntity> {
	
	List<OrgHospitalInfoEntity> selectByHospitalName(@Param("hospitalName") String hospitalName);
	
	List<OrgHospitalInfoEntity> selectByCreditCode(@Param("creditCode") String creditCode);

	List<Map<String, Object>> getList(@Param("columns")String[] columns, @Param("queryParam") OrgHospitalParam orgHospitalParam);

	/**
	 * 医院待审批表数据到医院
	 * @param hospitalInfo 待审批表实体
	 */
	void insertApproval(OrgHospitalInfoApprovalEntity hospitalInfo);

	/**
	 * 医院待审批表数据更新到医院
	 * @param hospitalInfo 待审批表实体
	 */
	void updateApproval(OrgHospitalInfoApprovalEntity hospitalInfo);
}
