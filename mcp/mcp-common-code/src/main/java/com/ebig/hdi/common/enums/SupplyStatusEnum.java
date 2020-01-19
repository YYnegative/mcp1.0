package com.ebig.hdi.common.enums;
/**
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-08-12 09:22:42
 */
public enum SupplyStatusEnum {
	
	UNCOMMITTED(0, "未提交"),
	COMMITTED(1, "已提交"),
	ACCEPTED(2, "已验收"),
	PART_ACCEPTED(3, "部分验收"),
	UNACCEPTED(4, "拒收");

    // 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private SupplyStatusEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getName(Integer key) {
        for (SupplyStatusEnum c : SupplyStatusEnum.values()) {
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
