package com.ebig.hdi.modules.drugs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.GoodsNatureEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.drugs.dao.UnicodeDrugsCateDao;
import com.ebig.hdi.modules.drugs.entity.DrugsTreeNode;
import com.ebig.hdi.modules.drugs.entity.UnicodeDrugsCateEntity;
import com.ebig.hdi.modules.drugs.service.UnicodeDrugsCateService;

@Service("unicodeDrugsCateService")
public class UnicodeDrugsCateServiceImpl extends ServiceImpl<UnicodeDrugsCateDao, UnicodeDrugsCateEntity>
		implements UnicodeDrugsCateService {

	/**
	 * 页面加载时刷新的耗材商品数据
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public List<DrugsTreeNode> queryNode(Map<String, Object> params) {
		List<UnicodeDrugsCateEntity> consumables = baseMapper
				.selectDrugs(Long.valueOf(String.valueOf(params.get("pcateId"))));

		ArrayList<DrugsTreeNode> treeNodeList = new ArrayList<>();
		for (UnicodeDrugsCateEntity entity : consumables) {
			// TODO 1.增加其他属性
			UnicodeDrugsCateEntity cateEntity = this.baseMapper.selectBypcateId(entity.getPcateId());
			DrugsTreeNode treeNode = new DrugsTreeNode();
			if (cateEntity == null) {
				treeNode.setPcateName(null);
			} else {
				treeNode.setPcateName(cateEntity.getCateName());
			}
			treeNode.setCateNo(entity.getCateNo());
			treeNode.setCateId(entity.getCateId());
			treeNode.setCateName(entity.getCateName());
			treeNode.setLevelOrder(entity.getLevelOrder());
			treeNode.setCateLevel(entity.getCateLevel());
			treeNode.setPcateId(entity.getPcateId());
			treeNode.setDeptId(entity.getDeptId());
			// TODO 2.叶子节点判断
			List<UnicodeDrugsCateEntity> cateIdList = baseMapper.selectCateId(entity.getCateId());
			if (StringUtil.isEmpty(cateIdList)) {
				treeNode.setLeaf(true);
				treeNodeList.add(treeNode);
			} else {
				Map<String, Object> map = new HashMap<>(16);
				map.put("pcateId", entity.getCateId());
				treeNode.setLeaf(false);
				treeNode.setChildren(queryNode(map));
				treeNodeList.add(treeNode);
			}
		}
		return treeNodeList;
	}

	/**
	 * 添加耗材商品数据
	 * 
	 * @param params
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(UnicodeDrugsCateEntity entity) {
		if (0L == entity.getPcateId()) {
			entity.setCateLevel(1);
		} else {
			UnicodeDrugsCateEntity cateEntity = baseMapper.selectById(entity.getPcateId());
			Integer cateLevel = cateEntity.getCateLevel();
			entity.setCateLevel(++cateLevel);
		}
		List<UnicodeDrugsCateEntity> consumableslist = baseMapper.selectByCateLevel(entity.getCateLevel());
		if (!StringUtil.isEmpty(consumableslist)) {
			for (UnicodeDrugsCateEntity consumablesEntity : consumableslist) {
				String cateNo = consumablesEntity.getCateNo();
				if (!StringUtil.isEmpty(cateNo) && cateNo.equals(entity.getCateNo())) {
					throw new HdiException("商品分类编码已存在");
				}
			}
		}
		entity.setDelFlag(DelFlagEnum.NORMAL.getKey());
		this.insert(entity);
	}

	/**
	 * 编辑耗材商品数据
	 * 
	 * @param params
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(UnicodeDrugsCateEntity entity) {
		List<UnicodeDrugsCateEntity> Consumableslist = baseMapper.selectCateLevel(entity.getCateLevel(),
				entity.getCateId());
		for (UnicodeDrugsCateEntity consumablesEntity : Consumableslist) {
			String cateNo = consumablesEntity.getCateNo();
			if (!StringUtil.isEmpty(cateNo) && cateNo.equals(entity.getCateNo())) {
				throw new HdiException("更改的商品分类编码已存在");
			}
		}
		this.updateById(entity);
	}

	/**
	 * 删除耗材商品数据
	 * 
	 * @param params
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteNode(Long cateId) {
		List<UnicodeDrugsCateEntity> queryForList = baseMapper.selectCateId(cateId);

		for (UnicodeDrugsCateEntity Entity : queryForList) {
			if (!StringUtil.isEmpty(Entity)) {
				this.deleteById(Entity.getCateId());
				deleteNode(Entity.getCateId());
			}
		}
		this.deleteById(cateId);
	}

	/**
	 * 描述: 生成药品统一编码（商品属性码 + 模块码 + 分类码 + 商品码 + 厂家码） <br/>
	 * 
	 * @see com.ebig.hdi.modules.drugs.service.UnicodeDrugsCateService#generatorGoodsUnicode(java.lang.Integer,
	 *      java.lang.Long, java.lang.String, java.lang.Long) <br/>
	 */
	@Override
	public String generatorGoodsUnicode(Integer goodsNature, Long typeId, String drugsCode, String factoryCode) {
		String goodsNatureCode = GoodsNatureEnum.DOMESTIC.getKey().equals(goodsNature)
				? GoodsNatureEnum.DOMESTIC.getValue()
				: GoodsNatureEnum.IMPORTED.getValue();
		String cateNo = this.selectById(typeId).getCateNo();
		return goodsNatureCode + cateNo + drugsCode + factoryCode;
	}

	@Override
	public void input(String[][] rows) {
		//TODO 
	}

	@Override
	public List<UnicodeDrugsCateEntity> selectAll() {
		List<UnicodeDrugsCateEntity> unicodeDrugsCateEntityList = this.baseMapper.selectAll();
		return unicodeDrugsCateEntityList;
	}

	@Override
	public void queryList(Long  pcateId, List<Long> treeNodeList) {
		List<UnicodeDrugsCateEntity> unicodeDrugsCateEntityList = baseMapper.selectDrugs(pcateId);
		if (CollectionUtils.isNotEmpty(unicodeDrugsCateEntityList)){
			for (UnicodeDrugsCateEntity unicodeDrugsCateEntity : unicodeDrugsCateEntityList) {
				queryList(unicodeDrugsCateEntity.getCateId(),treeNodeList);
				treeNodeList.add(unicodeDrugsCateEntity.getCateId());
			}
		}
		return ;
	}


}
