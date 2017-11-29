package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;

/**
 * An element matcher that checks an object's equality to another object.
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 */
@EqualsAndHashCode(callSuper = false)
public class StrEndsWithMatcher extends ConnectorMatcher.AbstractConnector<String> {

    /**
     * The object that is checked to be equal to the matched value.
     */
    private final String value;

    /**
     * Creates an element matcher that tests for equality.
     *
     * @param value The object that is checked to be equal to the matched value.
     */
    public StrEndsWithMatcher(String value) {
        this.value = value;
    }

    @Override
    public boolean matches(String target) {
        return target != null && value != null && target.endsWith(value);
    }

    @Override
    public String toString() {
        return "is(" + value + ")";
    }
}
