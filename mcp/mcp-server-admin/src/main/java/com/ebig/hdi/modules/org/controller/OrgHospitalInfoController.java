package com.ebig.hdi.modules.org.controller;

import com.ebig.hdi.common.entity.OrgHospitalInfoApprovalEntity;
import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.param.OrgHospitalParam;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoApprovalService;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 医院信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:40
 */
@RestController
@RequestMapping("org/orghospitalinfo")
public class OrgHospitalInfoController extends AbstractController{

    @Autowired
    private OrgHospitalInfoApprovalService orgHospitalInfoApprovalService;

    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    OrgHospitalInfoService orgHospitalInfoService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("org:orghospitalinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = orgHospitalInfoApprovalService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("org:orghospitalinfo:info")
    public Hdi info(@PathVariable("id") Long id){
        OrgHospitalInfoApprovalEntity orgHospitalInfo = orgHospitalInfoApprovalService.selectById(id);

        return Hdi.ok().put("orgHospitalInfo", orgHospitalInfo);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("org:orghospitalinfo:save")
    public Hdi save(@RequestBody OrgHospitalInfoApprovalEntity hospitalInfoApprovalEntity){
        hospitalInfoApprovalEntity.setCreateId(getUserId());
        Map<String,Object> map = orgHospitalInfoApprovalService.save(hospitalInfoApprovalEntity,getUser());
        if (!map.isEmpty()){
           return Hdi.error(map.get("errmessage").toString());
        }
        return Hdi.ok();
    }
    
    /**
     * 根据医院名字查询医院信息List
     * @return
     */
    @PostMapping("/queryByHospitalName")
    @RequiresPermissions("org:orghospitalinfo:queryByHospitalName")
    public Hdi queryByHospitalName(@RequestBody Map<String, Object> params){
    	List<OrgHospitalInfoEntity> hospitalList = orgHospitalInfoService.queryByHospitalName(params);
    	return Hdi.ok().put("orgHospitalInfos", hospitalList);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("org:orghospitalinfo:update")
    public Hdi update(@RequestBody OrgHospitalInfoApprovalEntity orgHospitalInfoApprovalEntity){
        ValidatorUtils.validateEntity(orgHospitalInfoApprovalEntity);
        orgHospitalInfoApprovalEntity.setEditId(getUserId());
        orgHospitalInfoApprovalService.update(orgHospitalInfoApprovalEntity,getUser());

        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("org:orghospitalinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        orgHospitalInfoApprovalService.delete(ids);
        return Hdi.ok();
    }
    /**
     * 导出
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody OrgHospitalParam orgHospitalParam) throws IOException {
        //获取查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.HOSPITAL_TEMPLATE.getKey()));
        //获取查询表头
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.HOSPITAL_TEMPLATE.getKey()));
        List<Map<String,Object>> list=orgHospitalInfoApprovalService.getList(columns,orgHospitalParam);
        ExcelUtils.exportExcel(request,response,list,columnNames,columns,"医院数据","医院列表单");
    }

    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
        orgHospitalInfoApprovalService.toggle(params);

        return Hdi.ok();
    }

    /**
     * 审批状态校验
     * @param params
     * @return
     */
    @PostMapping("/checkstatus")
    public Hdi checkStatus(@RequestBody Map<String,Object> params ){
        Map<String,Object> map = orgHospitalInfoApprovalService.checkStatus(params,getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errmessage").toString());
        }
        return Hdi.ok();
    }

}
