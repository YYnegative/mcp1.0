package com.ebig.hdi.common.enums;

public enum SysConfigEnum {

	SUPPLIER_ROLE_MENU("SUPPLIER_ROLE_MENU", "供应商角色菜单"),
	INITIAL_PASSWORD("INITIAL_PASSWORD", "初始化密码"),
	GROUP_ROLE_MENU("GROUP_ROLE_MENU","集团管理角色菜单"),
	LABEL_HTML("LABEL_HTML","标签模板"),
	BATCH_HTML("BATCH_HTML","批次码模块"),
	QRCODE_BATCH_WIDTH("QRCODE_BATCH_WIDTH","批次二维码宽度"),
	QRCODE_BATCH_HEIGHT("QRCODE_BATCH_HEIGHT","批次二维码高度"),
	QRCODE_LABEL_WIDTH("QRCODE_LABEL_WIDTH","标签二维码宽度"),
	QRCODE_LABEL_HEIGHT("QRCODE_LABEL_HEIGHT","标签二维码高度"),
	BATCH_CODE_WIDTH("BATCH_CODE_WIDTH","批次码宽度"),
	BATCH_CODE_HEIGHT("BATCH_CODE_HEIGHT","批次码高度"),
	LABEL_CODE_WIDTH("LABEL_CODE_WIDTH","标签码宽度"),
	LABEL_CODE_HEIGHT("LABEL_CODE_HEIGHT","标签码高度"),
	PURCHASE_TEMPLATE("PURCHASE_TEMPLATE","采购单导出模板"),
	REFUNDS_TEMPLATE("REFUNDS_TEMPLATE","退货单导出模板"),
	FACTORY_TEMPLATE("FACTORY_TEMPLATE","工厂管理导出模板"),
	HOSPITAL_TEMPLATE("HOSPITAL_TEMPLATE","医院管理导出模板"),
	SUPPLIER_TEMPLATE("SUPPLIER_TEMPLATE","供应商管理导出模板"),
	GOODSPLATFORMCONSUMABLES_TEMPLATE("GOODSPLATFORMCONSUMABLES_TEMPLATE","平台耗材导出模板"),
	GOODSPLATFORMREAGENT_TEMPLATE("GOODSPLATFORMREAGENT_TEMPLATE","平台试剂导出模板"),
	GOODS_PLATFORM_DRUGS_TEMPLATE("GOODS_PLATFORM_DRUGS_TEMPLATE","平台药品导出模板"),
	GOODS_SUPPLIER_CONSUMABLES_TEMPLATE("GOODS_SUPPLIER_CONSUMABLES_TEMPLATE","供应商耗材导出模板"),
	GOODS_SUPPLIER_DRUGS_TEMPLATE("GOODS_SUPPLIER_DRUGS_TEMPLATE","供应商药品导出模板"),
	SUIILY_ACCEPT_LIST_MASTER("SUIILY_ACCEPT_LIST_MASTER","验收单导出模板"),
	GOODSSUPPLIERREAGENT_TEMPLATE("GOODSSUPPLIERREAGENT_TEMPLATE","供应商试剂导出模板")


			;

	// 成员变量
	private String key;
	private String value;

	// 构造方法
	private SysConfigEnum(String key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(String key) {
		for (SysConfigEnum c : SysConfigEnum.values()) {
			if (c.getKey().equals(key)) {
				return c.value;
			}
		}
		return null;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}
