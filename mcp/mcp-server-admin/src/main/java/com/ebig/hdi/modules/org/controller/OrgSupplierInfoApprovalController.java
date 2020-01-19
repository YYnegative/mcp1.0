package com.ebig.hdi.modules.org.controller;

import com.ebig.hdi.common.enums.ApprovalTypeEnum;
import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoApprovalEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.org.param.OrgSupplierInfoParam;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoApprovalService;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商信息待审批表
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-15 09:24:04
 */
@RestController
@RequestMapping("/org/orgsupplierinfo")
public class OrgSupplierInfoApprovalController extends AbstractController {
    @Autowired
    private OrgSupplierInfoApprovalService supplierInfoApprovalService;

    @Autowired
    private OrgSupplierInfoService supplierInfoService;

    @Autowired
    private SysConfigService sysConfigService;
    /**
     * 列表
     */
    @PostMapping("/list")
    @RequiresPermissions("org:orgsupplierinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params) {
        PageUtils page = supplierInfoApprovalService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("org:orgsupplierinfo:info")
    public Hdi info(@PathVariable("id") Long id) {
        OrgSupplierInfoApprovalEntity orgSupplierInfo = supplierInfoApprovalService.selectById(id);

        return Hdi.ok().put("orgSupplierInfo", orgSupplierInfo);
    }

    /**
     * 供应商tree
     */
    @GetMapping(value = "/tree")
    @RequiresPermissions("org:orgsupplierinfo:tree")
    public Hdi tree() {
        //获取子供应商
        List<OrgSupplierInfoApprovalEntity> treeSupplierList = supplierInfoApprovalService.queryTree(new HashMap<String, Object>());
        return Hdi.ok().put("orgSupplierInfo", treeSupplierList);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("org:orgsupplierinfo:save")
    public Hdi save(@RequestBody OrgSupplierInfoApprovalEntity orgSupplierInfo) {
        ValidatorUtils.validateEntity(orgSupplierInfo);
        orgSupplierInfo.setCreateId(getUserId());
        Map<String, Object> map = supplierInfoApprovalService.save(orgSupplierInfo, getUser());
        if (!map.isEmpty()) {
            return Hdi.error(map.get("errmessage").toString());
        }
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("org:orgsupplierinfo:update")
    public Hdi update(@RequestBody OrgSupplierInfoApprovalEntity orgSupplierInfo) {
        ValidatorUtils.validateEntity(orgSupplierInfo);
        supplierInfoApprovalService.update(orgSupplierInfo, getUser());

        return Hdi.ok();
    }

    /**
     * 集团绑定和解绑
     */
    @PostMapping("/binding")
    @RequiresPermissions("org:orgsupplierinfo:binding")
    public Hdi binding(@RequestBody OrgSupplierInfoApprovalEntity entity) {
        if (ApprovalTypeEnum.PASS.getKey().equals(entity.getCheckStatus())) {
            OrgSupplierInfoEntity orgSupplierInfo = ReflectUitls.transform(entity, OrgSupplierInfoEntity.class);
            //校验子机构数量
            if (supplierInfoService.judgeChildNumber(orgSupplierInfo)) {
                throw new HdiException("集团机构已超过限定数量");
            }
            orgSupplierInfo.setEditId(getUserId());
            supplierInfoService.bingding(orgSupplierInfo);
            return Hdi.ok();
        }else {
            return Hdi.error("通过审批的供应商才能绑定或解绑");
        }


    }

    /**
     * 根据供应商名字查询供应商List
     *
     * @param params
     * @return
     */
    @PostMapping("/queryBySupplierName")
    @RequiresPermissions("org:orgsupplierinfo:queryBySupplierName")
    public Hdi queryBySupplierName(@RequestBody Map<String, Object> params) {
        List<OrgSupplierInfoEntity> supplierList = supplierInfoService.queryBySupplierName(params);
        return Hdi.ok().put("orgSupplierInfos", supplierList);
    }


    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("org:orgsupplierinfo:delete")
    public Hdi delete(@RequestBody Long[] ids) {
        supplierInfoApprovalService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

    /**
     * 导出
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody OrgSupplierInfoParam orgSupplierInfoParam) throws IOException {
        //获取查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.SUPPLIER_TEMPLATE.getKey()));
        //获取查询表头
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.SUPPLIER_TEMPLATE.getKey()));
        List<Map<String,Object>> list=supplierInfoApprovalService.getList(columns,orgSupplierInfoParam);
        ExcelUtils.exportExcel(request,response,list,columnNames,columns,"供应商数据","供应商列表单");
    }

    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
        supplierInfoApprovalService.toggle(params);
        return Hdi.ok();
    }
    /**
     *
     */
    @PostMapping("/checkstatus")
    public Hdi checkStatus(@RequestBody Map<String, Object> params) {
        Map<String, Object> map = supplierInfoApprovalService.checkStatus(params,getUser());
        if (!map.isEmpty()) {
            return Hdi.error(map.get("errorMessage").toString());
        }
        return Hdi.ok();
    }
}
