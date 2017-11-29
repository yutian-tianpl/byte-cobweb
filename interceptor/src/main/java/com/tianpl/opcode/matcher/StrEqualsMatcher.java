package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;

/**
 * An element matcher that checks an object's equality to another object.
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 */
@EqualsAndHashCode(callSuper = false)
public class StrEqualsMatcher extends EqualityMatcher<String> {
    public StrEqualsMatcher(Object value) {
        super(value);
    }

    @Override
    public boolean matches(String target) {
        if (target != null) {
            return target.equals(this.value);
        } else return value == null;
    }
}
