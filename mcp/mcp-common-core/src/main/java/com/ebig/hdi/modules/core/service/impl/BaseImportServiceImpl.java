package com.ebig.hdi.modules.core.service.impl;

import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.modules.core.service.BaseImportService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: mcp
 * @description: 导入基类实现类
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2019-12-16 16:36
 **/
public abstract  class BaseImportServiceImpl implements BaseImportService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public final  Hdi importFile(@RequestParam("file") MultipartFile file, SysUserEntity sysUserEntity) {
        try {
            String[][] rows = ExcelUtils.readExcelByInput(file.getInputStream(), file.getOriginalFilename(), 1);
            if(rows.length <= 1){
                return Hdi.ok("导入失败！excel文件数据为空");
            }
            Map<String,String> message = new HashMap<>(16);
            message = importData(rows,sysUserEntity);
            String key ="failCount";

            if(message.get(key)!= null && Integer.parseInt(message.get(key) )> 0 ){
                return Hdi.ok(message.get("successCount")+"条记录导入成功，\n"+message.get(key)+"条记录导入失败，失败原因："+message.get("errorMessage"));
            }
            return Hdi.ok(message.get("successCount")+"条记录导入成功，"+message.get(key)+"条记录导入失败");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Hdi.error("导入失败！"+e.getMessage());
        }
    }

    /**
     * @Description:抽象导入方法
     * @Author: ZhengHaiWen
     * @Date: 2019/12/16
     */
    protected abstract Map<String,String> importData(String[][] rows, SysUserEntity sysUserEntity);
}
