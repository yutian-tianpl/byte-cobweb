package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;
import org.objectweb.asm.Type;

@EqualsAndHashCode(callSuper = false)
public class TypeNameEndsWithMatcher extends ConnectorMatcher.AbstractConnector<Type> {

    private StrEndsWithMatcher matcher;

    public TypeNameEndsWithMatcher(String value) {
        this.matcher = new StrEndsWithMatcher(value);
    }

    @Override
    public boolean matches(Type target) {
        return target != null && (matcher.matches(target.getDescriptor()) || matcher.matches(target.getClassName()) || matcher.matches(target.getInternalName()));
    }

    @Override
    public String toString() {
        return "is(TypeNameEndsWithMatcher:" + matcher.toString() + ")";
    }
}
