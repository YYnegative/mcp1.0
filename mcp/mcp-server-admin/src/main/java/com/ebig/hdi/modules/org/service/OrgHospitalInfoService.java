package com.ebig.hdi.modules.org.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.param.OrgFactoryParam;
import com.ebig.hdi.modules.org.param.OrgHospitalParam;

/**
 * 医院信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:40
 */
public interface OrgHospitalInfoService extends IService<OrgHospitalInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    void insertValidate(OrgHospitalInfoEntity orgHospitalInfo);
    
    void updateValidate(OrgHospitalInfoEntity orgHospitalInfo);
    
    /**
     * 通过医院名字查询医院信息List
     * @param params
     * @return
     */
    List<OrgHospitalInfoEntity> queryByHospitalName(Map<String, Object> params);

	void delete(Long[] ids);

    /**
     * @Description: 批量导入
     * @Param: [rows, userId, deptId]
     * @return: java.util.Map
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/7
     */
    Map importData(String[][] rows, Long userId, Long deptId);

    /**
     * @Description: 根据查询条件导出
     * @Param: [columns, orgHospitalParam]
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/9
     */
    List<Map<String, Object>> getList(String[] columns, OrgHospitalParam orgHospitalParam);

    /**
     * @Description: 启用停用
     * @Param: [params]
     * @return: void
     * @Author: ZhengHaiWen
     * @Modified :
     * @Date: 2019/11/9
     */
    void toggle(Map<String, Object> params);

    List<OrgHospitalInfoEntity> selectByHospitalName(String hospitalName);
}

