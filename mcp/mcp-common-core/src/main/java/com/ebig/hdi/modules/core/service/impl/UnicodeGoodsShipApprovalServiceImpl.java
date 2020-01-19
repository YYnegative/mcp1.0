package com.ebig.hdi.modules.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.enums.IsMatchEnum;
import com.ebig.hdi.common.enums.OperationTypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.activiti.service.ActApprovalService;
import com.ebig.hdi.modules.core.dao.UnicodeGoodsShipApprovalDao;
import com.ebig.hdi.modules.core.dao.UnicodeGoodsShipHistDao;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：wenchao
 * @date：2019-12-23 16:24
 * @version：V1.0
 */
@Service("unicodeGoodsShipApprovalService")
public class UnicodeGoodsShipApprovalServiceImpl extends ServiceImpl<UnicodeGoodsShipApprovalDao, UnicodeGoodsShipApprovalEntity> implements UnicodeGoodsShipApprovalService {

    @Autowired
    private ActApprovalService actApprovalService;

    @Autowired
    UnicodeGoodsShipService unicodeGoodsShipService;

    @Autowired
    private UnicodeGoodsShipHistDao unicodeGoodsShipHistDao;

    @Override
    public List<UnicodeGoodsShipApprovalEntity> selectNotMatch(Integer limit) {
        return this.baseMapper.selectNotMatch(limit);
    }

    @Override
    public void initiateApproval(SysUserEntity entity, UnicodeGoodsShipApprovalEntity ship,
                                 Integer changeType, Integer actTypeEnumKey, String[] activitiConstant) {
        if (entity != null && ship != null && changeType != null
                && actTypeEnumKey != null && activitiConstant != null) {
            ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(entity, changeType,
                    actTypeEnumKey, ship.getShipId().toString(), null,
                    null);
            Map<String, Object> approvalMap = new HashMap<>(16);
            approvalMap.put("approvalEntity", approvalEntity);
            actApprovalService.insert(approvalEntity);
            ProcessInstance instance = actApprovalService.startProcess(entity.getUserId().toString(),
                    activitiConstant[0],
                    activitiConstant[1], approvalMap);
            if (instance != null) {
                approvalEntity.setProcessId(instance.getProcessInstanceId());
                approvalEntity.setApprovalCode(instance.getProcessInstanceId());
                ship.setProcessId(instance.getProcessInstanceId());
            }
            actApprovalService.updateById(approvalEntity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelMatch(Long shipId,SysUserEntity sysUserEntity) {
        if (shipId == null) {
            throw new HdiException("参数不能为空！");
        }
        UnicodeGoodsShipApprovalEntity entity = this.selectById(shipId);
        if (entity == null) {
            throw new HdiException("不存在关系标识对应的记录");
        }
        entity.setShipFlag(IsMatchEnum.CANCEL.getKey());
        UnicodeGoodsShipHistEntity histEntity = ReflectUitls.transform(entity, UnicodeGoodsShipHistEntity.class);
        histEntity.setOperType(OperationTypeEnum.CANCEL_MATCH.getKey());
        histEntity.setCredate(new Date());
        histEntity.setCremanid(sysUserEntity.getUserId());
        histEntity.setCremanname(sysUserEntity.getUsername());
        histEntity.setProcessId(null);
        unicodeGoodsShipHistDao.insert(histEntity);
        entity.setPapprovalId(null);
        entity.setPgoodsId(null);
        entity.setPspecsId(null);
        this.updateAllColumnById(entity);
        UnicodeGoodsShipEntity shipEntity = ReflectUitls.transform(entity, UnicodeGoodsShipEntity.class);
        unicodeGoodsShipService.updateAllColumnById(shipEntity);
    }
}
