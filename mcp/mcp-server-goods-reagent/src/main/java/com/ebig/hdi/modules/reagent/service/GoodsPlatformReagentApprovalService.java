package com.ebig.hdi.modules.reagent.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentApprovalEntity;
import com.ebig.hdi.modules.reagent.param.GoodsPlatformReagentParam;
import com.ebig.hdi.modules.reagent.vo.GoodsPlatformReagentVO;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 平台试剂信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:29:52
 */
public interface GoodsPlatformReagentApprovalService extends IService<GoodsPlatformReagentApprovalEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    void save(GoodsPlatformReagentVO goodsPlatformReagentVO,SysUserEntity sysUseentity);
    
    void update(GoodsPlatformReagentVO goodsPlatformReagentVO,SysUserEntity sysUserEntity);
    
    GoodsPlatformReagentVO selectById(Long id);

	void toggle(Map<String, Object> params);
    void delete(Long[] ids);
    /**
     * @Description: 平台试剂信息导入
     * @Author: ZhengHaiWen
     * @Date: 2019/11/26
     */
    Map goodsPlatformReagentImportData(String[][] rows, SysUserEntity sysUserEntity);

    List<Map<String, Object>> getList(Map<String, Object> params);

    /**
     * @Description:
     * @Author: ZhengHaiWen
     * @Date: 2019/12/2
     */
    Map<String, Object> checkStatus(Map<String, Object> params, SysUserEntity user) ;

}

