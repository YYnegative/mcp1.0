package com.ebig.hdi.modules.reagent.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ebig.hdi.modules.consumables.entity.UnicodeConsumablesCateEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.GoodsNatureEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.reagent.dao.UnicodeReagentCateDao;
import com.ebig.hdi.modules.reagent.entity.ReagentTreeNode;
import com.ebig.hdi.modules.reagent.entity.UnicodeReagentCateEntity;
import com.ebig.hdi.modules.reagent.service.UnicodeReagentCateService;

@Service("unicodeReagentCateService")
public class UnicodeReagentCateServiceImpl extends ServiceImpl<UnicodeReagentCateDao, UnicodeReagentCateEntity>
		implements UnicodeReagentCateService {

	/**
	 * 页面加载时刷新的耗材商品数据
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public List<ReagentTreeNode> queryNode(Map<String, Object> params) {
		List<UnicodeReagentCateEntity> consumables = baseMapper
				.selectReagent(Long.valueOf(String.valueOf(params.get("pcateId"))));

		ArrayList<ReagentTreeNode> treeNodeList = new ArrayList<>();
		for (UnicodeReagentCateEntity entity : consumables) {
			// TODO 1.增加其他属性
			UnicodeReagentCateEntity cateEntity = this.baseMapper.selectBypcateId(entity.getPcateId());
			ReagentTreeNode treeNode = new ReagentTreeNode();
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

			List<UnicodeReagentCateEntity> cateIdList = baseMapper.selectCateId(entity.getCateId());
			if (StringUtil.isEmpty(cateIdList)) {
				treeNode.setLeaf(true);
				treeNodeList.add(treeNode);
			} else {
				Map<String, Object> map = new HashMap<>();
				map.put("pcateId", entity.getCateId());
				treeNode.setLeaf(false);
				treeNode.setChildren(queryNode(map));
				treeNodeList.add(treeNode);
			}
		}
		return treeNodeList;
	}

	@Override
	public void queryList(Long pcateId ,List<Long> treeNodeList ){
		List<UnicodeReagentCateEntity> consumables = baseMapper
				.selectReagent(pcateId);
		if (!StringUtil.isEmpty(consumables)){
			for (UnicodeReagentCateEntity entity : consumables){
				queryList(entity.getCateId(),treeNodeList);
				treeNodeList.add(entity.getCateId());
			}
		}return;
	}



	/**
	 * 添加耗材商品数据
	 * 
	 * @param params
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(UnicodeReagentCateEntity entity) {
		if (0L == entity.getPcateId()) {
			entity.setCateLevel(1);
		} else {
			UnicodeReagentCateEntity cateEntity = baseMapper.selectById(entity.getPcateId());
			Integer cateLevel = cateEntity.getCateLevel();
			entity.setCateLevel(++cateLevel);
		}
		List<UnicodeReagentCateEntity> consumableslist = baseMapper.selectByCateLevel(entity.getCateLevel());
		if (!StringUtil.isEmpty(consumableslist)) {
			for (UnicodeReagentCateEntity consumablesEntity : consumableslist) {
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
	public void update(UnicodeReagentCateEntity entity) {
		List<UnicodeReagentCateEntity> Consumableslist = baseMapper.selectCateLevel(entity.getCateLevel(),
				entity.getCateId());
		for (UnicodeReagentCateEntity ConsumablesEntity : Consumableslist) {
			String cateNo = ConsumablesEntity.getCateNo();
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
		List<UnicodeReagentCateEntity> queryForList = baseMapper.selectCateId(cateId);

		for (UnicodeReagentCateEntity Entity : queryForList) {
			if (!StringUtil.isEmpty(Entity)) {
				this.deleteById(Entity.getCateId());
				deleteNode(Entity.getCateId());
			}
		}
		this.deleteById(cateId);
	}

	/**
	 * 描述: 获取试剂统一编码（商品属性码 + 模块码 + 分类码 + 商品码 + 厂家码） <br/>
	 * 
	 * @see com.ebig.hdi.modules.reagent.service.UnicodeReagentCateService#generatorGoodsUnicode(java.lang.Integer,
	 *      java.lang.Long, java.lang.String, java.lang.Long) <br/>
	 */
	@Override
	public String generatorGoodsUnicode(Integer goodsNature, Long typeId, String reagentCode, String factoryCode) {
		String goodsNatureCode = GoodsNatureEnum.DOMESTIC.getKey().equals(goodsNature)
				? GoodsNatureEnum.DOMESTIC.getValue()
				: GoodsNatureEnum.IMPORTED.getValue();
		String cateNo = this.selectById(typeId).getCateNo();
		return goodsNatureCode + cateNo + reagentCode + factoryCode;
	}

	@Override
	public void input(String[][] rows) {
		for (String[] row : rows) {
			// 大类码
			String mainTypeCode = row[0];
			String mainTypeName = row[1];
			// 一级产品分类码
			String firstTypeCode = row[2];
			String firstTypeName = row[3];

			// 大类码
			UnicodeReagentCateEntity mainType = inputCate(mainTypeName, mainTypeCode, 1, null);
			
			// 一级产品分类码
			inputCate(firstTypeName, firstTypeCode, 2, mainType);
		}

	}

	@Override
	public List<UnicodeReagentCateEntity> selectAll() {
		return this.baseMapper.selectList(null);
	}

	@Transactional(rollbackFor = Exception.class)
	private UnicodeReagentCateEntity inputCate(String cateName, String cateNo, Integer cateLevel, UnicodeReagentCateEntity parentCate) {
		Long pcateId = 0L; 
		if(null != parentCate) {
			pcateId = parentCate.getCateId();
		}
		List<UnicodeReagentCateEntity> typeList = this.baseMapper.selectList(
				new EntityWrapper<UnicodeReagentCateEntity>()
				.eq("cate_name", cateName)
				.eq("cate_level", cateLevel)
				.eq("pcate_id", pcateId));
		UnicodeReagentCateEntity type = new UnicodeReagentCateEntity();
		if (typeList.size() == 0) {
			// 不存在，保存
			type.setCateName(cateName);
			type.setCateNo(cateNo);
			type.setPcateId(null != parentCate ? parentCate.getCateId() : 0L);
			type.setCateLevel(cateLevel);
			type.setDelFlag(DelFlagEnum.NORMAL.getKey());
			this.baseMapper.insert(type);
		} else {
			// 存在，更新
			type = typeList.get(0);
			type.setCateNo(cateNo);
			type.setPcateId(null != parentCate ? parentCate.getCateId() : 0L);
			this.baseMapper.updateById(type);
		}
		
		return type;
	}

}
