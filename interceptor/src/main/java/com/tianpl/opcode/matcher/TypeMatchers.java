package com.tianpl.opcode.matcher;

import com.tianpl.opcode.matcher.method.Matchers;
import org.objectweb.asm.Type;

/**
 * TypeMatchers
 *
 * @Author yu.tian@tianpl.com
 * @Date 17/11/20 18:32
 */
public class TypeMatchers {
    public static ConnectorMatcher<Type> named(String match) {
        return new TypeNamedMatcher(match);
    }

    public static ConnectorMatcher<Type> nameStartsWith(String match) {
        return new TypeNameStartsWithMatcher(match);
    }

    public static ConnectorMatcher<Type> nameEndsWith(String match) {
        return new TypeNameEndsWithMatcher(match);
    }

    public static ConnectorMatcher<Type> nameContains(String match) {
        return new TypeNameContainsMatcher(match);
    }

    public static <T> ConnectorMatcher<T> not(Matcher<? super T> matcher) {
        return Matchers.not(matcher);
    }

    public static <T> ConnectorMatcher<T> any() {
        return Matchers.any();
    }

    public static <T> ConnectorMatcher<T> none() {
        return Matchers.none();
    }
}
