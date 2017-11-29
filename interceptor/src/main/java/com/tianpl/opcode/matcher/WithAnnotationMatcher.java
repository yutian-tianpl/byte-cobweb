package com.tianpl.opcode.matcher;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.List;

/**
 * WithAnnotationMatcher
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/21 14:20
 */
public class WithAnnotationMatcher extends ConnectorMatcher.AbstractConnector<List<AnnotationNode>> {
    private TypeEqualityMatcher matcher;

    public WithAnnotationMatcher(Type type) {
        this.matcher = new TypeEqualityMatcher(type);
    }

    @Override
    public boolean matches(List<AnnotationNode> annotationNodeList) {
        if (annotationNodeList == null || annotationNodeList.isEmpty()) return false;
        for (AnnotationNode an : annotationNodeList) {
            Type annotationType = Type.getType(an.desc);
            if (matcher.matches(annotationType)) return true;
        }
        return false;
    }
}
