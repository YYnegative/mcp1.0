package com.ebig.hdi.modules.org.controller;

import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.org.param.OrgFactoryParam;
import com.ebig.hdi.modules.org.service.OrgFactoryInfoApprovalService;
import com.ebig.hdi.modules.org.service.OrgFactoryInfoService;
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
 * 厂商信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:40
 */
@RestController
@RequestMapping("org/orgfactoryinfo")
public class OrgFactoryInfoController extends AbstractController {
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private OrgFactoryInfoApprovalService orgFactoryInfoApprovalService;

    @Autowired
    private OrgFactoryInfoService orgFactoryInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("org:orgfactoryinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params) {
        PageUtils page = orgFactoryInfoApprovalService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("org:orgfactoryinfo:info")
    public Hdi info(@PathVariable("id") Long id) {
        OrgFactoryInfoApprovalEntity orgFactoryInfo = orgFactoryInfoApprovalService.selectById(id);

        return Hdi.ok().put("orgFactoryInfo", orgFactoryInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("org:orgfactoryinfo:save")
    public Hdi save(@RequestBody OrgFactoryInfoApprovalEntity orgFactoryInfoApproval) {
        ValidatorUtils.validateEntity(orgFactoryInfoApproval);
        orgFactoryInfoApproval.setDeptId(getDeptId());
        orgFactoryInfoApproval.setCreateId(getUserId());
        Map<String, Object> map = orgFactoryInfoApprovalService.save(orgFactoryInfoApproval, getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errmessage").toString());
        }
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("org:orgfactoryinfo:update")
    public Hdi update(@RequestBody OrgFactoryInfoApprovalEntity orgFactoryInfoApproval) {
        ValidatorUtils.validateEntity(orgFactoryInfoApproval);
        orgFactoryInfoApproval.setEditId(getUserId());
        orgFactoryInfoApprovalService.update(orgFactoryInfoApproval, getUser());

        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("org:orgfactoryinfo:delete")
    public Hdi delete(@RequestBody Long[] ids) {
        orgFactoryInfoApprovalService.delete(ids);

        return Hdi.ok();
    }

    /**
     * 根据厂商名称查询(查询启用和审批通过的厂商)
     */
    @RequestMapping("/queryByFactoryName")
    //@RequiresPermissions("org:orgfactoryinfo:queryByFactoryName")
    public Hdi queryByFactoryName(@RequestBody Map<String, Object> params) {
        List<OrgFactoryInfoEntity> orgFactoryInfos = orgFactoryInfoService.queryByFactoryName(params);
        return Hdi.ok().put("orgFactoryInfos", orgFactoryInfos);
    }

    /**
     * 厂商管理导出
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody OrgFactoryParam queryParam) throws IOException {
        //获取查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.FACTORY_TEMPLATE.getKey()));
        //获取查询表头
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.FACTORY_TEMPLATE.getKey()));
        List<Map<String, Object>> list = orgFactoryInfoApprovalService.getList(columns, queryParam);
        ExcelUtils.exportExcel(request, response, list, columnNames, columns, "厂商数据", "厂商列表单");

    }

    /**
     * 厂商启用停用
     */
    @PostMapping("/toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params) {
        orgFactoryInfoApprovalService.toggle(params);
        return Hdi.ok();
    }

    /**
     * 厂商状态审核接口
     * @param params
     * @return
     */
    @RequestMapping("/checkStatus")
    public Hdi check(@RequestBody Map<String,Object> params) {
        Map<String, Object> map = orgFactoryInfoApprovalService.checkStatus(params,getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errorMessage").toString());
        }
        return Hdi.ok();
    }
}
