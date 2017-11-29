package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;

/**
 * StrStartsWithMatcher
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/21 17:43
 */
@EqualsAndHashCode(callSuper = false)
public class StrStartsWithMatcher extends ConnectorMatcher.AbstractConnector<String> {

    /**
     * The object that is checked to be equal to the matched value.
     */
    private final String value;

    /**
     * Creates an element matcher that tests for equality.
     *
     * @param value The object that is checked to be equal to the matched value.
     */
    public StrStartsWithMatcher(String value) {
        this.value = value;
    }

    @Override
    public boolean matches(String target) {
        return target != null && value != null && target.startsWith(value);
    }

    @Override
    public String toString() {
        return "is(" + value + ")";
    }
}
