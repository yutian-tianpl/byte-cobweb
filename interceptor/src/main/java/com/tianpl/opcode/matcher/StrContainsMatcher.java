package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;

/**
 * An element matcher that checks an object's equality to another object.
 *
 */
@EqualsAndHashCode(callSuper = false)
public class StrContainsMatcher extends ConnectorMatcher.AbstractConnector<String> {

    /**
     * The object that is checked to be equal to the matched value.
     */
    private final String value;

    /**
     * Creates an element matcher that tests for equality.
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
