package com.ebig.hdi.modules.unicode.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.DataSourceEnum;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.IsMatchEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.org.entity.OrgSupplierHospitalRefEntity;
import com.ebig.hdi.modules.org.service.OrgSupplierHospitalRefService;
import com.ebig.hdi.modules.unicode.dao.UnicodeSupplyShipDao;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import com.ebig.hdi.modules.unicode.service.UnicodeSupplyShipService;
import com.ebig.hdi.modules.unicode.vo.UnicodeSupplyShipEntityVO;

@Service("unicodeSupplyShipService")
public class UnicodeSupplyShipServiceImpl extends ServiceImpl<UnicodeSupplyShipDao, UnicodeSupplyShipEntity>
		implements UnicodeSupplyShipService {

	@Autowired
	private OrgSupplierHospitalRefService orgSupplierHospitalRefService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {

		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		
		Page<UnicodeSupplyShipEntityVO> page = new Page<>(currPage, pageSize);
		List<UnicodeSupplyShipEntityVO> list = this.baseMapper.selectView(page, params);
		page.setRecords(list);

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(UnicodeSupplyShipEntity unicodeSupplyShipEntity) {
		List<UnicodeSupplyShipEntity> list = this.baseMapper.selectList(
				new EntityWrapper<UnicodeSupplyShipEntity>()
				.eq("del_flag", DelFlagEnum.NORMAL.getKey())
				.eq("sources_supplier_credit_code", unicodeSupplyShipEntity.getSourcesSupplierCreditCode())
				.eq("sources_hospital_credit_code", unicodeSupplyShipEntity.getSourcesHospitalCreditCode())
				);
		if (!StringUtil.isEmpty(list)) {
			throw new HdiException("匹对关系已存在！");
		}
		unicodeSupplyShipEntity.setCredate(new Date());
		unicodeSupplyShipEntity.setDatasource(DataSourceEnum.MANUAL.getKey());
		unicodeSupplyShipEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
		unicodeSupplyShipEntity.setShipFlag(IsMatchEnum.NO.getKey());
		this.insert(unicodeSupplyShipEntity);

	}

	@Override
	public PageUtils matching(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		Page<Map<String, Object>> page = new Page<>(currPage, pageSize);
		
		List<Map<String, Object>> matchSupplierList= this.baseMapper.selectMatchSupplier(page, params);
		
		page.setRecords(matchSupplierList);
		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(UnicodeSupplyShipEntity unicodeSupplyShipEntity) {
		OrgSupplierHospitalRefEntity refEntity = orgSupplierHospitalRefService.selectById(unicodeSupplyShipEntity.getSupplierHospitalRefId());
		if (null != refEntity 
				&& DelFlagEnum.NORMAL.getKey().equals(refEntity.getDelFlag())
				&& unicodeSupplyShipEntity.getSupplierId().equals(refEntity.getSupplierId())
				&& unicodeSupplyShipEntity.getHospitalId().equals(refEntity.getHospitalId())
				) {
			unicodeSupplyShipEntity.setShipFlag(IsMatchEnum.YES.getKey());
			unicodeSupplyShipEntity.setEditdate(new Date());
			this.baseMapper.updateById(unicodeSupplyShipEntity);
		} else {
			throw new HdiException("关系不存在！");
		}
	}

	@Override
	public UnicodeSupplyShipEntity selectBySupplierIdAndHospitalId(Long supplierId, Long hospitalId) {
		return baseMapper.selectBySupplierIdAndHospitalId(supplierId,hospitalId);
	}

}
