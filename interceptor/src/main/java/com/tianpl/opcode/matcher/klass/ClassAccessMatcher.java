package com.tianpl.opcode.matcher.klass;

import com.tianpl.opcode.matcher.ConnectorMatcher;
import org.objectweb.asm.tree.ClassNode;

/**
 * MethodAccessMatch
 *
 * @Author yu.tian@tianpl.com
 * @Date 17/11/21 10:35
 */
public class ClassAccessMatcher extends ConnectorMatcher.AbstractConnector<ClassNode> {

    private int access;

    public ClassAccessMatcher(int access) {
        this.access = access;
    }

    @Override
    public boolean matches(ClassNode target) {
        return target != null && (target.access & access) != 0;
    }
}
