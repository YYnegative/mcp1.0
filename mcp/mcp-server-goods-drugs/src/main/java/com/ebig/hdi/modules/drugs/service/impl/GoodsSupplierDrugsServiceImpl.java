package com.ebig.hdi.modules.drugs.service.impl;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.modules.core.service.OrgFactoryInfoApprovalService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.FactoryStatusEnum;
import com.ebig.hdi.common.enums.IsMatchEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.enums.SupplierDataSource;
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.common.enums.TypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.drugs.dao.GoodsSupplierDrugsDao;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.entity.OrgFactoryEntity;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsService;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsSpecsService;
import com.ebig.hdi.modules.drugs.service.OrgFactoryService;
import com.ebig.hdi.modules.drugs.vo.GoodsSupplierDrugsEntityVo;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;

@Service("goodsSupplierDrugsService")
public class GoodsSupplierDrugsServiceImpl extends ServiceImpl<GoodsSupplierDrugsDao, GoodsSupplierDrugsEntity>
        implements GoodsSupplierDrugsService {

    @Autowired
    private GoodsSupplierDrugsSpecsService goodsSupplierDrugsSpecsService;

    @Autowired
    private OrgFactoryService orgFactoryService;

    @Autowired
    private SysSequenceService sysSequenceService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private OrgFactoryInfoApprovalService orgFactoryInfoApprovalService;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "d")
    public PageUtils queryPage(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());

        Page<GoodsSupplierDrugsEntityVo> page = new Page<GoodsSupplierDrugsEntityVo>(currPage, pageSize);

        List<GoodsSupplierDrugsEntityVo> list = this.baseMapper.selectSupplierDrugsList(page, params);

        for (GoodsSupplierDrugsEntityVo goodsSupplierDrugs : list) {
            List<GoodsSupplierDrugsSpecsEntity> specsList = goodsSupplierDrugsSpecsService.selectListByDrugsId(goodsSupplierDrugs.getId());
            goodsSupplierDrugs.setSpecsEntityList(specsList);
        }

        page.setRecords(list);

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,String> save(GoodsSupplierDrugsEntityVo goodsSupplierDrugsEntityVo,SysUserEntity user) {
        Map<String,String> errorMap = new HashMap<>(16);
        //批准文号/注册证号不允许重复
        Integer approvalsExist = this.baseMapper.selectByApprovals(goodsSupplierDrugsEntityVo.getApprovals());
        if (approvalsExist > 0) {
            errorMap.put("errorMessage" ,MessageFormat.format("批准文号/注册文号:{0}，已存在", goodsSupplierDrugsEntityVo.getApprovals()));
            return errorMap;
        }
        List<GoodsSupplierDrugsEntity> drugsCodeList = this.selectList(new EntityWrapper<GoodsSupplierDrugsEntity>()
                .eq("del_flag", DelFlagEnum.NORMAL.getKey())
                .eq("drugs_code", goodsSupplierDrugsEntityVo.getDrugsCode())
                .eq("supplier_id", goodsSupplierDrugsEntityVo.getSupplierId())
        );
        if (!StringUtil.isEmpty(drugsCodeList)) {
            errorMap.put("errorMessage" ,MessageFormat.format("商品编码:{0}，已存在", goodsSupplierDrugsEntityVo.getDrugsCode()));
            return errorMap;
        }
        // 生产厂商id为空则保存厂商信息
        if (StringUtil.isEmpty(goodsSupplierDrugsEntityVo.getFactoryId())) {
            // 保存厂商信息，状态为待审批
            OrgFactoryInfoApprovalEntity orgFactoryEntity = new OrgFactoryInfoApprovalEntity();
            String factoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
            orgFactoryEntity.setFactoryCode(factoryCode);
            if (StringUtils.isBlank(goodsSupplierDrugsEntityVo.getFactoryName())) {
                errorMap.put("errorMessage" ,"厂商名称不能为空");
                return errorMap;
            }

            if (orgFactoryService.selectList(new EntityWrapper<OrgFactoryEntity>()
                    .eq("del_flag", DelFlagEnum.NORMAL.getKey())
                    .eq("factory_name", goodsSupplierDrugsEntityVo.getFactoryName())).size() > 0) {
                errorMap.put("errorMessage" ,"厂商名称已存在");
                return errorMap;
            }

            orgFactoryEntity.setFactoryName(goodsSupplierDrugsEntityVo.getFactoryName());
            orgFactoryEntity.setStatus(FactoryStatusEnum.DRAFT.getKey());
            orgFactoryEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
            orgFactoryEntity.setCreateId(goodsSupplierDrugsEntityVo.getCreateId());
            orgFactoryEntity.setCreateTime(new Date());
            SysUserEntity userEntity = sysUserService.selectById(goodsSupplierDrugsEntityVo.getCreateId());
            if (userEntity != null && TypeEnum.USER_PLATFORM.getKey().equals(userEntity.getUserType())) {
                //平台用户录入，厂家信息属于该用户
                orgFactoryEntity.setDeptId(userEntity.getDeptId());
            }
            orgFactoryInfoApprovalService.save(orgFactoryEntity,userEntity);
            goodsSupplierDrugsEntityVo.setFactoryId(String.valueOf(orgFactoryEntity.getId()));
        }

        //生成药品编码
        String drugsCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_DRUGS_CODE.getKey());
        goodsSupplierDrugsEntityVo.setDrugsCode(drugsCode);

        //设置未匹对(0:未匹对;1:已匹对)
        goodsSupplierDrugsEntityVo.setIsMatch(0);

        // 保存药品信息
        goodsSupplierDrugsEntityVo.setCreateTime(new Date());
        goodsSupplierDrugsEntityVo.setDataSource(SupplierDataSource.SYSTEM.getKey());
        Map<String, Object> supplierInfo = this.baseMapper.selectSupplierInfoBySupplierId(goodsSupplierDrugsEntityVo.getSupplierId());
        if (StringUtil.isEmpty(supplierInfo)) {
            errorMap.put("errorMessage" ,"厂商名称已存在");
            return errorMap;
        }
        goodsSupplierDrugsEntityVo.setDeptId((Long) supplierInfo.get("dept_id"));

        //设置是否上传为未上传
        goodsSupplierDrugsEntityVo.setIsUpload(SupplierIsUploadEnum.NO.getKey());
        goodsSupplierDrugsEntityVo.setDelFlag(DelFlagEnum.NORMAL.getKey());
        this.baseMapper.insert(goodsSupplierDrugsEntityVo);

        // 保存商品规格
        goodsSupplierDrugsSpecsService.save(goodsSupplierDrugsEntityVo);
        return errorMap;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,String> update(GoodsSupplierDrugsEntityVo goodsSupplierDrugsEntityVo,SysUserEntity user) {
        Map<String,String> errorMap = new HashMap<>(16);

        // 生产厂商id为空则保存厂商信息
        if (StringUtil.isEmpty(goodsSupplierDrugsEntityVo.getFactoryId())) {
            // 保存厂商信息，状态为草稿
            OrgFactoryInfoApprovalEntity orgFactoryEntity = new OrgFactoryInfoApprovalEntity();
            String factoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
            orgFactoryEntity.setFactoryCode(factoryCode);
            if (StringUtils.isBlank(goodsSupplierDrugsEntityVo.getFactoryName())) {
                errorMap.put("errorMessage","厂商名称不能为空");
                return errorMap;
            }

            if (orgFactoryService.selectList(new EntityWrapper<OrgFactoryEntity>()
                    .eq("del_flag", DelFlagEnum.NORMAL.getKey())
                    .eq("factory_name", goodsSupplierDrugsEntityVo.getFactoryName())).size() > 0) {
                errorMap.put("errorMessage","厂商名称已存在");
                return errorMap;
            }
            Integer approvalsExist = this.baseMapper.selectByApprovals(goodsSupplierDrugsEntityVo.getApprovals());
            if (approvalsExist > 0) {
                errorMap.put("errorMessage", MessageFormat.format("批准文号/注册文号:{0}，已存在", goodsSupplierDrugsEntityVo.getApprovals()));
                return errorMap;
            }
            orgFactoryEntity.setFactoryName(goodsSupplierDrugsEntityVo.getFactoryName());
            orgFactoryEntity.setStatus(FactoryStatusEnum.DRAFT.getKey());
            orgFactoryEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
            orgFactoryEntity.setCreateId(goodsSupplierDrugsEntityVo.getEditId());
            orgFactoryEntity.setCreateTime(new Date());
            SysUserEntity userEntity = sysUserService.selectById(goodsSupplierDrugsEntityVo.getEditId());
            if (userEntity != null && TypeEnum.USER_PLATFORM.getKey().equals(userEntity.getUserType())) {
                //平台用户录入，厂家信息属于该用户
                orgFactoryEntity.setDeptId(userEntity.getDeptId());
            }
            orgFactoryInfoApprovalService.save(orgFactoryEntity,user);
            goodsSupplierDrugsEntityVo.setFactoryId(String.valueOf(orgFactoryEntity.getId()));
        }

        //设置未匹对(0:未匹对;1:已匹对)
        goodsSupplierDrugsEntityVo.setIsMatch(IsMatchEnum.NO.getKey());

        goodsSupplierDrugsEntityVo.setEditTime(new Date());

        //设置是否上传为未上传
        goodsSupplierDrugsEntityVo.setIsUpload(SupplierIsUploadEnum.NO.getKey());

        this.baseMapper.updateById(goodsSupplierDrugsEntityVo);

        //更新下发目录上商品为未上传状态
        this.baseMapper.updateSupplierGoodsSendNotUpload(goodsSupplierDrugsEntityVo.getSupplierId(), goodsSupplierDrugsEntityVo.getId());
        return errorMap;
    }

    @Override
    public GoodsSupplierDrugsEntityVo selectSupplierDrugsById(Long id) {
        return this.baseMapper.selectSupplierDrugsById(id);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            GoodsSupplierDrugsEntity goodsSupplierDrugs = new GoodsSupplierDrugsEntity();
            goodsSupplierDrugs.setId(id);
            goodsSupplierDrugs.setDelFlag(DelFlagEnum.DELETE.getKey());
            this.baseMapper.updateById(goodsSupplierDrugs);
        }
    }

    @Override
    public void toggle(Map<String, Object> params) {
        List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
        for (Long id : ids) {
            GoodsSupplierDrugsEntity goodsSupplierDrugs = new GoodsSupplierDrugsEntity();
            goodsSupplierDrugs.setId(id);
            goodsSupplierDrugs.setStatus(Integer.valueOf(params.get("status").toString()));
            this.baseMapper.updateById(goodsSupplierDrugs);
        }
    }


    @Override
    public void updateIsMatch(Long goodsId) {
        GoodsSupplierDrugsEntity goodsSupplierDrugsEntity = new GoodsSupplierDrugsEntity();
        goodsSupplierDrugsEntity.setId(goodsId);
        goodsSupplierDrugsEntity.setIsMatch(1);
        baseMapper.updateById(goodsSupplierDrugsEntity);

    }

    @Override
    public GoodsSupplierDrugsEntity selectByGoodsNameAndFactoryNameAndSupplierId(String goodsName, String factoryName, Long supplierId) {
        return baseMapper.selectByGoodsNameAndFactoryNameAndSupplierId(goodsName, factoryName, supplierId);
    }


    @Override
    public List<Map<String, Object>> selectBySourcesIds(List<String> sourcesSpecsIds) {
        return this.baseMapper.selectBySourcesIds(sourcesSpecsIds);
    }

    @Override
    public void input(String[][] rows, Long userId, Long deptId) {
        for (int i = 0; i < rows.length; i++) {
            String[] row = rows[i];
            inputRow(userId, deptId, i, row);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void inputRow(Long userId, Long deptId, int i, String[] row) {
        Integer length = 14;
        if (!length.equals(row.length)) {
            throw new HdiException("导入失败，请确认模版是否正确");
        }
        // 供应商
        String supplierName = row[0];
        // 药品名称
        String drugsName = row[1];
        // 通用名
        String commonName = row[2];
        // 商品属性
        String goodsNature = row[3];
        // 商品分类
        String typeName = row[4];
        // 厂商名称
        String factoryName = row[5];
        // 商品规格名称
        String specs = row[6];
        // 药品本位码
        String guid = row[7];
        // 批准文号
        String approvals = row[8];
        // 商品单位
        String goodsUnit = row[9];
        // 供货单位
        String supplyUnit = row[10];
        // 转换单位
        String convertUnit = row[11];
        // 储存方式
        String storeWay = row[12];
        //存储方式转化
        List<SysDictEntity> sysDicts = sysDictService.selectDictByType("store_way");
        Map<String, SysDictEntity> maps = sysDicts.stream().collect(Collectors.toMap(SysDictEntity::getValue, Function.identity()));
        SysDictEntity sysDictEntity = maps.get(storeWay);
        if (null != sysDictEntity) {
            storeWay = sysDictEntity.getCode();
        } else {
            throw new HdiException("导入失败，储存方式值错误");
        }
        String factoryId = null;
        if (!goodsNature.equals("国产")) {
            if (!goodsNature.equals("进口")) {
                throw new HdiException("第" + (i + 1) + "条记录商品属性填写不正确");
            }
        }
        if (StringUtils.isBlank(factoryName)) {
            throw new HdiException("第" + (i + 1) + "条记录厂商名称不能空");
        } else {
            List<OrgFactoryEntity> factoryList = orgFactoryService.selectList(new EntityWrapper<OrgFactoryEntity>()
                    .eq("factory_name", factoryName)
                    .eq("del_flag", 0));
            if (factoryList != null && factoryList.size() > 0) {
                factoryId = factoryList.get(0).getId().toString();
            } else {
                throw new HdiException("第" + (i + 1) + "条记录厂商名称不存在对应的数据");
            }
        }
        if (StringUtils.isBlank(supplierName)) {
            throw new HdiException("第" + (i + 1) + "条记录供应商名称不能空");
        }

        List<Long> supplierIdList = this.baseMapper.selectSupplierIdByName(supplierName);
        if (supplierIdList == null || supplierIdList.size() <= 0) {
            throw new HdiException("第" + (i + 1) + "条记录供应商名称查询不出对应的供应商记录");
        }

        // 保存数据
        GoodsSupplierDrugsEntityVo goodsSupplierDrugsVO = new GoodsSupplierDrugsEntityVo();
        //查询是否存在重复数据
        List<GoodsSupplierDrugsEntity> supplierDrugsList = this.baseMapper.selectList(new EntityWrapper<GoodsSupplierDrugsEntity>()
                .eq("drugs_name", drugsName)
                .eq("approvals", approvals));
        if (supplierDrugsList != null && supplierDrugsList.size() > 0) {

            // 先更新商品数据
            GoodsSupplierDrugsEntity drugs = supplierDrugsList.get(0);
            goodsSupplierDrugsVO.setDrugsName(drugsName);
            goodsSupplierDrugsVO.setFactoryId(factoryId);
            goodsSupplierDrugsVO.setId(drugs.getId());
            goodsSupplierDrugsVO.setSupplierId(drugs.getSupplierId());
            goodsSupplierDrugsVO.setCommonName(commonName);
            goodsSupplierDrugsVO.setGoodsNature(goodsNature.equals("国产") ? 0 : 1);
            goodsSupplierDrugsVO.setTypeName(typeName);
            goodsSupplierDrugsVO.setFactoryName(factoryName);
            goodsSupplierDrugsVO.setGoodsUnit(checkGoodsUnit(goodsUnit));
            goodsSupplierDrugsVO.setSupplyUnit(checkGoodsUnit(supplyUnit));
            goodsSupplierDrugsVO.setConvertUnit(convertUnit);
            goodsSupplierDrugsVO.setEditId(userId);
            //储存方式
            goodsSupplierDrugsVO.setStoreWay(storeWay);
//            update(goodsSupplierDrugsVO);

            //查询是否存在规格相同的数据
            List<GoodsSupplierDrugsSpecsEntity> specsList = goodsSupplierDrugsSpecsService.selectList(new EntityWrapper<GoodsSupplierDrugsSpecsEntity>()
                    .eq("drugs_id", drugs.getId())
                    .eq("specs", specs));
            GoodsSupplierDrugsSpecsEntity specsEntity = null;
            if (specsList != null && specsList.size() > 0) {
                // 存在商品并且存在规格
                specsEntity = specsList.get(0);
                specsEntity.setEditId(userId);
            } else {
                // 存在商品但是不存在规格

                specsEntity = new GoodsSupplierDrugsSpecsEntity();
                specsEntity.setCreateId(userId);
            }
            specsEntity.setSpecs(specs);
            specsEntity.setGuid(guid);
            specsEntity.setDrugsId(goodsSupplierDrugsVO.getId());
            List<GoodsSupplierDrugsSpecsEntity> list = new ArrayList<>();
            list.add(specsEntity);
            goodsSupplierDrugsSpecsService.insertOrUpdate(list);
        } else {
            // 新增
            goodsSupplierDrugsVO.setFactoryId(factoryId);
            goodsSupplierDrugsVO.setSupplierId(supplierIdList.get(0));
            goodsSupplierDrugsVO.setDrugsName(drugsName);
            goodsSupplierDrugsVO.setCommonName(commonName);
            goodsSupplierDrugsVO.setGoodsNature(goodsNature.equals("国产") ? 0 : 1);
            goodsSupplierDrugsVO.setTypeName(typeName);
            goodsSupplierDrugsVO.setFactoryName(factoryName);
            goodsSupplierDrugsVO.setApprovals(approvals);
            goodsSupplierDrugsVO.setStatus(StatusEnum.USABLE.getKey());
            goodsSupplierDrugsVO.setGoodsUnit(checkGoodsUnit(goodsUnit));
            goodsSupplierDrugsVO.setSupplyUnit(checkGoodsUnit(supplyUnit));
            goodsSupplierDrugsVO.setConvertUnit(convertUnit);
            goodsSupplierDrugsVO.setDeptId(deptId);
            goodsSupplierDrugsVO.setCreateId(userId);
            goodsSupplierDrugsVO.setSpecs(specs);
            goodsSupplierDrugsVO.setGuid(guid);
            //储存方式
            goodsSupplierDrugsVO.setStoreWay(storeWay);
            //this.save(goodsSupplierDrugsVO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String checkGoodsUnit(String srcGoodsUnit) {
        String goodsUnit = null;
        if (!StringUtil.isEmpty(srcGoodsUnit)) {
            List<SysDictEntity> dicts = sysDictService.selectList(new EntityWrapper<SysDictEntity>().eq("value", srcGoodsUnit));
            if (StringUtil.isEmpty(dicts)) {
                SysDictEntity dict = new SysDictEntity();
                dict.setName("商品单位");
                dict.setType("goods_unit");
                dict.setCode(sysSequenceService.selectSeqValueBySeqCode("GOODS_UNIT_CODE"));
                dict.setValue(srcGoodsUnit);
                sysDictService.insert(dict);
                goodsUnit = dict.getCode();
            } else {
                goodsUnit = dicts.get(0).getCode();
            }
        }
        return goodsUnit;
    }

    @Override
    public void updateSupplierGoodsSendNotUpload(Long supplierId, Long goodsId) {
        this.baseMapper.updateSupplierGoodsSendNotUpload(supplierId, goodsId);
    }

    @Override
    public Map<String, String> importData(String[][] rows, SysUserEntity user) {

        List<GoodsSupplierDrugsSpecsEntity> list = new ArrayList<>(16);
        Map<String, String> errorMessage = new HashMap<>(16);
        StringBuilder sb = new StringBuilder();
        String key = "successCount";
        Integer failCount = 0;
        Integer successCount = 0;
        Map<String, Object> gscMap = new HashMap<>(16);
        for (int i = 1; i < rows.length; i++) {
            String[] row = rows[i];
            String supplierName = row[0].trim();
            String drugsName = row[1].trim();
            String commonName = row[2].trim();
            String goodsNature = row[3].trim();
            String typeName = row[4].trim();
            String factoryName = row[5].trim();
            String specs = row[6].trim();
            String guid = row[7].trim();
            String approvals = row[8].trim();
            String goodsUnit = row[9].trim();
            String supplyUnit = row[10].trim();
            String convertUnit = row[11].trim();
            String storeWay = row[12].trim();
            String sunshinePno = row[13].trim();
            if (StringUtils.isEmpty(approvals) || StringUtils.isEmpty(factoryName) || StringUtils.isEmpty(supplierName) || StringUtils.isEmpty(drugsName)
                    || StringUtils.isEmpty(specs) || StringUtils.isEmpty(typeName) || StringUtils.isEmpty(goodsUnit) || StringUtil.isEmpty(storeWay)) {
                sb.append("\n");
                sb.append("第 ").append(i + 2).append(" 行数据错误，必填字段不能为空");
                failCount++;
                continue;
            } else {
                String factoryId = null;
                List<OrgFactoryEntity> factoryList = orgFactoryService.selectList(new EntityWrapper<OrgFactoryEntity>()
                        .eq("factory_name", factoryName)
                        .eq("del_flag", 0));
                if (factoryList != null && factoryList.size() > 0) {
                    factoryId = factoryList.get(0).getId().toString();
                } else {
                    sb.append("\n");
                    sb.append("第 ").append(i + 2).append(" 行数据错误，厂商名称不存在对应的数据");
                    failCount++;
                    continue;
                }
                if (!StringUtil.isEmpty(goodsNature)) {
                    if (!goodsNature.equals("国产")) {
                        if (!goodsNature.equals("进口")) {
                            sb.append("\n");
                            sb.append("第 ").append(i + 2).append(" 行数据错误，商品属性填写不正确");
                            failCount++;
                            continue;
                        }
                    }
                }
                List<Long> supplierIdList = this.baseMapper.selectSupplierIdByName(supplierName);
                if (supplierIdList == null || supplierIdList.size() <= 0) {
                    sb.append("\n");
                    sb.append("第 ").append(i + 2).append(" 行数据错误，供应商名称查询不出对应的供应商记录");
                    failCount++;
                    continue;
                }
                //存储方式转化
                List<SysDictEntity> sysDicts = sysDictService.selectDictByType("store_way");
                Map<String, SysDictEntity> maps = sysDicts.stream().collect(Collectors.toMap(SysDictEntity::getValue, Function.identity()));
                SysDictEntity sysDictEntity = maps.get(storeWay);
                if (null != sysDictEntity) {
                    storeWay = sysDictEntity.getCode();
                } else {
                    sb.append("\n");
                    sb.append("第 ").append(i + 2).append(" 行数据错误，储存方式值错误");
                    failCount++;
                    continue;
                }

                GoodsSupplierDrugsEntityVo supplierDrugs = this.baseMapper.selectByDrugsNameAndApprovals(drugsName, approvals);
                Integer nameDifferentFlag = this.baseMapper.selectTheSameApprovalsAndDifferentName(drugsName, approvals);
                Integer existFlag = this.baseMapper.selectTheSameNameAndDifferentApprovlas(drugsName, approvals, specs);
                if (!StringUtil.isEmpty(supplierDrugs)) {
                    String nameAndapprovals = drugsName + approvals;
                    if (!gscMap.containsKey(nameAndapprovals)) {
                        gscMap.put(nameAndapprovals, null);

                        supplierDrugs.setDrugsName(drugsName);
                        supplierDrugs.setFactoryId(factoryId);
                        supplierDrugs.setCommonName(commonName);
                        if (!StringUtil.isEmpty(goodsNature)) {
                            supplierDrugs.setGoodsNature("国产".equals(goodsNature) ? 0 : 1);
                        }
                        supplierDrugs.setTypeName(typeName);
                        supplierDrugs.setFactoryName(factoryName);
                        supplierDrugs.setGoodsUnit(checkGoodsUnit(goodsUnit));
                        supplierDrugs.setSupplyUnit(checkGoodsUnit(supplyUnit));
                        supplierDrugs.setConvertUnit(convertUnit);
                        supplierDrugs.setEditId(user.getUserId());
                        supplierDrugs.setSunshinePno(sunshinePno);
                        //储存方式
                        supplierDrugs.setStoreWay(storeWay);
                        update(supplierDrugs, user);
                    }
                    //查询是否存在规格相同的数据
                    List<GoodsSupplierDrugsSpecsEntity> specsList = goodsSupplierDrugsSpecsService.selectList(new EntityWrapper<GoodsSupplierDrugsSpecsEntity>()
                            .eq("drugs_id", supplierDrugs.getId())
                            .eq("specs", specs));
                    GoodsSupplierDrugsSpecsEntity specsEntity = null;
                    if (specsList != null && specsList.size() > 0) {
                        // 存在商品并且存在规格
                        specsEntity = specsList.get(0);
                        specsEntity.setEditId(user.getUserId());
                    } else {
                        // 存在商品但是不存在规格
                        specsEntity = new GoodsSupplierDrugsSpecsEntity();
                        specsEntity.setCreateId(user.getUserId());
                    }
                    specsEntity.setSpecs(specs);
                    specsEntity.setGuid(guid);
                    specsEntity.setDrugsId(supplierDrugs.getId());
                    list.add(specsEntity);
                    goodsSupplierDrugsSpecsService.insertOrUpdate(list);
                    successCount++;

                } else if (nameDifferentFlag > 0) {
                    sb.append("\n");
                    sb.append("第 ").append(i + 2).append(" 行数据错误，该批准文号已存在,药品名称错误");
                    failCount++;
                    continue;
                } else {

                    GoodsSupplierDrugsEntityVo goodsSupplierDrugsVO = new GoodsSupplierDrugsEntityVo();
                    //商品名一样 而批准文号一样/规格不一样 新记录
                    // 新增
                    goodsSupplierDrugsVO.setFactoryId(factoryId);
                    goodsSupplierDrugsVO.setSupplierId(supplierIdList.get(0));
                    goodsSupplierDrugsVO.setDrugsName(drugsName);
                    goodsSupplierDrugsVO.setCommonName(commonName);
                    if (!StringUtil.isEmpty(goodsNature)) {
                        goodsSupplierDrugsVO.setGoodsNature("国产".equals(goodsNature) ? 0 : 1);
                    }
                    goodsSupplierDrugsVO.setTypeName(typeName);
                    goodsSupplierDrugsVO.setFactoryName(factoryName);
                    goodsSupplierDrugsVO.setApprovals(approvals);
                    goodsSupplierDrugsVO.setStatus(StatusEnum.USABLE.getKey());
                    goodsSupplierDrugsVO.setGoodsUnit(checkGoodsUnit(goodsUnit));
                    goodsSupplierDrugsVO.setSupplyUnit(checkGoodsUnit(supplyUnit));
                    goodsSupplierDrugsVO.setConvertUnit(convertUnit);
                    goodsSupplierDrugsVO.setDeptId(user.getDeptId());
                    goodsSupplierDrugsVO.setCreateId(user.getUserId());
                    goodsSupplierDrugsVO.setSpecs(specs);
                    goodsSupplierDrugsVO.setGuid(guid);
                    goodsSupplierDrugsVO.setSunshinePno(sunshinePno);
                    //储存方式
                    goodsSupplierDrugsVO.setStoreWay(storeWay);
                    Map<String, String> errorMap = this.save(goodsSupplierDrugsVO, user);
                    if (!errorMap.isEmpty()) {
                        sb.append("\n");
                        sb.append("第 ").append(i + 2).append(" 行数据错误，").append(errorMap.get("errorMessage"));
                        failCount++;
                        continue;
                    }
                    successCount++;

                }
            }

        }
        if (failCount > 0) {
            throw new HdiException("导入失败,错误条数" + failCount.toString() + "\n 错误信息" + sb.toString());
        }
        errorMessage.put("errorMessage", sb.toString());
        errorMessage.put(key, successCount.toString());
        errorMessage.put("failCount", failCount.toString());
        return errorMessage;
    }

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "gsd")
    public List<Map<String, Object>> getList(Map<String, Object> map) {
        return this.baseMapper.getList(map);
    }
}
