package com.tianpl.opcode.matcher.method;

import com.tianpl.opcode.matcher.ConnectorMatcher;
import com.tianpl.opcode.matcher.StrContainsMatcher;
import lombok.EqualsAndHashCode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 */
@EqualsAndHashCode(callSuper = false)
public class MethodNameContainsMatcher extends ConnectorMatcher.AbstractConnector<MethodNode> {

    private StrContainsMatcher matcher;

    /**
     * Creates an element matcher that tests for equality.
     *
     * @param value The object that is checked to be equal to the matched value.
     */
    public MethodNameContainsMatcher(String value) {
        this.matcher = new StrContainsMatcher(value);
    }

    @Override
    public boolean matches(MethodNode mn) {
        return mn != null && matcher.matches(mn.name);
    }

    @Override
    public String toString() {
        return "is(MethodNameContainsMatcher:" + matcher.toString() + ")";
    }
}
