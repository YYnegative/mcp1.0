package com.ebig.hdi.modules.common;

import com.ebig.hdi.common.enums.ImportEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesService;
import com.ebig.hdi.modules.core.service.CorePurchaseMasterService;
import com.ebig.hdi.modules.drugs.service.GoodsPlatformDrugsApprovalService;
import com.ebig.hdi.modules.consumables.service.GoodsPlatformConsumablesApprovalService;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsService;
import com.ebig.hdi.modules.org.service.OrgFactoryInfoApprovalService;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoApprovalService;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoApprovalService;
import com.ebig.hdi.modules.reagent.service.GoodsPlatformReagentApprovalService;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentService;
import com.ebig.hdi.modules.refunds.service.RefundsMasterService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @title: ImportController
 * @projectName mcp
 * @description: 导入excel公共类
 * @author：wenchao
 * @date：2019-10-31 9:17
 * @version：V1.0
 */
@RestController
@RequestMapping("/common")
public class ImportController extends AbstractController {


    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private RefundsMasterService refundsMasterService;
    @Autowired
    private OrgFactoryInfoApprovalService orgFactoryInfoService;

    @Autowired
    private OrgHospitalInfoApprovalService orgHospitalInfoApprovalService;

    @Autowired
    private OrgSupplierInfoApprovalService orgSupplierInfoService;
    @Autowired
    private GoodsPlatformDrugsApprovalService goodsPlatformDrugsApprovalService;

    @Autowired
    private GoodsPlatformConsumablesApprovalService goodsPlatformConsumablesApprovalService;


    @Autowired
    private GoodsPlatformReagentApprovalService goodsPlatformReagentApprovalService;
    @Autowired
    private GoodsSupplierDrugsService goodsSupplierDrugsService;

    @Autowired
    private GoodsSupplierReagentService goodsSupplierReagentService;

    @Autowired
    private GoodsSupplierConsumablesService goodsSupplierConsumablesService;

    @Autowired
    private CorePurchaseMasterService corePurchaseMasterService;

    @RequestMapping("/import")
    public Hdi importFile(@RequestParam("file") MultipartFile file, @RequestParam("code") String code) {
        SysDictEntity dictEntity = sysDictService.selectDictByCode("import", code);
        if (dictEntity == null) {
            return Hdi.error("导入失败！找不到对应的服务");
        }
        try {
            String[][] rows = ExcelUtils.readExcelByInput(file.getInputStream(), file.getOriginalFilename(), 1);

            if (rows.length <= 1) {
                return Hdi.ok("导入失败！excel文件数据为空");
            }
            Map<String, String> message = new HashMap<>(16);
            if (ImportEnum.REFUNDS.getValue().equals(dictEntity.getValue())) {
                message = refundsMasterService.importData(rows, getUser());
            }
            if (ImportEnum.FACTORY.getValue().equals(dictEntity.getValue())) {
                message = orgFactoryInfoService.importData(rows, getUser());
            }
            if (ImportEnum.HOSPITAL.getValue().equals(dictEntity.getValue())) {
                message = orgHospitalInfoApprovalService.importData(rows, getUser());
            }
            if (ImportEnum.SUPPLIER.getValue().equals(dictEntity.getValue())) {
                message = orgSupplierInfoService.importData(rows, getUser());
            }
            if (ImportEnum.GOODS_PLATFORM_DRUGS_IMPORT.getValue().equals(dictEntity.getValue())) {
                message = goodsPlatformDrugsApprovalService.importData(rows, getUser());
            }
            if (ImportEnum.GOODSPLATFORMREAGENT.getValue().equals(dictEntity.getValue())) {
                message = goodsPlatformReagentApprovalService.goodsPlatformReagentImportData(rows, getUser());
            }
            if (ImportEnum.GOODSPLATFORMCONSUMABLES.getValue().equals(dictEntity.getValue())) {
                message = goodsPlatformConsumablesApprovalService.importData(rows, getUser());
            }
            if (ImportEnum.GOODS_SUPPLIER_DRUGS_IMPORT.getValue().equals(dictEntity.getValue())) {
                message = goodsSupplierDrugsService.importData(rows, getUser());
            }
            if (ImportEnum.GOODS_SUPPLIER_CONSUMABLES_IMPORT.getValue().equals(dictEntity.getValue())) {
                message = goodsSupplierConsumablesService.importData(rows, getUser());
            }
            if (ImportEnum.GOODS_SUPPLIER_REAGENT_IMPORT.getValue().equals(dictEntity.getValue())) {
                message = goodsSupplierReagentService.importData(rows, getUser());
            }
            if (ImportEnum.PURCHASE_IMPORT.getValue().equals(dictEntity.getValue())) {
                message = corePurchaseMasterService.importData(rows, getUser());
            }
            String key = "failCount";

            if (message.get(key) != null && Integer.parseInt(message.get(key)) > 0) {
                return Hdi.ok(message.get("successCount") + "条记录导入成功，\n" + message.get(key) + "条记录导入失败，失败原因：" + message.get("errorMessage"));
            }
            return Hdi.ok(message.get("successCount") + "条记录导入成功，" + message.get(key) + "条记录导入失败");
        } catch (HdiException e) {
            return Hdi.ok(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Hdi.error("导入失败！" + e.getMessage());
        }
    }

}
