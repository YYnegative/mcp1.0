package com.ebig.hdi.common.enums;

public enum StorageCondictionEnum {
    NORMAL("常温", "1"),
    SHADE("阴凉","2" ),
    REFRIGERATION("冷藏","3" ),
    FROST("冰冻", "4"),
    ;

    // 成员变量
    private String key;
    private String value;

    // 构造方法
    private StorageCondictionEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getName(String key) {
        for (StorageCondictionEnum c : StorageCondictionEnum.values()) {
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
