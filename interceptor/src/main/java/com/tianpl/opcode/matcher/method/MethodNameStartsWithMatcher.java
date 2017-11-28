package com.tianpl.opcode.matcher.method;

import com.tianpl.opcode.matcher.ConnectorMatcher;
import com.tianpl.opcode.matcher.StrStartsWithMatcher;
import lombok.EqualsAndHashCode;
import org.objectweb.asm.tree.MethodNode;

@EqualsAndHashCode(callSuper = false)
public class MethodNameStartsWithMatcher extends ConnectorMatcher.AbstractConnector<MethodNode> {

    private StrStartsWithMatcher matcher;

    public MethodNameStartsWithMatcher(String value) {
        this.matcher = new StrStartsWithMatcher(value);
    }

    @Override
    public boolean matches(MethodNode mn) {
        return mn != null && matcher.matches(mn.name);
    }

    @Override
    public String toString() {
        return "is(TypeNameStartsWithMatcher:" + matcher.toString() + ")";
    }
}
