package com.ebig.hdi.modules.reagent.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.constant.PageConstant;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipHistService;
import com.ebig.hdi.modules.reagent.dao.GoodsShipApprovalReagentDao;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentEntity;
import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipReagentEntity;
import com.ebig.hdi.modules.reagent.service.GoodsHospitalReagentService;
import com.ebig.hdi.modules.reagent.service.GoodsShipApprovalReagentService;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentService;
import com.ebig.hdi.modules.reagent.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("goodsShipApprovalReagentService")
public class GoodsShipApprovalReagentServiceImpl extends ServiceImpl<GoodsShipApprovalReagentDao, UnicodeGoodsShipApprovalEntity> implements GoodsShipApprovalReagentService {

    @Autowired
    private UnicodeGoodsShipHistService unicodeGoodsShipHistService;

    @Autowired
    private GoodsHospitalReagentService goodsHospitalReagentService;

    @Autowired
    private GoodsSupplierReagentService goodsSupplierReagentService;

    @Autowired
    private UnicodeGoodsShipService unicodeGoodsShipService;

    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;

    @Override
    public PageUtils queryPageHospitalGoods(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get(PageConstant.PAGE).toString());
        int pageSize = Integer.parseInt(params.get(PageConstant.LIMIT).toString());

        Page<UnicodeGoodsShipApprovalEntity> page = new Page<>(currPage, pageSize);
        List<UnicodeGoodsShipApprovalEntity> list = this.baseMapper.selectHospitaGoods(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
    public PageUtils queryPageSupplierGoods(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get(PageConstant.PAGE).toString());
        int pageSize = Integer.parseInt(params.get(PageConstant.LIMIT).toString());

        Page<UnicodeGoodsShipApprovalEntity> page = new Page<>(currPage, pageSize);
        List<UnicodeGoodsShipApprovalEntity> list = this.baseMapper.selectSupplierGoods(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public PageUtils pGoodsList(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get(PageConstant.PAGE).toString());
        int pageSize = Integer.parseInt(params.get(PageConstant.LIMIT).toString());
        params.put("goodsName", params.get("pGoodsName"));
        params.put("pspecs", params.get("pGoodsSpecs"));

        Page<UnicodeGoodsShipApprovalEntity> page = new Page<>(currPage, pageSize);
        List<UnicodeGoodsShipApprovalEntity> list = this.baseMapper.selectPGoodsList(page, params);
        if (CollectionUtils.isEmpty(list)) {
            params.put("approvals", "");
            list = this.baseMapper.selectPGoodsList(page, params);
        }
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHospitalPgoodsId(SysUserEntity userEntity, UnicodeGoodsShipApprovalEntity entity) {
        if (entity != null && userEntity != null) {
            UnicodeGoodsShipApprovalEntity ship = this.baseMapper.selectById(entity.getShipId());
            if (ship != null) {
                ship.setEditdate(new Timestamp(System.currentTimeMillis()));
                ship.setPgoodsId(entity.getId());
                ship.setPapprovalId(entity.getPapprovalId());
                ship.setPspecsId(entity.getPspecsId());
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                ship.setShipFlag(IsMatchEnum.YES.getKey());
                ship.setEditmanid(userEntity.getUserId());
                ship.setEditmanname(userEntity.getUsername());

                List<UnicodeGoodsShipApprovalEntity> hospitalMatchGoodsList = this.baseMapper.listHospitalMatchGoods(ship.getTorgId(), ship.getPgoodsId(), entity.getPspecsId());

                if (CollectionUtils.isNotEmpty(hospitalMatchGoodsList)) {
                    throw new HdiException("匹对失败！该平台商品已存在匹对关系！");
                }
                // 更新商品匹对状态为已匹对
                GoodsHospitalReagentEntity goodsHospitalReagentEntity = new GoodsHospitalReagentEntity();
                goodsHospitalReagentEntity.setId(ship.getTgoodsId());
                goodsHospitalReagentEntity.setIsMatch(IsMatchEnum.YES.getKey());
                goodsHospitalReagentService.updateById(goodsHospitalReagentEntity);
                unicodeGoodsShipApprovalService.initiateApproval(userEntity, ship, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.HOSPITAL_PLATFORM_REAGENT_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_HOSPITAL_PLATFORM_REAGENT_MATCH);
                this.updateById(ship);

                // 将匹对记录插入匹对历史表
                UnicodeGoodsShipHistEntity hist = ReflectUitls.transform(ship,UnicodeGoodsShipHistEntity.class);
                hist.setOperType(OperationTypeEnum.MATCH.getKey());
                hist.setShipFlag(IsMatchEnum.YES.getKey());
                hist.setCremanid(userEntity.getUserId());
                hist.setCremanname(userEntity.getUsername());
                hist.setCredate(new Date());
                hist.setPgoodsName(entity.getPgoodsName());
                hist.setPapprovals(entity.getPapprovals());
                hist.setPspecs(entity.getPspecs());
                hist.setEditdate(null);
                hist.setEditmanname(null);
                hist.setPgoodsNature(entity.getPgoodsNature());
                hist.setPfactoryName(entity.getFactoryName());
                hist.setDelFlag(DelFlagEnum.NORMAL.getKey());
                unicodeGoodsShipHistService.insert(hist);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplierPgoodsId(SysUserEntity userEntity, UnicodeGoodsShipApprovalEntity entity) {
        if (entity != null && userEntity != null) {
            UnicodeGoodsShipApprovalEntity ship = this.baseMapper.selectById(entity.getShipId());

            if (ship != null) {
                ship.setEditdate(new Timestamp(System.currentTimeMillis()));
                ship.setPgoodsId(entity.getId());
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                ship.setShipFlag(IsMatchEnum.YES.getKey());
                ship.setEditmanid(userEntity.getUserId());
                ship.setEditmanname(userEntity.getUsername());
                ship.setPapprovalId(entity.getPapprovalId());
                ship.setPspecsId(entity.getPspecsId());
                List<UnicodeGoodsShipApprovalEntity> supplierMatchGoodsList = this.baseMapper.listSupplierMatchGoods(ship.getTorgId(), ship.getPgoodsId(), entity.getPspecsId());

                if (!StringUtil.isEmpty(supplierMatchGoodsList)) {
                    throw new HdiException("匹对失败！该平台商品已存在匹对关系！");
                }
                // 更新商品匹对状态为已匹对
                GoodsSupplierReagentEntity goodsSupplierReagentEntity = new GoodsSupplierReagentEntity();
                goodsSupplierReagentEntity.setId(ship.getTgoodsId());
                goodsSupplierReagentEntity.setIsMatch(IsMatchEnum.YES.getKey());
                goodsSupplierReagentService.updateById(goodsSupplierReagentEntity);
                unicodeGoodsShipApprovalService.initiateApproval(userEntity, ship, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_REAGENT_MATCH.getKey(), ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_REAGENT_MATCH);
                this.updateById(ship);
                // 将匹对记录插入匹对历史表
                UnicodeGoodsShipHistEntity hist = ReflectUitls.transform(ship,UnicodeGoodsShipHistEntity.class);
                hist.setOperType(OperationTypeEnum.MATCH.getKey());
                hist.setShipFlag(IsMatchEnum.YES.getKey());
                hist.setCremanid(userEntity.getUserId());
                hist.setCremanname(userEntity.getUsername());
                hist.setCredate(new Date());
                hist.setEditdate(null);
                hist.setEditmanname(null);
                hist.setPgoodsName(entity.getPgoodsName());
                hist.setPapprovals(entity.getPapprovals());
                hist.setPspecs(entity.getPspecs());
                hist.setPgoodsNature(entity.getPgoodsNature());
                hist.setDelFlag(DelFlagEnum.NORMAL.getKey());
                hist.setPfactoryName(entity.getFactoryName());
                unicodeGoodsShipHistService.insert(hist);
            }
        }
    }

    @Override
    public List<UnicodeGoodsShipApprovalEntity> listHospitalMatchGoods(Long torgId, Long pgoodsId, Long pspecsId) {
        return this.baseMapper.listHospitalMatchGoods(torgId, pgoodsId, pspecsId);
    }

    @Override
    public List<UnicodeGoodsShipApprovalEntity> listSupplierMatchGoods(Long torgId, Long pgoodsId, Long pspecsId) {
        return this.baseMapper.listSupplierMatchGoods(torgId, pgoodsId, pspecsId);
    }

    @Override
    public Map<String, String> checkStatus(Map<String, String> params, SysUserEntity user) {
        Map<String, String> errorMap = new HashMap<>(16);
        Long shipId = Long.parseLong(params.get("shipId"));
        Integer checkStatus = Integer.parseInt(params.get("checkStatus"));
        if (StringUtil.isEmpty(checkStatus)) {
            errorMap.put("errorMessage", "状态为空");
            return errorMap;
        }
        if (StringUtil.isEmpty(shipId)) {
            errorMap.put("errorMessage", "传入ID为空");
            return errorMap;
        }
        if (!user.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())) {
            errorMap.put("errorMessage", "非平台用户审批权限");
            return errorMap;
        }
        UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity = this.baseMapper.selectById(shipId);
        unicodeGoodsShipApprovalEntity.setEditdate(new Date());
        unicodeGoodsShipApprovalEntity.setEditmanid(user.getUserId());
        if (ApprovalTypeEnum.PASS.getKey().equals(checkStatus)) {
            //修改待审批表
            unicodeGoodsShipApprovalEntity.setCheckStatus(ApprovalTypeEnum.PASS.getKey());
            UnicodeGoodsShipReagentEntity unicodeGoodsShipEntity = unicodeGoodsShipService.selectById(shipId);
            if (StringUtil.isEmpty(unicodeGoodsShipEntity)) {
                unicodeGoodsShipService.insert(unicodeGoodsShipApprovalEntity);
            } else {
                unicodeGoodsShipService.update(unicodeGoodsShipApprovalEntity);
            }
            //修改历史记录表
        }
        if (ApprovalTypeEnum.FAIL.getKey().equals(checkStatus)) {
            unicodeGoodsShipApprovalEntity.setShipFlag(IsMatchEnum.NO.getKey());
            unicodeGoodsShipApprovalEntity.setCheckStatus(ApprovalTypeEnum.FAIL.getKey());
        }
        this.baseMapper.updateById(unicodeGoodsShipApprovalEntity);
        return errorMap;
    }

	@Override
	public Map<String, String> hospitalCheckstatus(Map<String, String> params, SysUserEntity user) {
		Map<String,String> errorMap = new HashMap<>(16);
		Long shipId = Long.parseLong(params.get("shipId"));
		Integer checkStatus = Integer.parseInt(params.get("checkStatus"));
		if (StringUtil.isEmpty(checkStatus)) {
			errorMap.put("errorMessage", "状态为空");
			return errorMap;
		}
		if (StringUtil.isEmpty(shipId)) {
			errorMap.put("errorMessage", "传入ID为空");
			return errorMap;
		}
		if (!user.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())){
			errorMap.put("errorMessage", "非平台用户审批权限");
			return errorMap;
		}
		UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity = this.baseMapper.selectById(shipId);
		unicodeGoodsShipApprovalEntity.setEditdate(new Date());
		unicodeGoodsShipApprovalEntity.setEditmanid(user.getUserId());
		if (ApprovalTypeEnum.PASS.getKey().equals(checkStatus)){
			//修改待审批表
			unicodeGoodsShipApprovalEntity.setCheckStatus(ApprovalTypeEnum.PASS.getKey());
			UnicodeGoodsShipReagentEntity unicodeGoodsShipEntity = unicodeGoodsShipService.selectById(shipId);
			if (StringUtil.isEmpty(unicodeGoodsShipEntity)){
				unicodeGoodsShipService.insert(unicodeGoodsShipApprovalEntity);
			}else {
				unicodeGoodsShipService.update(unicodeGoodsShipApprovalEntity);
			}

		}
		if (ApprovalTypeEnum.FAIL.getKey().equals(checkStatus)){
            unicodeGoodsShipApprovalEntity.setShipFlag(IsMatchEnum.NO.getKey());
			unicodeGoodsShipApprovalEntity.setCheckStatus(ApprovalTypeEnum.FAIL.getKey());
		}
		this.baseMapper.updateById(unicodeGoodsShipApprovalEntity);
		return errorMap;
	}
}
