package com.tianpl.opcode.matcher.klass;

import com.tianpl.opcode.matcher.ConnectorMatcher;
import com.tianpl.opcode.matcher.WithAnnotationMatcher;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

/**
 * ClassWithAnnotationMatcher
 *
 * @Author yu.tian@tianpl.com
 * @Date 17/11/21 14:20
 */
public class ClassWithAnnotationMatcher extends ConnectorMatcher.AbstractConnector<ClassNode> {
    private WithAnnotationMatcher matcher;

    public ClassWithAnnotationMatcher(Type type) {
        this.matcher = new WithAnnotationMatcher(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(ClassNode classNode) {
        return classNode != null && classNode.visibleAnnotations != null && !classNode.visibleAnnotations.isEmpty() && matcher.matches(classNode.visibleAnnotations);
    }
}
