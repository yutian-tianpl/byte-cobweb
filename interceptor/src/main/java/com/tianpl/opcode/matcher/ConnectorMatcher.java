package com.tianpl.opcode.matcher;

/**
 * ConnectorMatcher
 *
 * A junctions allows to chain different {@link Matcher}s in a readable manner.
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/27 17:40
 * @param <S> The type of the object that is being matched.
 */
public interface ConnectorMatcher<S> extends Matcher<S> {

    /**
     * Creates a conjunction where this matcher and the {@code other} matcher must both be matched in order
     * to constitute a successful match. The other matcher is only invoked if this matcher constitutes a successful
     * match.
     *
     * @param other The second matcher to consult.
     * @param <U>   The type of the object that is being matched. Note that Java's type inference might not
     *              be able to infer the common subtype of this instance and the {@code other} matcher such that
     *              this type must need to be named explicitly.
     * @return A conjunction of this matcher and the other matcher.
     */
    <U extends S> ConnectorMatcher<U> and(Matcher<? super U> other);

    /**
     * Creates a disjunction where either this matcher or the {@code other} matcher must be matched in order
     * to constitute a successful match. The other matcher is only invoked if this matcher constitutes an
     * unsuccessful match.
     *
     * @param other The second matcher to consult.
     * @param <U>   The type of the object that is being matched. Note that Java's type inference might not
     *              be able to infer the common subtype of this instance and the {@code other} matcher such that
     *              this type must need to be named explicitly.
     * @return A disjunction of this matcher and the other matcher.
     */
    <U extends S> ConnectorMatcher<U> or(Matcher<? super U> other);

    /**
     * A base implementation of {@link ConnectorMatcher}.
     *
     * @param <V> The type of the object that is being matched.
     */
    abstract class AbstractConnector<V> implements ConnectorMatcher<V> {

        @Override
        public <U extends V> ConnectorMatcher<U> and(Matcher<? super U> other) {
            return new AndConnector<>(this, other);
        }

        @Override
        public <U extends V> ConnectorMatcher<U> or(Matcher<? super U> other) {
            return new OrConnector<>(this, other);
        }
    }
}
