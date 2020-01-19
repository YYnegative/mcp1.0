package com.ebig.hdi.tool.generator.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ebig.hdi.tool.generator.service.SysGeneratorService;
import com.ebig.hdi.tool.generator.utils.PageUtils;
import com.ebig.hdi.tool.generator.utils.Query;
import com.ebig.hdi.tool.generator.utils.DateUtils;
import com.ebig.hdi.tool.generator.utils.Hdi;

/**
 * 类功能说明：代码生成器<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 公司名称：信宜市新屋乐网络科技有限公司 <br/>
 * 作者：luorx <br/>
 * 创建时间：2019年1月19日 下午7:45:13 <br/>
 * 版本：V1.0 <br/>
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Hdi list(@RequestParam Map<String, Object> params){
		PageUtils pageUtil = sysGeneratorService.queryList(new Query(params));
		
		return Hdi.ok().put("page", pageUtil);
	}
	
	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
	public void code(String tables, HttpServletResponse response) throws IOException{
		byte[] data = sysGeneratorService.generatorCode(tables.split(","));
		
		response.reset();  
        response.setHeader("Content-Disposition", "attachment; filename=\"hdi-generator-code"+DateUtils.format(new Date())+".zip\"");  
        response.addHeader("Content-Length", "" + data.length);  
        response.setContentType("application/octet-stream; charset=UTF-8");  
  
        IOUtils.write(data, response.getOutputStream());  
	}
}
