package com.ebig.hdi.modules.core.controller;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.enums.TypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.core.entity.CoreAcceptDetailEntity;
import com.ebig.hdi.modules.core.entity.CoreAcceptMasterEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyDetailEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyMasterEntity;
import com.ebig.hdi.modules.core.param.CoreSupplyMasterlParam;
import com.ebig.hdi.modules.core.service.CoreSupplyMasterService;
import com.ebig.hdi.modules.report.service.CoreSupplyReportService;
import com.ebig.hdi.modules.report.vo.CoreSupplyDetailVo;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * --供货单管理--
 *
 * @author frink
 * @email 
 * @date 2019-05-26 18:26:03
 */
@RestController
@RequestMapping("/core/coresupplymaster")
@Slf4j
public class CoreSupplyMasterController extends AbstractController{
    @Autowired
    private CoreSupplyMasterService coreSupplyMasterService;

    @Autowired
    private SysConfigService sysConfigService;


    @Autowired
    private CoreSupplyReportService CoreSupplyReportService;
    /**
     *功能：获取打印的批次码PDF文件
     *@author frink
     */
    @PostMapping("/getBatchCodePdf")
    public void getLabelPdf(HttpServletResponse response, @RequestBody List<CoreSupplyDetailEntity> entityList) throws Exception{
        coreSupplyMasterService.getBatchCodePdf(response, entityList,getUserId());
    }
    
    
    /**
     * 页面主单列表
     */
    @RequestMapping("/listMaster")
    //@RequiresPermissions("core:coresupplymaster:list")
    public Hdi listMaster(@RequestBody Map<String, Object> params){
    	params.put("deptId", getDeptId());
        PageUtils page = coreSupplyMasterService.queryPage(params);

        return Hdi.ok().put("page", page);
    }
    
    
    /**
     * 页面主单列表条件查询
     */
    @RequestMapping("/listBedingungen")
    //@RequiresPermissions("core:corepurchasemaster:list")
    public Hdi listBedingungen(@RequestBody Map<String, Object> params){
    	params.put("deptId", getDeptId());
        PageUtils page = coreSupplyMasterService.bedingungenQueryPage(params);
        
        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{supplyMasterId}")
    @RequiresPermissions("core:coresupplymaster:info")
    public Hdi info(@PathVariable("supplyMasterId") Long supplyMasterId){
        CoreSupplyMasterEntity coreSupplyMaster = coreSupplyMasterService.selectById(supplyMasterId);

        return Hdi.ok().put("coreSupplyMaster", coreSupplyMaster);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("core:coresupplymaster:save")
    public Hdi save(@RequestBody MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterdetailsCommonEntity) throws IOException {

        coreSupplyMasterService.save(masterdetailsCommonEntity,getDeptId(),getUserId(),getUser().getUsername(),getUser());

        return Hdi.ok();
    }

    /**
     * 修改状态
     */
    @RequestMapping("/updateSupplyStatus")
    //@RequiresPermissions("core:coresupplymaster:update")
    public Hdi updateSupplyStatus(@RequestBody CoreSupplyMasterEntity coreSupplyMaster){
        ValidatorUtils.validateEntity(coreSupplyMaster);
        coreSupplyMasterService.updateSupplyStatus(coreSupplyMaster,getUser());//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("core:coresupplymaster:delete")
    public Hdi delete(@RequestBody List<CoreSupplyMasterEntity> listEntity){
        coreSupplyMasterService.deleteMaster(listEntity,getUser());

        return Hdi.ok();
    }
    
    
    /**
     * 供货单编辑 数据展示
     */
    @RequestMapping("/editList")
    //@RequiresPermissions("core:coresupplymaster:update")
    public Hdi editList(@RequestBody CoreSupplyMasterEntity coreSupplyMaster){
        ValidatorUtils.validateEntity(coreSupplyMaster);
        MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> page = coreSupplyMasterService.editList(coreSupplyMaster,getUser());
        
        return Hdi.ok().put("page", page);
    }
    
    
    /**
     * 编辑 数据保存
     */
    @RequestMapping("/editSave")
    //@RequiresPermissions("core:coresupplymaster:update")
    public Hdi editSave(@RequestBody MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> entity){
        ValidatorUtils.validateEntity(entity);
        coreSupplyMasterService.editSave(entity,getUserId(),getUser().getUsername(),getDeptId());
        
        return Hdi.ok();
    }
    
    
    /**
     * 供货验收数据展示查询
     */
    @RequestMapping("/supplyAcceptList")
    //@RequiresPermissions("core:coresupplymaster:update")
    public Hdi supplyAcceptList(@RequestBody CoreSupplyMasterEntity coreSupplyMaster){
        ValidatorUtils.validateEntity(coreSupplyMaster);
        MasterDetailsCommonEntity<CoreAcceptMasterEntity, CoreAcceptDetailEntity> page = coreSupplyMasterService.supplyAcceptList(coreSupplyMaster,getUser());
        
        return Hdi.ok().put("page", page);
    }
    
    
    
    /**
     * 供货验收数据保存
     */
    @RequestMapping("/supplyAcceptSave")
    //@RequiresPermissions("core:coresupplymaster:save")
    public Hdi supplyAcceptSave(@RequestBody  MasterDetailsCommonEntity<CoreAcceptMasterEntity, CoreAcceptDetailEntity> masterdetailsCommonEntity){
        coreSupplyMasterService.supplyAcceptSave(masterdetailsCommonEntity,getDeptId(),getUserId(),getUser().getUsername());
        
        coreSupplyMasterService.updateSupplyStatus(masterdetailsCommonEntity.getMaster().getSourceid());

        return Hdi.ok();
    }


    /**
     * 查询标签数据
     */
    @RequestMapping("/supplyLabelList")
    //@RequiresPermissions("core:coresupplymaster:save")
    public Hdi supplyLabelList(@RequestBody CoreSupplyMasterEntity coreSupplyMaster ){
        MasterDetailsCommonEntity<CoreSupplyMasterEntity,CoreSupplyDetailEntity> page = coreSupplyMasterService.supplyLabelList(coreSupplyMaster,getUser());

        return Hdi.ok().put("page", page);
    }
    
    
    /**
     * 保存标签
     */
    @RequestMapping("/supplyLabelSave")
    //@RequiresPermissions("core:coresupplymaster:save")
    public Hdi supplyLabelSave(@RequestBody  MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterdetailsCommonEntity) throws IOException{
        Long labelId = coreSupplyMasterService.supplyLabelSave(masterdetailsCommonEntity,getDeptId(),getUserId(),getUser().getUsername());
        return Hdi.ok().put("labelId",labelId);
    }
    /**
     *一键生成标签
     */
    @RequestMapping("/autoSupplyLabelSave")
    public Map<String, Object> autoSupplyLabelSave(@RequestBody  MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterdetailsCommonEntity) throws IOException{
        Map<String, Object> map = coreSupplyMasterService.autoSupplyLabelSave(masterdetailsCommonEntity, getDeptId(), getUserId(), getUser().getUsername());
        return map;
    }

    /**
     *    提交医院
     */
    @RequestMapping("/submitToHospital")
    //@RequiresPermissions("core:coresupplymaster:save")
    public Hdi submitToHospital(@RequestBody  CoreSupplyMasterEntity entity){
        coreSupplyMasterService.submitToHospital(entity,getUser());

        return Hdi.ok();
    }

    /**
     * 批量导入
     * @param file
     * @return
     */
    @RequestMapping("/import")
    public Hdi importFile(@RequestParam("file") MultipartFile file) {
        if (!getUser().getUserType().equals(TypeEnum.USER_SUPPLIER.getKey())) {
            throw new HdiException("当前登录用户无此权限");
        }
        try {
            String[][] rows = ExcelUtils.readExcelByInput(file.getInputStream(), file.getOriginalFilename(), 1);

            if(rows.length <= 1){
                return Hdi.ok("导入失败！excel文件数据为空");
            }
            //校验表头数据
            //校验表头数据
            StringBuffer sb = new StringBuffer();
            String[] excelHead = {"医院名称*", "库房名称*", "供货类型*", "供货时间*","供应商商品规格编码*",
            "供货数量*","供货单价*","生产批号*","生产日期*","失效日期*"};
            List<String> excelHeadList = Arrays.asList(excelHead);
            List<String> rowList = Arrays.asList(rows[0]);
            if (excelHeadList.size() > rowList.size()) {
                //缺少字段
                // 差集 (excelHeadList - rowList)
                List<String> reduce1 = excelHeadList.stream().filter(item -> !rowList.contains(item)).collect(Collectors.toList());
                for (int i = 0; i < reduce1.size(); i++) {
                    if(i == reduce1.size() -1){
                        sb.append(reduce1.get(i));
                    }else {
                        sb.append(reduce1.get(i)+",");
                    }
                }
                return Hdi.ok("导入失败！缺少字段" + sb.toString());
            }
            Map<String,String> message = coreSupplyMasterService.importData(rows,getUserId(),getDeptId());
            if(Integer.parseInt(message.get("failCount") )> 0 ){
                return Hdi.ok(message.get("successCount")+"条记录导入成功，\n"+message.get("failCount")+"条记录导入失败，失败原因："+message.get("errorMessage"));
            }
            return Hdi.ok(message.get("successCount")+"条记录导入成功，"+message.get("failCount")+"条记录导入失败");
        } catch (Exception e) {
            return Hdi.error("导入失败！"+e.getMessage());
        }
    }


    /**
     * 批量导出
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody CoreSupplyMasterlParam queryParam) throws IOException {
        //获取查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.SUIILY_ACCEPT_LIST_MASTER.getKey()));
        //获取查询表头
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.SUIILY_ACCEPT_LIST_MASTER.getKey()));
        HashMap<String, Object> map = new HashMap<>();
        map.put("columns",columns);
        map.put("queryParam",queryParam);
        List<Map<String, Object>> list = coreSupplyMasterService.getList(map);
        ExcelUtils.exportExcel(request, response, list, columnNames, columns, "验收单列表", "验收单信息");
    }

    @RequestMapping("/report")
    public void report(@RequestBody Long id, HttpServletResponse response) {

//        ClassPathResource resource = new ClassPathResource("/static/coresupply_master.jrxml");
//        ClassPathResource resource1 = new ClassPathResource("/static/coresupply_master.jasper");
//        String jrxmlPath = resource.getPath();
//        String jasperPath = resource1.getPath();
        //Thread.currentThread().getContextClassLoader().getResourceAsStream("static/coresupply_master.jrxml").getPath();

        String jrxmlPath = Thread.currentThread().getContextClassLoader().getResource("static/coresupply_master.jrxml").getPath();
        String jasperPath = Thread.currentThread().getContextClassLoader().getResource("static/coresupply_master.jasper").getPath();
        if (getUser() != null) {
            Map<String, Object> parameters = new HashMap<>(16);
            InputStream isRef = null;
            ServletOutputStream sosRef = null;
            response.setCharacterEncoding("UTF-8");
            response.reset();
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            try {
                JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
                isRef = new FileInputStream(jasperPath);
                sosRef = response.getOutputStream();
                List<CoreSupplyDetailVo> list = CoreSupplyReportService.getListBySupplyMasterId(id);
                JasperRunManager.runReportToPdfStream(isRef, sosRef, parameters, new JRBeanCollectionDataSource(list));
//                response.setContentType("application/pdf");
//              JasperReport jasperReport = (JasperReport) JRLoader.loadObject(isRef);
//                 JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
//                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,new JRBeanCollectionDataSource(list));
                response.setContentType("application/pdf");
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                try {
                    if (sosRef != null) {
                        sosRef.flush();
                        sosRef.close();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
                try {
                    if (isRef != null) {
                        isRef.close();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }

        }
    }
}
