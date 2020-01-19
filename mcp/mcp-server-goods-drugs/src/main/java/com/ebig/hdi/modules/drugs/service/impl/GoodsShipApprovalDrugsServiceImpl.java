package com.ebig.hdi.modules.drugs.service.impl;

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
import com.ebig.hdi.modules.drugs.dao.GoodsShipApprovalDrugsDao;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.UnicodeGoodsShipDrugsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsHospitalDrugsService;
import com.ebig.hdi.modules.drugs.service.GoodsShipApprovalDrugsService;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsService;
import com.ebig.hdi.modules.drugs.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("goodsShipApprovalDrugsService")
public class GoodsShipApprovalDrugsServiceImpl extends ServiceImpl<GoodsShipApprovalDrugsDao, UnicodeGoodsShipApprovalEntity> implements GoodsShipApprovalDrugsService {

    @Autowired
    private UnicodeGoodsShipHistService unicodeGoodsShipHistService;

    @Autowired
    private GoodsSupplierDrugsService goodsSupplierDrugsService;

    @Autowired
    private GoodsHospitalDrugsService goodsHospitalDrugsService;

    @Autowired
    private UnicodeGoodsShipService unicodeGoodsShipService;

    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
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
    public void updateHospitalPgoodsId(SysUserEntity sysUserEntity, UnicodeGoodsShipApprovalEntity entity) {
        if (entity != null && sysUserEntity != null) {
            UnicodeGoodsShipApprovalEntity ship = this.baseMapper.selectById(entity.getShipId());
            if (ship != null) {
                ship.setEditdate(new Timestamp(System.currentTimeMillis()));
                ship.setPgoodsId(entity.getId());
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                ship.setShipFlag(IsMatchEnum.YES.getKey());
                ship.setEditmanid(sysUserEntity.getUserId());
                ship.setEditmanname(sysUserEntity.getUsername());
                ship.setPapprovalId(entity.getPapprovalId());
                ship.setPspecsId(entity.getPspecsId());
                List<UnicodeGoodsShipApprovalEntity> hospitalMatchGoodsList = this.baseMapper.listHospitalMatchGoods(ship.getTorgId(), ship.getPgoodsId(), entity.getPspecsId());

                if (CollectionUtils.isNotEmpty(hospitalMatchGoodsList)) {
                    throw new HdiException("匹对失败！该平台商品已存在匹对关系！");
                }

                // 更新商品匹对状态为已匹对
                GoodsHospitalDrugsEntity goodsHospitalDrugsEntity = new GoodsHospitalDrugsEntity();
                goodsHospitalDrugsEntity.setId(ship.getTgoodsId());
                goodsHospitalDrugsEntity.setIsMatch(IsMatchEnum.YES.getKey());
                unicodeGoodsShipApprovalService.initiateApproval(sysUserEntity, ship, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.HOSPITAL_PLATFORM_DRUGS_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_HOSPITAL_PLATFORM_DRUGS_MATCH);
                this.updateById(ship);
                // 将匹对记录插入匹对历史表
                UnicodeGoodsShipHistEntity hist = ReflectUitls.transform(ship,UnicodeGoodsShipHistEntity.class);
                hist.setOperType(OperationTypeEnum.MATCH.getKey());
                hist.setShipFlag(IsMatchEnum.YES.getKey());
                hist.setCremanid(sysUserEntity.getUserId());
                hist.setCremanname(sysUserEntity.getUsername());
                hist.setCredate(new Date());
                hist.setPgoodsName(entity.getPgoodsName());
                hist.setPapprovals(entity.getPapprovals());
                hist.setPspecs(entity.getPspecs());
                hist.setPfactoryName(entity.getFactoryName());
                hist.setPgoodsNature(entity.getPgoodsNature());
                hist.setDelFlag(DelFlagEnum.NORMAL.getKey());
                unicodeGoodsShipHistService.insert(hist);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplierPgoodsId(SysUserEntity sysUserEntity, UnicodeGoodsShipApprovalEntity entity) {
		if (entity != null && sysUserEntity != null) {
			UnicodeGoodsShipApprovalEntity ship = this.baseMapper.selectById(entity.getShipId());
			if (ship != null) {
                ship.setEditdate(new Timestamp(System.currentTimeMillis()));
                ship.setPgoodsId(entity.getId());
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                ship.setShipFlag(IsMatchEnum.YES.getKey());
                ship.setEditmanid(sysUserEntity.getUserId());
                ship.setEditmanname(sysUserEntity.getUsername());
                ship.setPapprovalId(entity.getPapprovalId());
                ship.setPspecsId(entity.getPspecsId());

				List<UnicodeGoodsShipApprovalEntity> supplierMatchGoodsList = this.baseMapper.listSupplierMatchGoods(ship.getTorgId(), ship.getPgoodsId(), entity.getPspecsId());

				if (!StringUtil.isEmpty(supplierMatchGoodsList)) {
					throw new HdiException("匹对失败！该平台商品已存在匹对关系！");
				}

				// 更新商品匹对状态为已匹对
				GoodsSupplierDrugsEntity goodsSupplierDrugsEntity = new GoodsSupplierDrugsEntity();
				goodsSupplierDrugsEntity.setId(ship.getTgoodsId());
				goodsSupplierDrugsEntity.setIsMatch(IsMatchEnum.YES.getKey());
				goodsSupplierDrugsService.updateById(goodsSupplierDrugsEntity);
				unicodeGoodsShipApprovalService.initiateApproval(sysUserEntity, ship, ChangeTypeEnum.UPDATE.getKey(),
						ActTypeEnum.SUPPLIER_PLATFORM_DRUGS_MATCH.getKey(), ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_DRUGS_MATCH);
				this.updateById(ship);

                // 将匹对记录插入匹对历史表
                UnicodeGoodsShipHistEntity hist = ReflectUitls.transform(ship,UnicodeGoodsShipHistEntity.class);
                hist.setOperType(OperationTypeEnum.MATCH.getKey());
                hist.setShipFlag(IsMatchEnum.YES.getKey());
                hist.setCremanid(sysUserEntity.getUserId());
                hist.setCremanname(sysUserEntity.getUsername());
                hist.setCredate(new Date());
                hist.setPgoodsName(entity.getPgoodsName());
                hist.setPapprovals(entity.getPapprovals());
                hist.setPspecs(entity.getPspecs());
                hist.setEditdate(null);
                hist.setEditmanname(null);
                hist.setPfactoryName(entity.getFactoryName());
                hist.setPgoodsNature(entity.getPgoodsNature());
                hist.setDelFlag(DelFlagEnum.NORMAL.getKey());
                unicodeGoodsShipHistService.insert(hist);

			}
		}
	}


    @Override
    public List<UnicodeGoodsShipApprovalEntity> listHospitalMatchGoods(Long torgId, Long pGoodsId, Long pSpecsId) {
        return this.baseMapper.listHospitalMatchGoods(torgId, pGoodsId, pSpecsId);
    }

    @Override
    public List<UnicodeGoodsShipApprovalEntity> listSupplierMatchGoods(Long torgId, Long pGoodsId, Long pSpecsId) {
        return this.baseMapper.listSupplierMatchGoods(torgId, pGoodsId, pSpecsId);
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
            UnicodeGoodsShipDrugsEntity unicodeGoodsShipEntity = unicodeGoodsShipService.selectById(shipId);
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
            UnicodeGoodsShipDrugsEntity unicodeGoodsShipEntity = unicodeGoodsShipService.selectById(shipId);
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
}
