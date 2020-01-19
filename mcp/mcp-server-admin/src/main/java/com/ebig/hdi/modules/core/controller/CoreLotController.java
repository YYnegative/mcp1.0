package com.ebig.hdi.modules.core.controller;

import com.ebig.hdi.common.enums.TypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.core.entity.CoreLotEntity;
import com.ebig.hdi.modules.core.service.CoreLotService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author frink
 * @email
 * @date 2019-05-25 12:01:14
 */
@RestController
@RequestMapping("/core/corelot")
public class CoreLotController extends AbstractController {
    @Autowired
    private CoreLotService coreLotService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("core:corelot:list")
    public Hdi list(@RequestBody Map<String, Object> params) {
        params.put("deptId", getDeptId());
        PageUtils page = coreLotService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 页面列表条件查询
     */
    @RequestMapping("/listBedingungen")
    //@RequiresPermissions("core:corepurchasemaster:list")
    public Hdi listBedingungen(@RequestBody Map<String, Object> params) {
        params.put("deptId", getDeptId());
        PageUtils page = coreLotService.bedingungenQueryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{lotid}")
    @RequiresPermissions("core:corelot:info")
    public Hdi info(@PathVariable("lotid") Long lotid) {
        CoreLotEntity coreLot = coreLotService.selectById(lotid);

        return Hdi.ok().put("coreLot", coreLot);
    }

    /**
     * 新增
     */
    @RequestMapping("/save")
    //@RequiresPermissions("core:corelot:save")
    public Hdi save(@RequestBody CoreLotEntity coreLot) {
        coreLot.setDeptId(getDeptId());
        coreLotService.save(coreLot, getUser());

        return Hdi.ok();
    }

    /**
     * 编辑保存
     */
    @RequestMapping("/updateLot")
    //@RequiresPermissions("core:corelot:update")
    public Hdi updateLot(@RequestBody CoreLotEntity coreLot) {
        ValidatorUtils.validateEntity(coreLot);
        coreLotService.updateLot(coreLot, getUser());

        return Hdi.ok();
    }

    /**
     * 停用启用
     */
    @RequestMapping("/updateLotstatus")
    //@RequiresPermissions("core:corelot:update")
    public Hdi updateLotstatus(@RequestBody CoreLotEntity coreLot) {
        ValidatorUtils.validateEntity(coreLot);
        coreLotService.updateLot(coreLot, getUser());

        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("core:corelot:delete")
    public Hdi delete(@RequestBody List<CoreLotEntity> coreLotList) {
        coreLotService.deleteLot(coreLotList, getUser());
        return Hdi.ok();
    }

    /**
     * 批量导入
     *
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
            if (rows.length <= 1) {
                return Hdi.ok("导入失败！excel文件数据为空");
            }
            //校验表头数据
            StringBuffer sb = new StringBuffer();
            String[] excelHead = {"供应商商品规格编码*", "生产批号*", "生产日期*", "失效日期*"};
            List<String> excelHeadList = Arrays.asList(excelHead);
            List<String> rowList = Arrays.asList(rows[0]);
            if (excelHeadList.size() > rowList.size()) {
                //缺少字段
                // 差集 (excelHeadList - rowList)
                List<String> reduce1 = excelHeadList.stream().filter(item -> !rowList.contains(item)).collect(Collectors.toList());
                for (int i = 0; i < reduce1.size(); i++) {
                    if (i == reduce1.size() - 1) {
                        sb.append(reduce1.get(i));
                    } else {
                        sb.append(reduce1.get(i) + ",");
                    }
                }
                return Hdi.ok("导入失败！缺少字段" + sb.toString());

            }
            Map<String, String> message = coreLotService.importData(rows, getUserId(), getDeptId());
            if (Integer.parseInt(message.get("failCount")) > 0) {
                return Hdi.ok(message.get("successCount") + "条记录导入成功，\n" + message.get("failCount") + "条记录导入失败，失败原因：" + message.get("errorMessage"));
            }
            return Hdi.ok(message.get("successCount") + "条记录导入成功，" + message.get("failCount") + "条记录导入失败");
        } catch (Exception e) {
            return Hdi.error("导入失败！" + e.getMessage());
        }
    }
}
