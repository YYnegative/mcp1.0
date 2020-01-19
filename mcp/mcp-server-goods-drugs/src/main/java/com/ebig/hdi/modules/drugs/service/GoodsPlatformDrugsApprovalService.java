package com.ebig.hdi.modules.drugs.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsApprovalEntity;
import com.ebig.hdi.modules.drugs.vo.GoodsPlatformDrugsEntityVo;
import com.ebig.hdi.modules.drugs.vo.GoodsPlatformDrugsParams;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 平台药品信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:21:25
 */
public interface GoodsPlatformDrugsApprovalService extends IService<GoodsPlatformDrugsApprovalEntity> {

    PageUtils queryPage(Map<String, Object> params);


	Map<String, String> save(GoodsPlatformDrugsEntityVo goodsPlatformDrugsVo,SysUserEntity user) throws Exception;

	void update(GoodsPlatformDrugsEntityVo goodsPlatformDrugsEntityVo,SysUserEntity user);

	GoodsPlatformDrugsEntityVo selectPlatformDrugsById(Long id);

	void delete(Long[] ids);

	void toggle(Map<String, Object> params);

	/**
	 * @Description: 平台药品导入
	 * @Author: YangYing
	 * @Date: 2019/11/27
	 */
    Map<String, String> importData(String[][] rows, SysUserEntity user);
	/**
	 * @Description: 
	 * @Author: YangYing
	 * @Date: 2019/11/28 0028
	 */
	List<Map<String, Object>> getList(Map<String, Object> map);

	Map<String, String> checkStatus(Map<String, Object> params, SysUserEntity user);
}

