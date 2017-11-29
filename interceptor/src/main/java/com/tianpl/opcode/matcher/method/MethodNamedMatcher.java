package com.tianpl.opcode.matcher.method;

import com.tianpl.opcode.matcher.ConnectorMatcher;
import com.tianpl.opcode.matcher.StrEqualsMatcher;
import lombok.EqualsAndHashCode;
import org.objectweb.asm.tree.MethodNode;

/**
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * An element matcher that checks an object's equality to another object.
 *
 */
@EqualsAndHashCode(callSuper = false)
public class MethodNamedMatcher extends ConnectorMatcher.AbstractConnector<MethodNode> {

    private StrEqualsMatcher matcher;

    /**
     * Creates an element matcher that tests for equality.
     *
     * @param value The object that is checked to be equal to the matched value.
     */
    public MethodNamedMatcher(String value) {
        this.matcher = new StrEqualsMatcher(value);
    }

    @Override
    public boolean matches(MethodNode mn) {
        return mn != null && matcher.matches(mn.name);
    }

    @Override
    public String toString() {
        return "is(MethodNamedMatcher:" + matcher.toString() + ")";
    }
}
