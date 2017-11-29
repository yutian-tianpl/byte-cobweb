package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;
/**
 * AndConnector And关系Matcher Connector
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/21 17:42
 */
@EqualsAndHashCode(callSuper = false)
public class AndConnector<W> extends ConnectorMatcher.AbstractConnector<W> {

    /**
     * And关系Matcher的两端
     */
    private final Matcher<? super W> left, right;

    /**
     * 创建一个And关系的Matcher
     *
     * @param left  The first matcher to consult for a match.
     * @param right The second matcher to consult for a match. This matcher is only consulted
     *              if the {@code first} matcher constituted a match.
     */
    public AndConnector(Matcher<? super W> left, Matcher<? super W> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean matches(W target) {
        return left.matches(target) && right.matches(target);
    }

    @Override
    public String toString() {
        return "(" + left + " and " + right + ')';
    }
}
