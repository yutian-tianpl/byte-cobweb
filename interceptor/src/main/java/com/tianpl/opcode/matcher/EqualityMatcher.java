package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;

/**
 * EqualityMatcher
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/20 18:32
 */
@EqualsAndHashCode(callSuper = false)
public class EqualityMatcher<T> extends ConnectorMatcher.AbstractConnector<T> {

    protected final Object value;

    public EqualityMatcher(Object value) {
        this.value = value;
    }

    @Override
    public boolean matches(T target) {
        return value.equals(target);
    }

    @Override
    public String toString() {
        return "is(" + value + ")";
    }
}
