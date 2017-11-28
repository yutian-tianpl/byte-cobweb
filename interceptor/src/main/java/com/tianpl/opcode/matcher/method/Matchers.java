package com.tianpl.opcode.matcher.method;

import com.tianpl.opcode.matcher.BooleanMatcher;
import com.tianpl.opcode.matcher.Matcher;
import com.tianpl.opcode.matcher.NegatingMatcher;
import com.tianpl.opcode.matcher.ConnectorMatcher;

/**
 * Matchers
 *
 * @Author yu.tian@tianpl.com
 * @Date 17/11/21 11:00
 */
public class Matchers {
    public static <T> ConnectorMatcher<T> not(Matcher<? super T> matcher) {
        return new NegatingMatcher<>(matcher);
    }

    public static <T> ConnectorMatcher<T> any() {
        return new BooleanMatcher<>(true);
    }

    public static <T> ConnectorMatcher<T> none() {
        return new BooleanMatcher<>(false);
    }
}
