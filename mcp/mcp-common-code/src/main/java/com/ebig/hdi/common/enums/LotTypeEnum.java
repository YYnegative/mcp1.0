package com.ebig.hdi.common.enums;

/**
 * @athor: wen
 * Date: 2019/9/5
 * Time: 16:38
 * Description: 批号类型
 */
public enum LotTypeEnum {

    ONE(1, "生产批号"),
    TWO(2, "灭菌批号"),
    ;
    // 成员变量
    private Integer key;
    private String value;

    private LotTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
    // 普通方法
    public static String getName(Integer key) {
        for (LotTypeEnum c : LotTypeEnum.values()) {
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
