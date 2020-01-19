package com.ebig.hdi.modules.drugs.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.drugs.entity.DrugsTreeNode;
import com.ebig.hdi.modules.drugs.entity.UnicodeDrugsCateEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-13 11:02:14
 */
public interface UnicodeDrugsCateService extends IService<UnicodeDrugsCateEntity> {
    
    List<DrugsTreeNode> queryNode(Map<String, Object> params);
    
    void save(UnicodeDrugsCateEntity unicodeDrugsCateEntity);
    
    void update(UnicodeDrugsCateEntity unicodeDrugsCateEntity);
    
    void deleteNode(Long cateId);

    /**
     * 函数功能说明 ：获取药品统一编码 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param goodsNature 商品属性
     * 参数：@param typeId 商品分类ID
     * 参数：@param drugsCode 药品编码
     * 参数：@param factoryCode 厂商编码
     * 参数：@return <br/>
     * return：String <br/>
     */
	String generatorGoodsUnicode(Integer goodsNature, Long typeId, String drugsCode, String factoryCode);

	void input(String[][] rows);

    List<UnicodeDrugsCateEntity> selectAll();


    void queryList(Long typeId, List<Long> cateIds);
}

