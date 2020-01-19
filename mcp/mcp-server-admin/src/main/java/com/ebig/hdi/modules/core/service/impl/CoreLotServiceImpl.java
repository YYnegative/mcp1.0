package com.ebig.hdi.modules.core.service.impl;

import java.text.MessageFormat;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.enums.IsCiteEnum;
import com.ebig.hdi.common.enums.LotTypeEnum;
import com.ebig.hdi.common.utils.DateUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.service.PublicComboDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.dao.CoreLotDao;
import com.ebig.hdi.modules.core.entity.CoreLotEntity;
import com.ebig.hdi.modules.core.service.CoreLotService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;


@Service("coreLotService")
@Slf4j
public class CoreLotServiceImpl extends ServiceImpl<CoreLotDao, CoreLotEntity> implements CoreLotService {

    @Autowired
    private PublicComboDataService publicComboDataService;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "cl")
    public PageUtils queryPage(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        // 手动数据过滤
        if (params.get(Constant.SQL_FILTER) != null) {
            String sqlFilter = params.get(Constant.SQL_FILTER).toString();
            params.put("deptIds", sqlFilter);
        }

        Page<CoreLotEntity> page = new Page<CoreLotEntity>(currPage, pageSize);
        List<CoreLotEntity> list = this.baseMapper.selectByDeptId(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    /**
     * 条件查询
     */
    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "cl")
    public PageUtils bedingungenQueryPage(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        // 手动数据过滤
        if (params.get(Constant.SQL_FILTER) != null) {
            String sqlFilter = params.get(Constant.SQL_FILTER).toString();
            params.put("deptIds", sqlFilter);
        }

        Page<CoreLotEntity> page = new Page<CoreLotEntity>(currPage, pageSize);
        List<CoreLotEntity> list = this.baseMapper.selectByBedingungen(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    /**
     * 新增商品批号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CoreLotEntity coreLot, SysUserEntity user) {
        if (user.getUserType() != 1) {
            throw new HdiException("当前登录用户无此权限");
        }
        List<CoreLotEntity> coreLotEntities = this.baseMapper.getLotInfoByLotno(coreLot);
        if (coreLotEntities.size() > 0) {
            throw new HdiException("该商品批号已存在");
        }

        CoreLotEntity supplierId = this.baseMapper.getSupplierId(user.getDeptId());
        coreLot.setSupplierId(supplierId.getSupplierId());
        coreLot.setDeptId(user.getDeptId());
        coreLot.setLottype(1);
        coreLot.setLotstatus(1);
        coreLot.setDelFlag(0);
        this.baseMapper.insert(coreLot);
    }

    @Override
    public void updateLot(CoreLotEntity coreLot, SysUserEntity user) {
        if (user.getUserType() != 1) {
            throw new HdiException("当前登录用户无此权限");
        }
        this.updateById(coreLot);
    }


    @Override
    public void deleteLot(List<CoreLotEntity> coreLotList, SysUserEntity user) {
        if (user.getUserType() != 1) {
            throw new HdiException("当前登录用户无此权限");
        }
        for (CoreLotEntity coreLotEntity : coreLotList) {
            coreLotEntity.setDelFlag(DelFlagEnum.DELETE.getKey());
            this.baseMapper.updateById(coreLotEntity);
        }
    }


    @Override
    public CoreLotEntity saveLot(Long goodsid, Integer goodsclass, Long goodstypeid, String lotno, Long deptId, Date proddate, Date invadate) {
        CoreLotEntity lotEntity = new CoreLotEntity();
        lotEntity.setDeptId(deptId);
        lotEntity.setGoodsid(goodsid);
        lotEntity.setGoodsclass(goodsclass);
        lotEntity.setGoodstypeid(goodstypeid);
        lotEntity.setLottype(1);
        lotEntity.setLotstatus(1);
        lotEntity.setLotno(lotno);
        lotEntity.setProddate(proddate);
        lotEntity.setInvadate(invadate);

        this.baseMapper.saveLot(lotEntity);
        System.out.println(lotEntity.getLotid());
        return lotEntity;
    }


    //提交医院
    @Override
    public CoreLotEntity selectLot(Long lotid) {
        CoreLotEntity lotEntity = this.baseMapper.selectById(lotid);
        return lotEntity;
    }

    @Override
    public Map<String, String> importData(String[][] rows, Long userId, Long deptId) {
        //保存返回信息
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = new HashMap<>(16);
        if (rows.length <= 1) {
            map.put("successCount", "0");
            map.put("successCount", "0");
            sb.append("excel文件为空");
            map.put("errorMessage", sb.toString());
            return map;
        }
        if (userId == null || deptId == null) {
            map.put("successCount", "0");
            map.put("failCount", String.valueOf(rows.length - 1));
            sb.append("系统错误");
            map.put("errorMessage", sb.toString());
        }
        Integer successCount = 0;
        List<CoreLotEntity> list = new ArrayList<>();
        for (int i = 1; i < rows.length; i++) {
            String[] row = rows[i];
            if (StringUtil.isEmpty(StringUtil.trim(row[0]))) {
                sb.append("第" + (i + 1) + "行必填项供应商商品规格编码为空");
                sb.append("\n");
                continue;
            }
            List<Map<String, Object>> supplierGoodsList = publicComboDataService.querySupplierGoodsInfo(deptId, StringUtil.trim(row[0]));
            if (CollectionUtils.isEmpty(supplierGoodsList)) {
                log.error(MessageFormat.format("生成商品批号信息失败！供应商商品不存在，供应商商品规格编码{0}", row[0]));
                sb.append("第" + (i + 1) + "行必填项供应商商品规格编码数据错误");
                sb.append("\n");
                continue;
            }
            CoreLotEntity lotEntity = new CoreLotEntity();
            lotEntity.setDeptId(deptId);
            lotEntity.setSupplierId(Long.parseLong(supplierGoodsList.get(0).get("supplierId").toString()));
            lotEntity.setGoodsclass(Integer.parseInt(supplierGoodsList.get(0).get("goodsclass").toString()));
            lotEntity.setGoodsid(Long.parseLong(supplierGoodsList.get(0).get("goodsid").toString()));
            lotEntity.setGoodstypeid(Long.parseLong(supplierGoodsList.get(0).get("goodstypeid").toString()));
            lotEntity.setLottype(LotTypeEnum.ONE.getKey());
            lotEntity.setLotstatus(IsCiteEnum.YES.getKey());
            lotEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
            if (StringUtil.isEmpty(StringUtil.trim(row[5]))) {
                sb.append("第" + (i + 1) + "行必填项生产批号为空");
                sb.append("\n");
                continue;
            }
            int count = this.selectCount(new EntityWrapper<CoreLotEntity>().eq("dept_id", deptId).eq("supplier_id", lotEntity.getSupplierId())
                    .eq("goodsid", lotEntity.getGoodsid()).eq("goodsclass", lotEntity.getGoodsclass()).eq("goodstypeid", lotEntity.getGoodstypeid())
                    .eq("del_flag", 0).eq("lotno", StringUtil.trim(row[5])));
            if (count > 0) {
                sb.append("第" + (i + 1)+"行" + supplierGoodsList.get(0).get("goodsName").toString() + "商品已经存在" + row[5] + "批号");
                sb.append("\n");
                continue;
            }

            lotEntity.setLotno(StringUtil.trim(row[5]));
            if (StringUtil.isEmpty(StringUtil.trim(row[6]))) {
                sb.append("第" + (i + 1) + "行必填项生产日期为空");
                sb.append("\n");
                continue;
            }
            Date proddate = DateUtils.stringToDate(StringUtil.trim(row[6]), "yyyy-MM-dd HH:mm:ss");
            lotEntity.setProddate(proddate);
            if (StringUtil.isEmpty(row[7])) {
                sb.append("第" + (i + 1) + "行必填项失效日期为空");
                sb.append("\n");
                continue;
            }
            Date invadate = DateUtils.stringToDate(StringUtil.trim(row[7]), "yyyy-MM-dd HH:mm:ss");
            if (proddate.getTime() > invadate.getTime()) {
                sb.append("第" + (i + 1) + "行必填项生产日期大于了失效日期");
                sb.append("\n");
                continue;
            }
            lotEntity.setInvadate(DateUtils.stringToDate(StringUtil.trim(row[7]), "yyyy-MM-dd HH:mm:ss"));
            list.add(lotEntity);
            successCount++;

        }
        if(CollectionUtils.isNotEmpty(list)&& (rows.length-successCount) == 1){
            this.insertBatch(list);
            map.put("successCount", successCount.toString());
            map.put("failCount", String.valueOf(0));
        }else {
            map.put("successCount", "0");
            map.put("failCount", String.valueOf(rows.length-1));
        }
        map.put("errorMessage", sb.toString());
        return map;
    }


}
