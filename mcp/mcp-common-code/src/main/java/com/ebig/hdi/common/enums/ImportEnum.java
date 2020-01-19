package com.ebig.hdi.common.enums;
/**
 * @projectName mcp
 * @description: 导入枚举
 * @author：zhenghaiwen
 * @date：2019-11-27 16:22
 * @version：V1.0
 */
public enum ImportEnum {
    REFUNDS(1, "refunds_import"),
    FACTORY(2, "factory_import"),
    HOSPITAL(3, "hospital_import"),
    SUPPLIER(4, "supplier_import"),
    GOODSPLATFORMREAGENT(5, "goodsplatformreagent_import"),
    GOODSPLATFORMCONSUMABLES(6,"goodsplatformconsumables_import"),
    GOODS_PLATFORM_DRUGS_IMPORT(7,"goods_platform_drugs_import"),
    GOODS_SUPPLIER_DRUGS_IMPORT(8,"goods_supplier_drugs_import"),
    GOODS_SUPPLIER_CONSUMABLES_IMPORT(10,"goods_supplier_consumables_import"),
    GOODS_SUPPLIER_REAGENT_IMPORT(11,"goods_supplier_reagent_import"),
    PURCHASE_IMPORT(12,"purchase_import")

    ;

    // 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private ImportEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getName(Integer key) {
        for (ImportEnum c : ImportEnum.values()) {
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
