package com.ebig.hdi.common.enums;

/**
 * 采购单状态 0 已作废 1 未确认 2 已确认3 已供货 4 部分供货
 */
public enum PurchaseStatusEnum {

    EXPIRED(0, "已作废"),
    UNCONFIRMED(1, "未确认"),
    CONFIRMED(2, "已确认"),
    PROVIDED(3, "已供货"),
    PART_PROVIDED(4, " 部分供货")
    ;

    // 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private PurchaseStatusEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getName(Integer key) {
        for (PurchaseStatusEnum c : PurchaseStatusEnum.values()) {
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
