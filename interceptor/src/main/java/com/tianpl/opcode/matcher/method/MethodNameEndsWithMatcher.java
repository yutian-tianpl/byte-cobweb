package com.tianpl.opcode.matcher.method;

import com.tianpl.opcode.matcher.ConnectorMatcher;
import com.tianpl.opcode.matcher.StrEndsWithMatcher;
import lombok.EqualsAndHashCode;
import org.objectweb.asm.tree.MethodNode;

@EqualsAndHashCode(callSuper = false)
public class MethodNameEndsWithMatcher extends ConnectorMatcher.AbstractConnector<MethodNode> {

    private StrEndsWithMatcher matcher;

    public MethodNameEndsWithMatcher(String value) {
        this.matcher = new StrEndsWithMatcher(value);
    }

    @Override
    public boolean matches(MethodNode mn) {
        return mn != null && matcher.matches(mn.name);
    }

    @Override
    public String toString() {
        return "is(MethodNameEndsWithMatcher:" + matcher.toString() + ")";
    }
}
