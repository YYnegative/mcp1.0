package com.ebig.hdi.modules.core.service.impl;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ebig.hdi.common.enums.LabelTypeEnum;
import com.ebig.hdi.modules.core.entity.CoreLabelSizeEntity;
import com.ebig.hdi.modules.core.service.CoreLabelSizeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.DateUtils;
import com.ebig.hdi.common.utils.FastDFSClientUtils;
import com.ebig.hdi.common.utils.Html2PdfUtil;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.dao.CoreLabelMasterDao;
import com.ebig.hdi.modules.core.entity.CoreLabelMasterEntity;
import com.ebig.hdi.modules.core.service.CoreLabelMasterService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;

import javax.servlet.http.HttpServletResponse;


@Service("coreLabelMasterService")
public class CoreLabelMasterServiceImpl extends ServiceImpl<CoreLabelMasterDao, CoreLabelMasterEntity> implements CoreLabelMasterService {

    private static String LABEL_HEAD_HTML;

    private static String LABEL_BODY_HTML;

    private static String LABEL_END_HTML;


    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private FastDFSClientUtils fastDFSClientUtils;

    @Autowired
    private CoreLabelSizeService coreLabelSizeService;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "clm")
    public PageUtils queryPage(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        // 手动数据过滤
        if (params.get(Constant.SQL_FILTER) != null) {
            String sqlFilter = params.get(Constant.SQL_FILTER).toString();
            params.put("deptIds", sqlFilter);
        }

        Page<CoreLabelMasterEntity> page = new Page<CoreLabelMasterEntity>(currPage, pageSize);
        List<CoreLabelMasterEntity> list = this.baseMapper.selectByDeptId(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "clm")
    public PageUtils bedingungenQueryPage(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        // 手动数据过滤
        if (params.get(Constant.SQL_FILTER) != null) {
            String sqlFilter = params.get(Constant.SQL_FILTER).toString();
            params.put("deptIds", sqlFilter);
        }

        Page<CoreLabelMasterEntity> page = new Page<CoreLabelMasterEntity>(currPage, pageSize);
        List<CoreLabelMasterEntity> list = this.baseMapper.selectByBedingungen(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMaster(List<CoreLabelMasterEntity> listEntity, SysUserEntity user) {
        if (user.getUserType() != 1) {
            throw new HdiException("当前登录用户无此权限");
        }
        for (CoreLabelMasterEntity coreLabelMasterEntity : listEntity) {
            CoreLabelMasterEntity labelSubmitted = this.baseMapper.selectLabelSubmittedById(coreLabelMasterEntity.getLabelid());
            if (!StringUtil.isEmpty(labelSubmitted)) {
                throw new HdiException(MessageFormat.format("删除失败！标签编号：{0}已提交医院！", labelSubmitted.getLabelno()));
            }
            coreLabelMasterEntity.setDelFlag(DelFlagEnum.DELETE.getKey());
            this.baseMapper.updateById(coreLabelMasterEntity);
        }
    }


    //HDI转换用  根据labelno  查询是否存在此标签
    @Override
    public CoreLabelMasterEntity selectByLabelno(String labelno) {
        CoreLabelMasterEntity coreLabelMasterEntity = this.baseMapper.selectByLabelno(labelno);

        return coreLabelMasterEntity;
    }

    //HDI转换用  生成新的标签
    @Override
    public void saveLabel(CoreLabelMasterEntity entity) {
        this.baseMapper.insert(entity);

    }


    //提交医院   查询所有的相关的标签
    @Override
    public List<CoreLabelMasterEntity> getCoreLabelMasterEntity(Long sourceid) {
        List<CoreLabelMasterEntity> list = this.baseMapper.getCoreLabelMasterEntity(sourceid);
        return list;
    }

    /**
     * 描述: 获取打印的标签PDF文件 <br/>
     *
     * @see com.ebig.hdi.modules.core.service.CoreLabelMasterService #printLabel(java.lang.Long[]) <br/>
     */
    @Override
    public void getLabelPdf(Long[] labelids, HttpServletResponse response, SysUserEntity user) throws Exception {
        if (StringUtil.isEmpty(labelids)) {
            throw new HdiException("请至少选择一条标签记录！");
        }
        this.baseMapper.updateLabelStatus(labelids);
        //获取标签模板
        getLabelTemplate();

        //设置页面的大小
        String codeWidth = null;
        String codeHeight = null;
        try {
            CoreLabelSizeEntity coreLabelSizeEntity = coreLabelSizeService.getUserDetail(user.getUserId(), LabelTypeEnum.LABEL.getKey());
            if (StringUtil.isEmpty(coreLabelSizeEntity)) {
                codeWidth = sysConfigService.getValue(SysConfigEnum.LABEL_CODE_WIDTH.getKey());
                codeHeight = sysConfigService.getValue(SysConfigEnum.LABEL_CODE_HEIGHT.getKey());
            } else {
                codeWidth = coreLabelSizeEntity.getPdfWidth().toString();
                codeHeight = coreLabelSizeEntity.getPdfHeight().toString();
            }

        } catch (Exception e) {
            throw new HdiException("获取批次码高/宽度错误，请检查参数配置！");
        }
        Double htmlCodeWidth = (Double.parseDouble(codeWidth))* (122d/350d);
        Double htmlCodeHeight = (Double.parseDouble(codeHeight))*(55d/161d);
        //动态拼接标签HTML
        LABEL_HEAD_HTML = LABEL_HEAD_HTML.replaceAll("80mm", htmlCodeWidth+"mm")
                .replaceAll("40mm", htmlCodeHeight+"mm");
        StringBuilder html = new StringBuilder(LABEL_HEAD_HTML);
        List<CoreLabelMasterEntity> coreLabelList = this.baseMapper.selectByLabelids(Arrays.asList(labelids));
        for (CoreLabelMasterEntity coreLabel : coreLabelList) {
            String labelTemplate = LABEL_BODY_HTML.replaceAll("@consumeUnit", coreLabel.getHospitalName())
                    .replaceAll("@provideUnit", coreLabel.getSupplierName())
                    .replaceAll("@qrCodeUrl", fastDFSClientUtils.getResAccessUrl(coreLabel.getImageUrl()))
                    .replaceAll("@provideTime", DateUtils.format(coreLabel.getSupplyTime()))
                    .replaceAll("@labelNo", coreLabel.getLabelno());
            html.append(labelTemplate);
        }
        html.append(LABEL_END_HTML);

        Rectangle pageSize = new Rectangle(Float.parseFloat(codeWidth), Float.parseFloat(codeHeight));
        Document document = new Document(pageSize, 2, 2, 2, 2);
        String pdfFilePath = Html2PdfUtil.getPdfUrl(html.toString(), document);
        StringUtil.responsePdf(response, pdfFilePath, "标签");
    }


    private void getLabelTemplate() {
        String html = sysConfigService.getValue(SysConfigEnum.LABEL_HTML.getKey());
        LABEL_BODY_HTML = StringUtils.substringBetween(html, "<body>", "</body>");
        LABEL_HEAD_HTML = StringUtils.substringBeforeLast(html, LABEL_BODY_HTML);
        LABEL_END_HTML = StringUtils.substringAfterLast(html, LABEL_BODY_HTML);
    }
}
