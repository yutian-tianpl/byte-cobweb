package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;

/**
 * An element matcher that reverses the matching result of another matcher.
 *
 * @param <T> The type of the matched entity.
 */
@EqualsAndHashCode(callSuper = false)
public class NegatingMatcher<T> extends ConnectorMatcher.AbstractConnector<T> {

    /**
     * The element matcher to be negated.
     */
    private final Matcher<? super T> matcher;

    /**
     * Creates a new negating element matcher.
     *
     * @param matcher The element matcher to be negated.
     */
    public NegatingMatcher(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(T target) {
        return !matcher.matches(target);
    }

    @Override
    public String toString() {
        return "not(" + matcher + ')';
    }
}
