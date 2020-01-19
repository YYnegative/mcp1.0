package com.ebig.hdi.modules.refunds.controller;

import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsMasterVO;
import com.ebig.hdi.modules.refunds.entity.vo.ReturnDetailVo;
import com.ebig.hdi.modules.refunds.entity.vo.ReturnSupplyVo;
import com.ebig.hdi.modules.refunds.entity.vo.SaveRefundsMasterVo;
import com.ebig.hdi.modules.refunds.service.RefundsMasterService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import com.ebig.hdi.modules.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 退货单信息
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
@RestController
@RequestMapping("/refunds/refundsmaster")
public class RefundsMasterController extends AbstractController {
    @Autowired
    private RefundsMasterService refundsMasterService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private SysDictService sysDictService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("refunds:refundsmaster:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = refundsMasterService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("refunds:refundsmaster:info")
    public Hdi info(@PathVariable("id") Long id){
        RefundsMasterVO refundsMasterVO = refundsMasterService.selectById(id);

        return Hdi.ok().put("refundsMaster", refundsMasterVO);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("refunds:refundsmaster:save")
    public Hdi save(@RequestBody RefundsMasterVO refundsMasterVO){
    	refundsMasterVO.setDeptId(getDeptId());
    	refundsMasterVO.setCreateId(getUserId());
        refundsMasterService.save(refundsMasterVO);

        return Hdi.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("refunds:refundsmaster:update")
    public Hdi update(@RequestBody RefundsMasterVO refundsMasterVO){
    	refundsMasterService.update(refundsMasterVO);
        return Hdi.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("refunds:refundsmaster:delete")
    public Hdi delete(@RequestBody Long[] ids){
        refundsMasterService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }
    
    /**
     * 生成退货单数据
     */
    @PostMapping("/submit")
    //@RequiresPermissions("refunds:refundsapplymaster:submit")
    public Hdi submit(@RequestBody Long[] ids){
    	refundsMasterService.submit(ids,getUserId());
    	return Hdi.ok();
    }
    /**
     * 导出退货单
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) throws IOException {
        //获取模板表头和查询字段名
        //获取模板表头和查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.REFUNDS_TEMPLATE.getKey()));
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.REFUNDS_TEMPLATE.getKey()));
        params.put("columns",columns);
        List<Map<String,Object>> list = refundsMasterService.getList(params);
        List<SysDictEntity> dicts =  sysDictService.selectDictByType("goods_unit");
        Map<String,String> dmap = dicts.stream().collect(Collectors.toMap(SysDictEntity::getCode, SysDictEntity::getValue));
        String goodsunit ="goods_unit_code";
        for (Map<String,Object> map :list) {
            if(map.get(goodsunit)!=null ){
                map.put(goodsunit,dmap.get(map.get(goodsunit)));
            }
        }
        ExcelUtils.exportExcel(request,response,list,columnNames,columns,"退货单信息","退货单");
    }

    /**
     * 新增退货单
     * @param refundsMasterVo 退货单自定义vo
     * @return
     */
    @PostMapping("/saverefunds")
    public Hdi saveRefunds(@RequestBody SaveRefundsMasterVo refundsMasterVo){
        Map map = refundsMasterService.saveRefunds(getDeptId(), getUserId(), refundsMasterVo);
        if (map.size()!=0){
            return Hdi.error(map.get("errmessage").toString());
        }
        return Hdi.ok();
    }


    /**
     * 修改退货单
     * @param refundsMasterVo 退货单自定义vo
     * @return
     */
    @PostMapping("/updaterefunds")
    public Hdi updateRefunds(@RequestBody SaveRefundsMasterVo refundsMasterVo){
        Map map = refundsMasterService.updateRefunds(getDeptId(), getUserId(), refundsMasterVo);
        if (map.size()!=0){
            return Hdi.error(map.get("errmessage").toString());
        }
        return Hdi.ok();
    }

    @PostMapping("/deleterefunds")
    public Hdi deleteRefunds(@RequestBody Long[] ids){
        refundsMasterService.deleteRefunds(ids);
        return Hdi.ok();
    }



    /**
     * 返回细单
     */
    @PostMapping("/returndetails")
    public Hdi returnDetails(@RequestBody Map<String,Object> params){
        List<ReturnDetailVo> refundsList = refundsMasterService.returnDetails(params);
        return Hdi.ok().put("refundsList",refundsList);
    }

    /**
     * 供货单号自匹配
     * @param params
     * @return
     */
    @PostMapping("/returnsupplyno")
    public Hdi returnSupplyNo(@RequestBody Map<String,Object> params){
        List<ReturnSupplyVo> supplyNoList = refundsMasterService.returnSupplyNo(getDeptId(),params);
        return Hdi.ok().put("supplyNoList",supplyNoList);
    }


    @PostMapping("/returngoodsspecs")
    public Hdi returnGoodsSpecs(@RequestBody Map<String,Object> params){
        List<Map<String,Object>> specsMap = refundsMasterService.returnGoodsSpecs(params);
        return  Hdi.ok().put("specsMap",specsMap);
    }

    @PostMapping("/returnlot")
    public Hdi returnLot(@RequestBody Map<String,Object> params){
        List<Map<String,Object>> lotMap = refundsMasterService.returnLot(params);
        return Hdi.ok().put("lotMap",lotMap);
    }
}

