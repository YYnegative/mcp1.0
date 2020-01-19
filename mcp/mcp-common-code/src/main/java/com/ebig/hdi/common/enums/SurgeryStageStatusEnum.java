package com.ebig.hdi.common.enums;
/**
 * 类功能说明：跟台目录状态枚举类<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 修改说明：<br/>
 * 创建时间：2019年06月11日 上午09:41:32 <br/>
 * 版本：V1.0 <br/>
 */
public enum SurgeryStageStatusEnum {
	
	//跟台状态
	UNDISTRIBUTED(1,"未提交"),
	DISTRIBUTED(2,"已经提交"),
	ACCEPTED(3,"已验收"),
	PATH_ACCEPTED(4,"部分验收"),
	REJECTION(5,"拒收");
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private SurgeryStageStatusEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (SurgeryStageStatusEnum c : SurgeryStageStatusEnum.values()) {
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
