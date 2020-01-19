package com.ebig.hdi.modules.core.service;

import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @program: mcp
 * @description: 导入公共模板类
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2019-12-18 10:58
 **/
public class ImportCommon {
    public static Hdi importFile(@RequestParam("file") MultipartFile file, SysUserEntity sysUserEntity, Function<Object[],Map> execute) {
        try {
            String[][] rows = ExcelUtils.readExcelByInput(file.getInputStream(), file.getOriginalFilename(), 1);
            if(rows.length <= 1){
                return Hdi.ok("导入失败！excel文件数据为空");
            }
            Map<String,String> message = new HashMap<>(16);
            Object[] param = new Object[]{rows, sysUserEntity,message};

            message = execute.apply(param);
            String key ="failCount";

            if(message.get(key)!= null && Integer.parseInt(message.get(key) )> 0 ){
                return Hdi.ok(message.get("successCount")+"条记录导入成功，\n"+message.get(key)+"条记录导入失败，失败原因："+message.get("errorMessage"));
            }
            return Hdi.ok(message.get("successCount")+"条记录导入成功，"+message.get(key)+"条记录导入失败");
        } catch (Exception e) {
            return Hdi.error("导入失败！"+e.getMessage());
        }
    }
}
