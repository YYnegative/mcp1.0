
package com.ebig.hdi.modules.coretransform.task;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.enums.SupplyStatusEnum;
import com.ebig.hdi.modules.core.entity.CoreSupplyMasterEntity;
import com.ebig.hdi.modules.core.service.CorePurchaseDetailService;
import com.ebig.hdi.modules.core.service.CoreSupplyMasterService;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.task.ITask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


@Component("supplyToSpdRgTask")
@Slf4j
public class SupplyToSpdRgTask implements ITask {


    private static AtomicBoolean isRunning = new AtomicBoolean(false);


    @Autowired
    private CorePurchaseDetailService corePurchaseDetailService;

    @Autowired
    private CoreSupplyMasterService coreSupplyMasterService;


    @Override
    public void run(ScheduleJobEntity scheduleJob) {
        synchronized (TransformPoTask.class) {
            if (isRunning.get()) {
                log.info("supplyToSpdRgTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning.set(true);
        }
        try {
            // 查询1000条未提交医院的供货主单数据
            Map<String,Object> params = new HashMap<>(16);
            params.put("limit",1000);
            params.put("supply_status",SupplyStatusEnum.UNCOMMITTED.getKey());
            List<CoreSupplyMasterEntity> coreSupplyMasterList = coreSupplyMasterService.selectListByMap(params);
            if(CollectionUtils.isNotEmpty(coreSupplyMasterList)){
                for (CoreSupplyMasterEntity entity:coreSupplyMasterList) {
                    coreSupplyMasterService.sendCoreSupplyData(entity);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            isRunning.set(false);
        }
    }

}
