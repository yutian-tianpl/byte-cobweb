package com.tianpl.opcode.matcher.method;

import com.tianpl.opcode.matcher.ConnectorMatcher;
import org.objectweb.asm.tree.MethodNode;

/**
 * MethodAccessMatch
 *
 * @Author yu.tian@tianpl.com
 * @Date 17/11/21 10:35
 */
public class MethodAccessMatch extends ConnectorMatcher.AbstractConnector<MethodNode> {

    private int access;

    public MethodAccessMatch(int access) {
        this.access = access;
    }

    @Override
    public boolean matches(MethodNode target) {
        return target != null && (target.access & access) != 0;
    }
}
