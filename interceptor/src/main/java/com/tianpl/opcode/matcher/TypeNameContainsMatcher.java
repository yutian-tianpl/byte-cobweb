package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;
import org.objectweb.asm.Type;

/**
 * TypeNameContainsMatcher
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/22 17:43
 *
 */
@EqualsAndHashCode(callSuper = false)
public class TypeNameContainsMatcher extends ConnectorMatcher.AbstractConnector<Type> {

    private StrContainsMatcher matcher;

    /**
     * Creates an element matcher that tests for equality.
     *
     * @param value The object that is checked to be equal to the matched value.
     */
    public TypeNameContainsMatcher(String value) {
        this.matcher = new StrContainsMatcher(value);
    }

    @Override
    public boolean matches(Type target) {
        return target != null && (matcher.matches(target.getDescriptor()) || matcher.matches(target.getClassName()) || matcher.matches(target.getInternalName()));
    }

    @Override
    public String toString() {
        return "is(TypeNameContainsMatcher:" + matcher.toString() + ")";
    }
}
