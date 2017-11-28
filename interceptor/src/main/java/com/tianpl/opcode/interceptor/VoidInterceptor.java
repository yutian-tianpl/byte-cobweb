package com.tianpl.opcode.interceptor;

import com.tianpl.opcode.Interceptor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class VoidInterceptor extends Interceptor {
    public VoidInterceptor() {
        super();
    }

    @Override
    public boolean matchBeforeReadClass(String className, byte[] byteCode) {
        return true;
    }

    @Override
    public boolean matchAfterReadClass(ClassNode cn) {
        return false;
    }

    @Override
    public boolean matchMethod(ClassNode cn, MethodNode mn) {
        return true;
    }

    @Override
    protected void doOnStart(Object invocation, Object[] arg, String executionId) {
    }

    @Override
    protected void doOnThrowableThrown(Object invocation, Throwable throwable, String executionId) {
    }

    @Override
    protected void doOnThrowableSurprise(Object invocation, Throwable throwable, String executionId) {
    }

    @Override
    protected void doOnFinish(Object invocation, Object result, String executionId) {
    }

}
