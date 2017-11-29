package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;
import org.objectweb.asm.Type;

/**
 * TypeEqualityMatcher
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/21 17:43
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
