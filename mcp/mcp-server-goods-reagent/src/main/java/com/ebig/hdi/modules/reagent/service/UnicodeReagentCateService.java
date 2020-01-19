package com.ebig.hdi.modules.reagent.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.consumables.entity.UnicodeConsumablesCateEntity;
import com.ebig.hdi.modules.reagent.entity.ReagentTreeNode;
import com.ebig.hdi.modules.reagent.entity.UnicodeReagentCateEntity;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-13 11:01:00
 */
public interface UnicodeReagentCateService extends IService<UnicodeReagentCateEntity> {

    List<ReagentTreeNode> queryNode(Map<String, Object> params);
    
    void save(UnicodeReagentCateEntity unicodeReagentCateEntity);
    
    void update(UnicodeReagentCateEntity unicodeReagentCateEntity);
    
    void deleteNode(Long cateId);

    /**
     * 函数功能说明 ：获取试剂统一编码<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param goodsNature 商品属性
     * 参数：@param typeId 商品分类
     * 参数：@param reagentCode 试剂编码
     * 参数：@param factoryCode 厂商编码
     * 参数：@return <br/>
     * return：String <br/>
     */
	String generatorGoodsUnicode(Integer goodsNature, Long typeId, String reagentCode, String factoryCode);

	void input(String[][] rows);

    /**
     * @Description: 查询所有商品分类
     * @Author: ZhengHaiWen
     * @Date: 2019/12/3
     */
    List<UnicodeReagentCateEntity> selectAll() ;

    void queryList(Long pcateId ,List<Long> treeNodeList );
}

