package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;

/**
 * NullMatcher
 * null匹配器
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/21 10:36
 * @param <T> The type of the matched entity.
 */
@EqualsAndHashCode(callSuper = false)
public class NullMatcher<T> extends ConnectorMatcher.AbstractConnector<T> {

    @Override
    public boolean matches(T target) {
        return target == null;
    }

    @Override
    public String toString() {
        return "isNull()";
    }
}
