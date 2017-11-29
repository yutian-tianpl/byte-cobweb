package com.tianpl.opcode.example.javaagent;

import com.tianpl.opcode.Interceptor;
import com.tianpl.opcode.matcher.ConnectorMatcher;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ExampleInterceptor extends Interceptor {
    private ConnectorMatcher<Type> readBeforeMatcher;
    private ConnectorMatcher<ClassNode> readAfterMather;
    private ConnectorMatcher<MethodNode> methodMather;

    public ExampleInterceptor(ConnectorMatcher<Type> readBeforeMatcher, ConnectorMatcher<ClassNode> readAfterMather, ConnectorMatcher<MethodNode> methodMather) {
        super();
        this.readBeforeMatcher = readBeforeMatcher;
        this.readAfterMather = readAfterMather;
        this.methodMather = methodMather;
    }

    @Override
    public boolean matchBeforeReadClass(String className, byte[] byteCode) {
        return super.matchBeforeReadClass(className,byteCode) && readBeforeMatcher != null && readBeforeMatcher.matches(Type.getObjectType(className));
    }

    @Override
    public boolean matchAfterReadClass(ClassNode cn) {
        return readAfterMather != null && readAfterMather.matches(cn);
    }

    @Override
    public boolean matchMethod(ClassNode cn, MethodNode mn) {
        return methodMather != null && methodMather.matches(mn);
    }

    @Override
    protected void doOnStart(Object source, Object[] arg, String executionId) {

    }

    @Override
    protected void doOnThrowableThrown(Object source, Throwable throwable, String executionId) {
    }

    @Override
    protected void doOnThrowableSurprise(Object source, Throwable throwable, String executionId) {
    }

    @Override
    protected void doOnFinish(Object source, Object result, String executionId) {

    }
}
