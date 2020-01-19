
package com.ebig.hdi.modules.activiti.actlistener;

import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import org.activiti.engine.delegate.DelegateTask;

import java.util.Map;

/**
 * @author wenchao
 * @date 2019/11/11.
 * @email 154040976@qq.com
 * <p>
 * 自定义机构变更 监听器
 */
public class OrgApprovalListener extends ActNodeListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        super.notify(delegateTask);
        Map<String, Object> map = delegateTask.getVariables();
        ActApprovalEntity entity = (ActApprovalEntity) map.get("approvalEntity");
        delegateTask.addCandidateUser(entity.getUserId());
    }
}
