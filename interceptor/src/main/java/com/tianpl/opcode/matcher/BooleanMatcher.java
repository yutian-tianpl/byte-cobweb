package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;

/**
 * MethodAccessMatch
 *
 * @Author yu.tian@tianpl.com
 * @Date 17/11/21 10:36
 */
@EqualsAndHashCode(callSuper = false)
public class BooleanMatcher<T> extends ConnectorMatcher.AbstractConnector<T> {

    /**
     * The predefined result.
     */
    private final boolean matches;

    /**
     * Creates a new boolean element matcher.
     *
     * @param matches The predefined result.
     */
    public BooleanMatcher(boolean matches) {
        this.matches = matches;
    }

    @Override
    public boolean matches(T target) {
        return matches;
    }

    @Override
    public String toString() {
        return Boolean.toString(matches);
    }
}
