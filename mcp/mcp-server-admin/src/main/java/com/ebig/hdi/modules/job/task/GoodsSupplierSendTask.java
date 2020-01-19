package com.ebig.hdi.modules.job.task;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.goods.entity.GoodsSupplierSendEntity;
import com.ebig.hdi.modules.goods.service.GoodsSupplierSendService;
import com.ebig.hdi.modules.goods.service.TempPubGoodsService;
import com.ebig.hdi.modules.job.dao.TempPubGoodsDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.entity.TempPubGoodsEntity;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import com.ebig.hdi.modules.sys.service.SysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 医院供应商品目录下发
 *
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-07-31 03:38:09
 */
@Component("goodsSupplierSendTask")
@Slf4j
public class GoodsSupplierSendTask implements ITask {

    private static AtomicBoolean isRunning = new AtomicBoolean(false);

    @Autowired
    private GoodsSupplierSendService goodsSupplierSendService;

    @Autowired
    private TempPubGoodsService tempPubGoodsService;

   @Autowired
    private TempPubGoodsDao tempPubGoodsDao;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ScheduleJobEntity scheduleJob) {
        synchronized (GoodsSupplierSendTask.class) {
            if (isRunning.get()) {
                log.info("goodsSupplierSendTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning.set(true);
        }

        try {
            log.info("goodsSupplierSendTask定时任务正在执行，参数为：{}", scheduleJob.getParams());

            // 获取1000条未上传记录
            List<TempPubGoodsEntity>  tempPubGoodsList= goodsSupplierSendService.selectNotUpload(1000);

            if (CollectionUtils.isEmpty(tempPubGoodsList)){
               log.info("未有供应商商品下发目录");
               return;
            }
            //转换商品单位字典值为真实值
            List<SysDictEntity> goodsUnitList = sysDictService.selectDictByType("goods_unit");
            Map<String, String> map = new HashMap<>();
            if (CollectionUtils.isNotEmpty(goodsUnitList)) {
                map = goodsUnitList.stream().collect(Collectors.toMap(SysDictEntity::getCode, SysDictEntity::getValue));
            }
            String consumablesType = sysConfigService.getValue("SOURCE_CONSUMABLES");
            for (TempPubGoodsEntity tempPubGoods : tempPubGoodsList) {
                if (StringUtil.isEmpty(tempPubGoods.getMgoodsid())) {
                    log.error(MessageFormat.format("下发id:{0}商品原系统规格id为空，下发失败！", tempPubGoods.getSendId()));
                    continue;
                }

                if (null != map.get(tempPubGoods.getGoodsunit())) {
                    tempPubGoods.setGoodsunit(map.get(tempPubGoods.getGoodsunit()));
                }
                //设置储存条件
                String storeWay = tempPubGoods.getStoreWay();
                if (!StringUtil.isEmpty(storeWay)) {
                    tempPubGoods.setGoodsprop(new BigDecimal(storeWay));
                }

                if (!StringUtil.isEmpty(consumablesType) && Integer.valueOf(consumablesType).equals(tempPubGoods.getGoodscategorytype())) {
                    //商品为耗材类型时，将批准文号字段赋值给注册证号
                    tempPubGoods.setRegisterdocno(tempPubGoods.getApprovedocno());
                    tempPubGoods.setApprovedocno(null);
                }

            }
            // 先查询临时表是否存在主键相同的数据
            List<TempPubGoodsEntity> tempGoodsList = tempPubGoodsService.selectBatchIds(tempPubGoodsList.stream().map(TempPubGoodsEntity::getMgoodsid).collect(Collectors.toList()));
            List<TempPubGoodsEntity> insertList;
            if (CollectionUtils.isNotEmpty(tempGoodsList)) {
                //存新增记录
                insertList = tempPubGoodsList.stream().filter(t -> !tempGoodsList.stream().map(TempPubGoodsEntity::getMgoodsid).collect(Collectors.toList())
                        .contains(t.getMgoodsid())).collect(Collectors.toList());

                //存修改记录
                List<TempPubGoodsEntity> updateList = tempPubGoodsList.stream().filter(t -> tempGoodsList.stream().map(TempPubGoodsEntity::getMgoodsid).collect(Collectors.toList())
                        .contains(t.getMgoodsid())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(updateList)){
                    updateBatchByIds(updateList);
                }

            } else {
                insertList = tempPubGoodsList;
            }
            if(CollectionUtils.isNotEmpty(insertList)){
                insertBatch(insertList);
            }
            if(CollectionUtils.isNotEmpty(tempPubGoodsList)){
                updateBatchGoodsSupplierSend(tempPubGoodsList);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            isRunning.set(false);
        }
    }

    /**
     * 批量更新
     * @param list
     */
    private void updateBatchByIds(List<TempPubGoodsEntity> list) {
        Date date = new Date();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setEditdate(date);
            list.get(i).setHospitalflag(0);
            tempPubGoodsDao.updateById(list.get(i));
        }
    }

    /**
     * 批量新增
     * @param list
     */
    private void insertBatch(List<TempPubGoodsEntity> list) {
        //批量插入
        Date date = new Date();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setUdflag(1);
            list.get(i).setInputdate(date);
            list.get(i).setEditdate(date);
            tempPubGoodsDao.insert(list.get(i));
        }
    }

    private void updateBatchGoodsSupplierSend(List<TempPubGoodsEntity> list) {
        List<GoodsSupplierSendEntity> updateList = new ArrayList<>();
        GoodsSupplierSendEntity goodsSupplierSendEntity;
        for (TempPubGoodsEntity entity : list) {
            goodsSupplierSendEntity = new GoodsSupplierSendEntity();
            goodsSupplierSendEntity.setId(entity.getSendId());
            goodsSupplierSendEntity.setIsUpload(SupplierIsUploadEnum.YES.getKey());
            updateList.add(goodsSupplierSendEntity);
        }
        goodsSupplierSendService.updateBatchById(updateList);

    }
}
