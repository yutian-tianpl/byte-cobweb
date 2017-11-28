package com.tianpl.opcode.matcher.method;

import com.tianpl.opcode.matcher.ConnectorMatcher;
import com.tianpl.opcode.matcher.WithAnnotationMatcher;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

/**
 * WithAnnotationMatcher
 *
 * @Author yu.tian@tianpl.com
 * @Date 17/11/21 14:20
 */
public class MethodWithAnnotationMatcher extends ConnectorMatcher.AbstractConnector<MethodNode> {
    private WithAnnotationMatcher matcher;

    public MethodWithAnnotationMatcher(Type type) {
        this.matcher = new WithAnnotationMatcher(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(MethodNode methodNode) {
        return methodNode != null && methodNode.visibleAnnotations != null && !methodNode.visibleAnnotations.isEmpty() && matcher.matches(methodNode.visibleAnnotations);
    }
}
