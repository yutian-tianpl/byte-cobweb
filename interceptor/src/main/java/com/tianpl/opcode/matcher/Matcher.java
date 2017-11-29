package com.tianpl.opcode.matcher;

/**
 * Matcher
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/20 10:36
 *
 * 匹配器模式接口
 * 用于匹配目标类或者目标方法
 *
 * 匹配的思想来自于bytebuddy
 */
public interface Matcher<T> {

    /**
     * Matches a target against this element matcher.
     *
     * @param target The instance to be matched.
     * @return {@code true} if the given element is matched by this matcher or {@code false} otherwise.
     */
    boolean matches(T target);
}
