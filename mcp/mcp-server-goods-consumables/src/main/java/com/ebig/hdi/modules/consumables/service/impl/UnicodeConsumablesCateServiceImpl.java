package com.ebig.hdi.modules.consumables.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.GoodsNatureEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.consumables.dao.UnicodeConsumablesCateDao;
import com.ebig.hdi.modules.consumables.entity.ConsumablesTreeNode;
import com.ebig.hdi.modules.consumables.entity.UnicodeConsumablesCateEntity;
import com.ebig.hdi.modules.consumables.service.UnicodeConsumablesCateService;

@Service("unicodeConsumablesCateService")
public class UnicodeConsumablesCateServiceImpl extends
		ServiceImpl<UnicodeConsumablesCateDao, UnicodeConsumablesCateEntity> implements UnicodeConsumablesCateService {

	/**
	 * 页面加载时刷新的耗材商品数据
	 * 
	 * @param params
	 * @return
	 */
	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "ucc")
	public List<ConsumablesTreeNode> queryNode(Map<String, Object> params) {
		List<UnicodeConsumablesCateEntity> consumables = baseMapper
				.selectConsumables(Long.valueOf(String.valueOf(params.get("pcateId"))));

		ArrayList<ConsumablesTreeNode> treeNodeList = new ArrayList<>();
		for (UnicodeConsumablesCateEntity entity : consumables) {
			// TODO 1.增加其他属性
			UnicodeConsumablesCateEntity cateEntity = this.baseMapper.selectBypcateId(entity.getPcateId());
			ConsumablesTreeNode treeNode = new ConsumablesTreeNode();
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
			List<UnicodeConsumablesCateEntity> cateIdList = baseMapper.selectCateId(entity.getCateId());
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

	/**
	 * 添加耗材商品数据
	 *
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(UnicodeConsumablesCateEntity entity) {
		if (0L == entity.getPcateId()) {
			entity.setCateLevel(1);
		} else {
			UnicodeConsumablesCateEntity cateEntity = baseMapper.selectById(entity.getPcateId());
			Integer cateLevel = cateEntity.getCateLevel();
			entity.setCateLevel(++cateLevel);
		}
		List<UnicodeConsumablesCateEntity> consumableslist = baseMapper.selectByCateLevel(entity.getCateLevel());
		if (!StringUtil.isEmpty(consumableslist)) {
			for (UnicodeConsumablesCateEntity consumablesEntity : consumableslist) {
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
	 * @param entity
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(UnicodeConsumablesCateEntity entity) {
		List<UnicodeConsumablesCateEntity> Consumableslist = baseMapper.selectCateLevel(entity.getCateLevel(),
				entity.getCateId());
		for (UnicodeConsumablesCateEntity ConsumablesEntity : Consumableslist) {
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
	 * @param cateId
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteNode(Long cateId) {
		List<UnicodeConsumablesCateEntity> queryForList = baseMapper.selectCateId(cateId);

		for (UnicodeConsumablesCateEntity Entity : queryForList) {
			if (!StringUtil.isEmpty(Entity)) {
				this.deleteById(Entity.getCateId());
				deleteNode(Entity.getCateId());
			}
		}
		this.deleteById(cateId);
	}

	/**
	 * 描述: 生成耗材统一编码（商品属性码 + 模块码 + 分类码 + 商品码 + 厂家码） <br/>
	 *
	 */
	@Override
	public String generatorGoodsUnicode(Integer goodsNature, Long typeId, String consumablesCode, String factoryCode) {
		String goodsNatureCode = GoodsNatureEnum.DOMESTIC.getKey().equals(goodsNature)
				? GoodsNatureEnum.DOMESTIC.getValue()
				: GoodsNatureEnum.IMPORTED.getValue();
		String cateNo = this.selectById(typeId).getCateNo();
		return goodsNatureCode + cateNo + consumablesCode + factoryCode;
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
			// 二级产品分类码
			String secondTypeCode = row[4];
			String secondTypeName = row[5];

			// 大类码
			UnicodeConsumablesCateEntity mainType = inputCate(mainTypeName, mainTypeCode, 1, null);
			
			// 一级产品分类码
			UnicodeConsumablesCateEntity firstType = inputCate(firstTypeName, firstTypeCode, 2, mainType);
			
			// 二级产品分类码
			inputCate(secondTypeName, secondTypeCode, 3, firstType);
			
		}

	}

	/**
	 * 查询所有耗材商品分类标识 和 名称
	 * @return
	 */
	@Override
	public List<UnicodeConsumablesCateEntity> selectAll() {
		List<UnicodeConsumablesCateEntity> List=this.baseMapper.selectAll();
		return List;
	}

	@Override
	public void queryList(Long pcateId, List<Long> treeNodeList) {
		List<UnicodeConsumablesCateEntity> consumables = baseMapper
				.selectConsumables(pcateId);
		if (!StringUtil.isEmpty(consumables)){
			for (UnicodeConsumablesCateEntity entity : consumables){
				queryList(entity.getCateId(),treeNodeList);
				treeNodeList.add(entity.getCateId());
			}
		}return;
	}

	@Transactional(rollbackFor = Exception.class)
	private UnicodeConsumablesCateEntity inputCate(String cateName, String cateNo, Integer cateLevel, UnicodeConsumablesCateEntity parentCate) {
		Long pcateId = 0L; 
		if(null != parentCate) {
			pcateId = parentCate.getCateId();
		}
		List<UnicodeConsumablesCateEntity> typeList = this.baseMapper.selectList(
				new EntityWrapper<UnicodeConsumablesCateEntity>()
				.eq("cate_name", cateName)
				.eq("cate_level", cateLevel)
				.eq("pcate_id", pcateId));
		UnicodeConsumablesCateEntity type = new UnicodeConsumablesCateEntity();
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
