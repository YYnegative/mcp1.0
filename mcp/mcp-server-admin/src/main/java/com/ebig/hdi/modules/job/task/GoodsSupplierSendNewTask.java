package com.ebig.hdi.modules.job.task;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.goods.entity.GoodsSupplierSendEntity;
import com.ebig.hdi.modules.goods.service.GoodsSupplierSendService;
import com.ebig.hdi.modules.goods.service.TempPubSupplyGoodsService;
import com.ebig.hdi.modules.job.dao.TempPubSupplyGoodsDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.entity.TempPubSupplyGoodsEntity;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 医院供应商品目录下发(改造后)
 *
 * @author alan
 * @Date 2019-08-31
 */
@Component("goodsSupplierSendNewTask")
@Slf4j
public class GoodsSupplierSendNewTask implements ITask {

    private static AtomicBoolean isRunning = new AtomicBoolean(false);

    @Autowired
    private GoodsSupplierSendService goodsSupplierSendService;

     @Autowired
    private TempPubSupplyGoodsService tempPubSupplyGoodsService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private TempPubSupplyGoodsDao tempPubSupplyGoodsDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ScheduleJobEntity scheduleJob) {
        synchronized (GoodsSupplierSendNewTask.class) {
            if (isRunning.get()) {
                log.info("GoodsSupplierSendNewTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning.set(true);
        }

        try {
            log.info("GoodsSupplierSendNewTask定时任务正在执行，参数为：{}", scheduleJob.getParams());

            // 获取1000条未上传记录--接收对象改造后为TempPubSupplyGoodsEntity；
            List<TempPubSupplyGoodsEntity> tempPubGoodsList = goodsSupplierSendService.selectNotUploadNew(1000);

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

            for (TempPubSupplyGoodsEntity tempPubGoods : tempPubGoodsList) {
                if (StringUtil.isEmpty(tempPubGoods.getMgoodsid())) {
                    log.error(MessageFormat.format("下发id:{0}商品原系统规格id为空，下发失败！", tempPubGoods.getSendId()));
                    continue;
                }

                if (null != map.get(tempPubGoods.getGoodsunit())) {
                    tempPubGoods.setGoodsunit(map.get(tempPubGoods.getGoodsunit()));
                }

            }
            // 先查询临时表是否存在主键相同的数据
            List<TempPubSupplyGoodsEntity> tempGoodsList = tempPubSupplyGoodsService.selectBatchIds(tempPubGoodsList.stream().map(TempPubSupplyGoodsEntity::getMgoodsid).collect(Collectors.toList()));
            List<TempPubSupplyGoodsEntity> insertList;
            if (CollectionUtils.isNotEmpty(tempGoodsList)) {
                //存新增记录
                insertList = tempPubGoodsList.stream().filter(t -> !tempGoodsList.stream().map(TempPubSupplyGoodsEntity::getMgoodsid).collect(Collectors.toList())
                        .contains(t.getMgoodsid())).collect(Collectors.toList());

                //存修改记录
                List<TempPubSupplyGoodsEntity> updateList = tempPubGoodsList.stream().filter(t -> tempGoodsList.stream().map(TempPubSupplyGoodsEntity::getMgoodsid).collect(Collectors.toList())
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
    private void updateBatchByIds(List<TempPubSupplyGoodsEntity> list) {
        //批量更新

        for (int i = 0; i <list.size() ; i++) {
            tempPubSupplyGoodsDao.updateById(list.get(i));

        }
    }

    /**
     * 批量新增
     * @param list
     */
    private void insertBatch(List<TempPubSupplyGoodsEntity> list) {
        //批量插入

        for (int i = 0; i <list.size() ; i++) {
            tempPubSupplyGoodsDao.insert(list.get(i));

        }
    }

    private void updateBatchGoodsSupplierSend(List<TempPubSupplyGoodsEntity> list) {
        List<GoodsSupplierSendEntity> updateList = new ArrayList<>();
        GoodsSupplierSendEntity goodsSupplierSendEntity;
        for (TempPubSupplyGoodsEntity entity : list) {
            goodsSupplierSendEntity = new GoodsSupplierSendEntity();
            goodsSupplierSendEntity.setId(entity.getSendId());
            goodsSupplierSendEntity.setIsUpload(SupplierIsUploadEnum.YES.getKey());
            updateList.add(goodsSupplierSendEntity);
        }
        goodsSupplierSendService.updateBatchById(updateList);

    }
}

