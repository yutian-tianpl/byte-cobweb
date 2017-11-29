package com.tianpl.opcode.matcher;

import lombok.EqualsAndHashCode;

/**
 * StrEqualsMatcher
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/21 17:43
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
