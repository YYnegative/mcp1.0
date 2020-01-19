package com.ebig.hdi.modules.core.service.impl;

import com.ebig.hdi.common.enums.LabelTypeEnum;
import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.dao.CoreLabelSizeDao;
import com.ebig.hdi.modules.core.entity.CoreLabelSizeEntity;
import com.ebig.hdi.modules.core.service.CoreLabelSizeService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;


@Service("coreLabelSizeService")
public class CoreLabelSizeServiceImpl extends ServiceImpl<CoreLabelSizeDao, CoreLabelSizeEntity> implements CoreLabelSizeService {

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CoreLabelSizeEntity> page = this.selectPage(
                new Query<CoreLabelSizeEntity>(params).getPage(),
                new EntityWrapper<CoreLabelSizeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public CoreLabelSizeEntity getUserDetail(Long userId,Integer typeId) {
        return this.baseMapper.getUserDetail(userId,typeId);
    }

    @Override
    public CoreLabelSizeEntity selectByUserIdAndTypeId( Map<String, Object> params) {
        Long userId = Long.parseLong(params.get("userId").toString());
        Integer typeId = Integer.parseInt(params.get("typeId").toString());
        CoreLabelSizeEntity coreLabelSizeEntitys = new CoreLabelSizeEntity();
        CoreLabelSizeEntity coreLabelSizeEntity = this.baseMapper.getUserDetail(userId, typeId);
        if (StringUtil.isEmpty(coreLabelSizeEntity)){
            if (LabelTypeEnum.LABEL.getKey().equals(typeId)){
                coreLabelSizeEntitys.setTypeId(LabelTypeEnum.LABEL.getKey());
                coreLabelSizeEntitys.setPdfWidth(Double.parseDouble(sysConfigService.getValue(SysConfigEnum.LABEL_CODE_WIDTH.getKey())));
                coreLabelSizeEntitys.setPdfHeight(Double.parseDouble(sysConfigService.getValue(SysConfigEnum.LABEL_CODE_HEIGHT.getKey())));
                coreLabelSizeEntitys.setQrcodeHeight(Integer.parseInt(sysConfigService.getValue(SysConfigEnum.QRCODE_LABEL_HEIGHT.getKey())));
                coreLabelSizeEntitys.setQrcodeWidth(Integer.parseInt(sysConfigService.getValue(SysConfigEnum.QRCODE_LABEL_WIDTH.getKey())));
            }else {
                coreLabelSizeEntitys.setTypeId(LabelTypeEnum.Batch.getKey());
                coreLabelSizeEntitys.setPdfWidth(Double.parseDouble(sysConfigService.getValue(SysConfigEnum.BATCH_CODE_WIDTH.getKey())));
                coreLabelSizeEntitys.setPdfHeight(Double.parseDouble(sysConfigService.getValue(SysConfigEnum.BATCH_CODE_HEIGHT.getKey())));
                coreLabelSizeEntitys.setQrcodeHeight(Integer.parseInt(sysConfigService.getValue(SysConfigEnum.QRCODE_BATCH_HEIGHT.getKey())));
                coreLabelSizeEntitys.setQrcodeWidth(Integer.parseInt(sysConfigService.getValue(SysConfigEnum.QRCODE_BATCH_WIDTH.getKey())));
            }
            return coreLabelSizeEntitys;
        }else {
            return coreLabelSizeEntity;
        }

    }

    @Override
    public void insertOrUpdateByUserIdAndTypeId(CoreLabelSizeEntity coreLabelSize, SysUserEntity user) {
        CoreLabelSizeEntity labelSizeEntity = this.baseMapper.getUserDetail(user.getUserId(), coreLabelSize.getTypeId());
        if (StringUtil.isEmpty(labelSizeEntity)) {
            coreLabelSize.setUserId(user.getUserId());
            coreLabelSize.setCreateId(user.getUserId());
            coreLabelSize.setCreateTime(new Date());
            coreLabelSize.setCreateName(user.getUsername());
            this.baseMapper.insert(coreLabelSize);
        } else {
            labelSizeEntity.setEditId(user.getUserId());
            labelSizeEntity.setEditName(user.getUsername());
            labelSizeEntity.setEditTime(new Date());
            labelSizeEntity.setPdfWidth(coreLabelSize.getPdfWidth());
            labelSizeEntity.setPdfHeight(coreLabelSize.getPdfHeight());
            labelSizeEntity.setQrcodeWidth(coreLabelSize.getQrcodeWidth());
            labelSizeEntity.setQrcodeHeight(coreLabelSize.getQrcodeHeight());
            this.baseMapper.updateAllColumnById(labelSizeEntity);
        }
    }
}
