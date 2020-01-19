package com.ebig.hdi.common.enums;

/**
 * @title: AssigneeTypeEnum
 * @projectName mcp
 * @description: 办理人类型 1办理人 2候选人 3组
 * @author：wenchao
 * @date：2019-11-11 11:09
 * @version：V1.0
 */
public enum AssigneeTypeEnum {

    USER_TYPE(1, "办理人"),
    USER_S_TYPE(2, "候选人"),
    GROUP_TYPE(3, "组"),;

    /// 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private AssigneeTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getName(Integer key) {
        for (AssigneeTypeEnum c : AssigneeTypeEnum.values()) {
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
