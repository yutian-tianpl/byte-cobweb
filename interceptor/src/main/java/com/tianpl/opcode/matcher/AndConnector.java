package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;
/**
 * AndConnector
 *
 * A conjunction matcher which only matches an element if both represented matchers constitute a match.
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/27 17:42
 * @param <W> The type of the object that is being matched.
 */
@EqualsAndHashCode(callSuper = false)
public class AndConnector<W> extends ConnectorMatcher.AbstractConnector<W> {

    /**
     * The element matchers that constitute this conjunction.
     */
    private final Matcher<? super W> left, right;

    /**
     * Creates a new conjunction matcher.
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
