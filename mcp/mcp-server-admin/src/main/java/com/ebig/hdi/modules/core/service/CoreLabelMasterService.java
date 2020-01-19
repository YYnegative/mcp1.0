package com.ebig.hdi.modules.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.entity.CoreLabelMasterEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:14
 */
public interface CoreLabelMasterService extends IService<CoreLabelMasterEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    PageUtils bedingungenQueryPage(Map<String, Object> params);
    
    void deleteMaster(List<CoreLabelMasterEntity> listEntity,SysUserEntity user);
    
	//HDI转换用  根据labelno  查询是否存在此标签
	CoreLabelMasterEntity selectByLabelno(String labelno);
	
	//HDI转换用  生成新的标签
	void saveLabel(CoreLabelMasterEntity entity);
	
	//提交医院   查询所有的相关的标签
	List<CoreLabelMasterEntity> getCoreLabelMasterEntity(Long sourceid);

	/**
	 * 函数功能说明 ：获取打印的标签PDF文件 <br/>
	 * 修改者名字： <br/>
	 * 修改日期： <br/>
	 * 修改内容：<br/>
	 * 作者：roncoo-lrx <br/>
	 * 参数：@param ids
	 * 参数：@return
	 * 参数：@throws Exception <br/>
	 * return：String <br/>
	 */
	void getLabelPdf(Long[] labelids,HttpServletResponse response,SysUserEntity userEntity) throws Exception;
}

