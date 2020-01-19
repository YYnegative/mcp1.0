package com.ebig.hdi.common.enums;

/**
 * 供货类型 0 非票货同行 1 票货同行
 */
public enum SupplyTypeEnum {

    ZERO(0, "非票货同行"),
    ONE(1, "票货同行");

    // 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private SupplyTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getName(Integer key) {
        for (SupplyTypeEnum c : SupplyTypeEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.value;
            }
        }
        return null;
    }
    // 普通方法
    public static Integer getKey(String name) {
        for (SupplyTypeEnum c : SupplyTypeEnum.values()) {
            if (c.getValue().equals(name)) {
                return c.key;
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
