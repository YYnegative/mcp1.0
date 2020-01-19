package com.ebig.mcp.server.api;


import com.ebig.hdi.common.utils.BASE64Utils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * 文件名称: ${file_name} <br/>
 * 类路径: ${package_name} <br/>
 * 描述: ${todo} <br/>
 * 作者：wenchao <br/>
 * 时间：${date} ${time} <br/>
 * 版本：V1.0 <br/>
 */
public class Test {
    public static void main(String[] args) throws Exception {

        String str = BASE64Utils.encryptBASE64(44011118+"e1153123d7d180ceeb820d577ff119876678");
        str = BASE64Utils.decryptBASE64(str);
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
//        Client client = (Client) clientFactory.createClient("http://localhost:8888/mcp-server/uploadsuppliergoods?wsdl");
        Client client = (Client) clientFactory.createClient("http://localhost:8888/mcp-server/downloadsaleback?wsdl");
//        Client client = (Client) clientFactory.createClient("http://localhost:8888/mcp-server/uploadPurchaseMenu?wsdl");
//        Client client = (Client) clientFactory.createClient("http://localhost:8888/mcp-server/uploadPurchaseComfirm?wsdl");
//        Client client = (Client) clientFactory.createClient("http://localhost:8888/mcp-server/uploadSupplyMenu?wsdl");

//        Object[] result = client.invoke("uploadSupplierGoods",
        Object[] result = client.invoke("downloadSaleBack",
//        Object[] result = client.invoke("uploadPurchaeMuneCriteria",
//        Object[] result = client.invoke("uploadPurchaseMenuComfirm",
//        Object[] result = client.invoke("uploadSupplyMenu",
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "\n" +
                        "<datapacket  sign=\"NDQwMTExMThlMTE1MzEyM2Q3ZDE4MGNlZWI4MjBkNTc3ZmYxMTk4NzY2Nzg=\" supplierCode=\"44011118\" goodsType=\"1\"> \n" +
                        "  <rowdata> \n" +
                        "    <masterdata> \n" +
                        //商品上传
//                        "      <item  supplierCode=\"44011118\" sunshinePno=\"000002\" goodsName=\"测试耗材2\" goodsSpecsCode=\"CS0002\" goodsSpecs=\"CS0003\" guid=\"BW0002\" goodsNature=\"1\" typeName=\"一般耗材\" factoryName=\"帽峰山制药厂\" status=\"1\" goodsUnit=\"1\" supplyUnit=\"1\" convertNnit=\"1:1\" approvals=\"PW0002\" storeWay = \"常温\" /> \n" +
                        //退货单下载
                        "       <item supplierCode=\"44011118\"  status=\"1\"  startTime=\"2019-10-26T15:30:28.269Z\"  endTime=\"null\"  createTime=\"null\" /> \n"+
                       //采购单下载
//                        "       <item supplierCode=\"HDI000528\"   beginTime=\"2019-09-08 10:12:59\"  endTime=\"2019-09-19 10:13:05\"   /> \n"+
                        //采购单确认
//                        "       <item supplierCode=\"myadminZY001\"   purchaseSeq=\"119\"  purchaseStatus=\"1\"   /> \n"+
                        //供货单上传
//                        "       <item supplierCode=\"123\"   sourceId=\"1\"  sellNo=\"223\"  supplyNo=\"2561\"   purchaseMasterId=\"64\"  delFlag=\"0\" purplanNo=\"2561\"   supplyTime=\"2019-11-26T15:30:28.269Z\"  supplyType=\"1\"  /> \n"+
//                        "    <detaildata>   <item sourceDtlId=\"11\"   purchaseDetailId=\"63\"  goodsSpecsCode=\"223\"  goodsName=\"2561\"   goodsSpecs=\"64\"  goodsUnit=\"0\" supplyAmount=\"25\"   beginTime=\"2019-1-26T15:30:28.269Z\" endTime=\"2022-11-26T15:30:28.269Z\"  lotNo=\"2222\"  totalAmount=\"222\"  /> \n"+
//                        "    </detaildata> \n" +

                        "    </masterdata> \n" +
                        "  </rowdata> \n" +
                        "</datapacket>\n");
        if(result != null && result.length > 0)
        {
            System.out.println(result[0].toString());
        }
//        Map<String,Object> map = new HashMap<>();
//        map.put("seqCode","SUPPLIER_CONSUMABLES_CODE");
//        String s = HttpUtils.doGet("http://localhost:2367/mcp-server-admin/org/syssequence/selectvalue/SUPPLIER_CONSUMABLES_CODE");
//
//        System.out.println(s);


    }

}
