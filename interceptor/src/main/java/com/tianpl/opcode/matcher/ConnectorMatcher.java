package com.tianpl.opcode.matcher;

/**
 * ConnectorMatcher
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/21 17:40
 * @param <S> The type of the object that is being matched.
 */
public interface ConnectorMatcher<S> extends Matcher<S> {

    /**
     * 创建一个And关系的match
     *
     * @param other 被连接matcher
     * @param <U>   被连接的matcher所匹配的类型
     * @return An AndConnector of this matcher and the other matcher.
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
