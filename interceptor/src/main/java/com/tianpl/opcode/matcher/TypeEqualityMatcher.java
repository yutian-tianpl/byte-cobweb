package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;
import org.objectweb.asm.Type;

/**
 * An element matcher that checks an object's equality to another object.
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 */
@EqualsAndHashCode(callSuper = false)
public class TypeEqualityMatcher extends EqualityMatcher<Type> {
    public TypeEqualityMatcher(Object value) {
        super(value);
    }

    @Override
    public boolean matches(Type target) {
        if (target != null) {
            return target.equals(this.value);
        } else return value == null;
    }
}
