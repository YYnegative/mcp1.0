package com.ebig.hdi.tool.generator.utils;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类功能说明：查询参数<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 公司名称：信宜市新屋乐网络科技有限公司 <br/>
 * 作者：roncoo-lrx <br/>
 * 创建时间：2019年1月19日 下午7:50:23 <br/>
 * 版本：V1.0 <br/>
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	//当前页码
    private int page;
    //每页条数
    private int limit;

    public Query(Map<String, Object> params){
        this.putAll(params);

        //分页参数
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("offset", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
