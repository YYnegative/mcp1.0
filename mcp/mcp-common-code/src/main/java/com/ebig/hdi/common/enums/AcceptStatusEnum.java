package com.ebig.hdi.common.enums;

/**
 * 验收单状态 0 未结算, 1 已结算
 */
public enum AcceptStatusEnum {

    UNSETTlEMENT(0, "未结算"),
    SETTlEMENT(1, "已结算");

    // 成员变量
    private Integer key;
    private String value;

    // 构造方法
    AcceptStatusEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getName(Integer key) {
        for (AcceptStatusEnum c : AcceptStatusEnum.values()) {
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
