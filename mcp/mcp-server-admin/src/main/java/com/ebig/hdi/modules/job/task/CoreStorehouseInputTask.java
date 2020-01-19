package com.ebig.hdi.modules.job.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.enums.DataSourceEnum;
import com.ebig.hdi.common.enums.IsMatchEnum;
import com.ebig.hdi.common.utils.DateUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.core.service.CoreStorehouseService;
import com.ebig.hdi.modules.job.dao.TempSpdStorehouseDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import com.ebig.hdi.modules.unicode.service.UnicodeSupplyShipService;

import lombok.extern.slf4j.Slf4j;

@Component("coreStorehouseInputTask")
@Slf4j
public class CoreStorehouseInputTask implements ITask {
    @Autowired
    private TempSpdStorehouseDao tempSpdStorehouseDao;

    @Autowired
    private CoreStorehouseService coreStorehouseService;

    @Autowired
    private UnicodeSupplyShipService unicodeSupplyShipService;

    private static AtomicBoolean isRunning = new AtomicBoolean(false);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ScheduleJobEntity scheduleJob) {
        synchronized (CoreStorehouseInputTask.class) {
            if (isRunning.get()) {
                log.info("coreStorehouseInputTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning.set(true);
        }

        try {
            log.info("coreStorehouseInputTask定时任务正在执行，参数为：{}", scheduleJob.getParams());

            // 默认获得前一天临时表数据
            Date beginTime = scheduleJob.getBeginTime();
            Date endTime = scheduleJob.getEndTime();
            if (StringUtil.isEmpty(beginTime) && StringUtil.isEmpty(endTime)) {
                endTime = new Date();
                beginTime = DateUtils.addDateDays(endTime, -1);
            }
            Map<String, Object> args = new HashMap<>();
            args.put("beginTime", beginTime);
            args.put("endTime", endTime);
            List<Map<String, Object>> list = tempSpdStorehouseDao.selectStorehouseList(args);

            if (CollectionUtils.isNotEmpty(list)) {
                // 插入数据，并删除临时表数据
                List<CoreStorehouseEntity> cshyList = new ArrayList<>(list.size());
                for (Map<String, Object> map : list) {
                    CoreStorehouseEntity coreStorehouseEntity = transferToObj(map);
                    cshyList.add(coreStorehouseEntity);
                }
                List<String> orgdataids = cshyList.stream().map(CoreStorehouseEntity::getOrgdataid).collect(Collectors.toList());

                //批量查询
                List<CoreStorehouseEntity> mcpList = coreStorehouseService.selectByOrgdataids(orgdataids);
                List<CoreStorehouseEntity> insertList;
                if (CollectionUtils.isNotEmpty(mcpList)) {
                    //存新增记录
                    insertList = cshyList.stream().filter(t -> !mcpList.stream().map(CoreStorehouseEntity::getOrgdataid).collect(Collectors.toList())
                            .contains(t.getOrgdataid())).collect(Collectors.toList());
                    //存修改记录并设置库房id
                    List<CoreStorehouseEntity> updateList = cshyList.stream().filter(t -> mcpList.stream().map(CoreStorehouseEntity::getOrgdataid)
                            .collect(Collectors.toList()).contains(t.getOrgdataid())).collect(Collectors.toList());
                    for (int i = 0; i < updateList.size(); i++) {
                        for (int j = 0; j < mcpList.size(); j++) {
                            if (updateList.get(i).getOrgdataid().equals(mcpList.get(j).getOrgdataid())) {
                                updateList.get(i).setStorehouseid(mcpList.get(j).getStorehouseid());
                            }
                        }
                    }
                    //批量更新
                    if (CollectionUtils.isNotEmpty(updateList)) {
                        updateBatchByIds(updateList);
                    }

                } else {
                    insertList = cshyList;
                }
                if (CollectionUtils.isNotEmpty(insertList)) {
                    insertBatch(insertList);
                }
                //删除临时表数据
                if (CollectionUtils.isNotEmpty(orgdataids)) {
                    deleteBatchIds(orgdataids);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            isRunning.set(false);
        }

    }

    /**
     * 根据id批量删除spd_storehouse表数据
     *
     * @param orgdataids
     */
    private void deleteBatchIds(List<String> orgdataids) {
        tempSpdStorehouseDao.deleteBatchIds(orgdataids);
    }

    /**
     * 批量更新
     *
     * @param updateList
     */
    private void updateBatchByIds(List<CoreStorehouseEntity> updateList) {
        coreStorehouseService.updateBatchById(updateList);
    }

    /**
     * 批量新增
     *
     * @param list
     */
    private void insertBatch(List<CoreStorehouseEntity> list) {
        for (CoreStorehouseEntity entity : list) {
            List<UnicodeSupplyShipEntity> usslist = unicodeSupplyShipService.selectList(new EntityWrapper<UnicodeSupplyShipEntity>()
                    .eq("ship_flag", IsMatchEnum.YES.getKey())
                    .eq("sources_hospital_id", entity.getUorganid()));
            if (CollectionUtils.isNotEmpty(usslist)) {
                entity.setHorgId(usslist.get(0).getHospitalId());
                entity.setDataSource(DataSourceEnum.PORT.getKey());
            }

        }
        //批量新增
        coreStorehouseService.insertBatch(list);
    }

    /**
     * 数据转换
     *
     * @param map
     * @return
     */
    private CoreStorehouseEntity transferToObj(Map<String, Object> map) {
        CoreStorehouseEntity coreStorehouse = new CoreStorehouseEntity();
        coreStorehouse.setStorehousename((String) map.get("storehousename"));
        coreStorehouse.setStorehouseno((String) map.get("storehouseno"));
        coreStorehouse.setShaddress((String) map.get("shaddress"));
        coreStorehouse.setOrgdataid((String) map.get("storehouseid"));
        coreStorehouse.setUorganid((String) map.get("uorganid"));
        coreStorehouse.setCredate((Timestamp) map.get("credate"));
        coreStorehouse.setEditdate((Timestamp) map.get("editdate"));
        coreStorehouse.setMemo((String) map.get("memo"));

        return coreStorehouse;
    }

//    @Transactional(rollbackFor = Exception.class)
//    private void input(CoreStorehouseEntity coreStorehouse) {
//
//        CoreStorehouseEntity mcpStorehouse = coreStorehouseDao.selectByOrgdataid(coreStorehouse.getOrgdataid());
//
//        if (mcpStorehouse != null) {
//            coreStorehouse.setStorehouseid(mcpStorehouse.getStorehouseid());
//            coreStorehouseDao.updateById(coreStorehouse);
//        } else {
//            List<UnicodeSupplyShipEntity> list = unicodeSupplyShipDao.selectList(new EntityWrapper<UnicodeSupplyShipEntity>()
//                    .eq("ship_flag", IsMatchEnum.YES.getKey())
//                    .eq("sources_hospital_id", coreStorehouse.getUorganid()));
//            if (StringUtil.isEmpty(list)) {
//                return;
//            }
//            coreStorehouse.setHorgId(list.get(0).getHospitalId());
//            coreStorehouse.setDataSource(DataSourceEnum.PORT.getKey());
//            coreStorehouseDao.insert(coreStorehouse);
//        }
//        if (!StringUtil.isEmpty(coreStorehouse.getOrgdataid())) {
//            tempSpdStorehouseDao.deleteById(coreStorehouse.getOrgdataid());
//        }
//
//
//    }

}
