package com.tianpl.opcode.example.javaagent;

import com.tianpl.opcode.Interceptor;
import com.tianpl.opcode.matcher.ConnectorMatcher;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashMap;
import java.util.Map;

/**
 * AgentArgs
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 *
 * @Date 17/11/29 09:26
 */
public class ExampleInterceptor extends Interceptor {
    private static ThreadLocal<Map<String,Long>> rtStart = ThreadLocal.withInitial(HashMap::new);
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
        if (executionId != null) {
            rtStart.get().put(executionId,System.currentTimeMillis());
        }
    }

    @Override
    protected void doOnThrowableThrown(Object source, Throwable throwable, String executionId) {
        System.out.println("doOnThrowableThrown");
    }

    @Override
    protected void doOnThrowableSurprise(Object source, Throwable throwable, String executionId) {
        System.out.println("doOnThrowableThrown");
    }

    @Override
    protected void doOnFinish(Object source, Object result, String executionId) {
        if (executionId != null) {
            Long start = rtStart.get().get(executionId);
            if (start != null) {
                rtStart.get().remove(executionId);
                System.out.println(System.currentTimeMillis() - start);
            }
        }
    }
}
