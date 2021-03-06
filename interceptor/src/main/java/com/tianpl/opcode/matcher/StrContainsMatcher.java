package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;

/**
 * StrContainsMatcher
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/21 17:43
 */
@EqualsAndHashCode(callSuper = false)
public class StrContainsMatcher extends ConnectorMatcher.AbstractConnector<String> {

    /**
     * The object that is checked to be equal to the matched value.
     */
    private final String value;

    /**
     * 创建一个String类型的包含关系matcher
     *
     * @param value The object that is checked to be equal to the matched value.
     */
    public StrContainsMatcher(String value) {
        this.value = value;
    }

    @Override
    public boolean matches(String target) {
        return target != null && value != null && target.contains(value);
    }

    @Override
    public String toString() {
        return "is(StrContainsMatcher:" + value + ")";
    }
}
