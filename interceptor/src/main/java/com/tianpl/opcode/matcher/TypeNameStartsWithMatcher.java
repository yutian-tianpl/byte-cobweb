package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;
import org.objectweb.asm.Type;

@EqualsAndHashCode(callSuper = false)
public class TypeNameStartsWithMatcher extends ConnectorMatcher.AbstractConnector<Type> {

    private StrStartsWithMatcher matcher;

    public TypeNameStartsWithMatcher(String value) {
        this.matcher = new StrStartsWithMatcher(value);
    }

    @Override
    public boolean matches(Type target) {
        return target != null && (matcher.matches(target.getDescriptor()) || matcher.matches(target.getClassName()) || matcher.matches(target.getInternalName()));
    }

    @Override
    public String toString() {
        return "is(TypeNameStartsWithMatcher:" + matcher.toString() + ")";
    }
}
