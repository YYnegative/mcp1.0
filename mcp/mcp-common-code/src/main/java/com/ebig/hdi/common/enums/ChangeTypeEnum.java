package com.ebig.hdi.common.enums;

/**
 * @title: ChangeTypeEnum
 * @projectName mcp
 * @description: 变更类型枚举类
 * @author：wenchao
 * @date：2019-11-05 13:16
 * @version：V1.0
 */
public enum ChangeTypeEnum {

    ADD(1,"新增"),
    UPDATE(2, "更新"),
    ;

    /// 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private ChangeTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getName(Integer key) {
        for (ChangeTypeEnum c : ChangeTypeEnum.values()) {
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
