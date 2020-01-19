package com.ebig.hdi.modules.org.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ebig.hdi.common.enums.ApprovalTypeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierHospitalRefEntity;
import com.ebig.hdi.modules.org.service.OrgSupplierHospitalRefService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 供应商医院绑定关系
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:39
 */
@RestController
@RequestMapping("org/orgsupplierhospitalref")
public class OrgSupplierHospitalRefController extends AbstractController {

    @Autowired
    private OrgSupplierHospitalRefService orgSupplierHospitalRefService;

    /**
     * 供应商绑定医院
     */
    @RequestMapping("/binding")
    @RequiresPermissions("org:orgsupplierhospitalref:binding")
    public Hdi binding(@RequestBody Map<String, Object> params) {
        if (ApprovalTypeEnum.PASS.getKey().equals(params.get("checkStatus"))) {
            params.put("createId", getUserId());
            try {
                orgSupplierHospitalRefService.binding(params);
            } catch (Exception e) {
                return Hdi.error("供应商未授权不能绑定");
            }
            return Hdi.ok();
        } else {
            return Hdi.error("通过审批的供应商才可以绑定医院");
        }
    }

    @RequestMapping("/queryAllHospital/{id}")
    public Hdi queryAllHospital(@PathVariable("id") Long id) {
        List<OrgHospitalInfoEntity> list = orgSupplierHospitalRefService.queryAllHospital(id);
        return Hdi.ok().put("orgHospitalInfoList", list);
    }

    @RequestMapping("/queryMatchHospital/{id}")
    public Hdi queryMatchHospital(@PathVariable("id") Long id) {
        List<OrgHospitalInfoEntity> list = orgSupplierHospitalRefService.queryMatchHospital(id);
        return Hdi.ok().put("orgHospitalInfoList", list);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("org:orgsupplierhospitalref:list")
    public Hdi list(@RequestParam Map<String, Object> params) {
        PageUtils page = orgSupplierHospitalRefService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("org:orgsupplierhospitalref:info")
    public Hdi info(@PathVariable("id") Long id) {
        OrgSupplierHospitalRefEntity orgSupplierHospitalRef = orgSupplierHospitalRefService.selectById(id);

        return Hdi.ok().put("orgSupplierHospitalRef", orgSupplierHospitalRef);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("org:orgsupplierhospitalref:save")
    public Hdi save(@RequestBody OrgSupplierHospitalRefEntity orgSupplierHospitalRef) {
        orgSupplierHospitalRefService.insert(orgSupplierHospitalRef);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("org:orgsupplierhospitalref:update")
    public Hdi update(@RequestBody OrgSupplierHospitalRefEntity orgSupplierHospitalRef) {
        ValidatorUtils.validateEntity(orgSupplierHospitalRef);
        orgSupplierHospitalRefService.updateAllColumnById(orgSupplierHospitalRef);//全部更新

        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("org:orgsupplierhospitalref:delete")
    public Hdi delete(@RequestBody Long[] ids) {
        orgSupplierHospitalRefService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
