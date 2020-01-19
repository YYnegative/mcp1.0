package com.ebig.hdi.modules.consumables.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.consumables.entity.ConsumablesTreeNode;
import com.ebig.hdi.modules.consumables.entity.UnicodeConsumablesCateEntity;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-13 09:53:58
 */
public interface UnicodeConsumablesCateService extends IService<UnicodeConsumablesCateEntity> {
    
    List<ConsumablesTreeNode> queryNode(Map<String, Object> params);
    
    void save(UnicodeConsumablesCateEntity unicodeConsumablesCateEntity);
    
    void update(UnicodeConsumablesCateEntity unicodeConsumablesCateEntity);
    
    void deleteNode(Long cateId);

    
    /**
     * 函数功能说明 ：获取耗材统一编码 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param goodsNature 商品属性
     * 参数：@param typeId 商品分类ID
     * 参数：@param consumablesCode 耗材编码
     * 参数：@param factoryCode 厂商编码
     * 参数：@return <br/>
     * return：String <br/>
     */
	String generatorGoodsUnicode(Integer goodsNature, Long typeId, String consumablesCode, String factoryCode);

	void input(String[][] rows);

    List<UnicodeConsumablesCateEntity> selectAll();

    void queryList(Long pcateId ,List<Long> treeNodeList );
}

