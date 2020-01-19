package com.ebig.hdi.common.enums;
/**
 * 类功能说明：供应商是否上传枚举类<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 修改说明：<br/>
 * 创建时间：2019年06月24日 上午09:41:32 <br/>
 * 版本：V1.0 <br/>
 */
public enum SupplierIsUploadEnum {

	NO(0, "未上传"),
	YES(1, "已上传"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private SupplierIsUploadEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (SupplierIsUploadEnum c : SupplierIsUploadEnum.values()) {
			if (c.getKey().equals(key)) {
				return c.value;
			}
		}
		return null;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}
