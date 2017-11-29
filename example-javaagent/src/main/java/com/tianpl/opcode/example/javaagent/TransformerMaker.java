package com.tianpl.opcode.example.javaagent;

import com.tianpl.opcode.ClassModifier;
import com.tianpl.opcode.Interceptor;
import com.tianpl.opcode.InterceptorException;
import com.tianpl.opcode.example.target.ForExample;
import com.tianpl.opcode.example.target.ForExample1;
import com.tianpl.opcode.matcher.ConnectorMatcher;
import com.tianpl.opcode.matcher.TypeMatchers;
import com.tianpl.opcode.matcher.klass.ClassMatchers;
import com.tianpl.opcode.matcher.method.MethodMatchers;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.Instrumentation;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * AgentArgs
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 *
 * @Date 17/11/29 08:22
 */
public class TransformerMaker {
    public static void make(final AgentArgs agentArgs, Instrumentation instrumentation) {
        try {
            if (!AgentArgs.valida(agentArgs)) {
                throw new InterceptorException("JavaAgent参数设置错误");
            }
            ConnectorMatcher<Type> readBeforeMatcher;
            switch (agentArgs.getNameMatcherRule()) {
                case NAME_ALL:
                    readBeforeMatcher = TypeMatchers.any();
                    break;
                case NAME_START_WITH:
                    readBeforeMatcher = TypeMatchers.nameStartsWith(agentArgs.getMatchRule());
                    break;
                case NAME_END_WITH:
                    readBeforeMatcher = TypeMatchers.nameEndsWith(agentArgs.getMatchRule());
                    break;
                case NAMED:
                    readBeforeMatcher = TypeMatchers.named(agentArgs.getMatchRule());
                    break;
                case NAME_CONTAINS:
                    readBeforeMatcher = TypeMatchers.nameContains(agentArgs.getMatchRule());
                    break;
                default:
                    throw new InterceptorException("JavaAgent必须设置正确的代理实现方式.");
            }

            ConnectorMatcher<ClassNode> readAfterMather = ClassMatchers.not(ClassMatchers.isInterface());
            ConnectorMatcher<MethodNode> methodMather = MethodMatchers.not(MethodMatchers.isAbstract())
                    .and(MethodMatchers.not(MethodMatchers.isConstructor()))
                    .and(MethodMatchers.not(MethodMatchers.isTypeInitializer()))
                    .and(MethodMatchers.not(MethodMatchers.named("main")))
                    .and(MethodMatchers.withAnnotation(ForExample.class)
                        .or(MethodMatchers.withAnnotation(ForExample1.class)));

            Interceptor interceptor = new ExampleInterceptor(
                    readBeforeMatcher,
                    readAfterMather,
                    methodMather);
            String interceptorId = Interceptor.registerInterceptor(interceptor);
            instrumentation.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
                if (!isAncestor(TransformerMaker.class.getClassLoader(), loader) && !isTarget(readBeforeMatcher,className)) {
                    return classfileBuffer;
                }

                return AccessController.doPrivileged((PrivilegedAction<byte[]>) () -> ClassModifier.modify(className, classfileBuffer, interceptorId));
            });
        } catch (Throwable th) {
            th.printStackTrace(System.err);
        }
    }

    private static boolean isTarget(ConnectorMatcher<Type> matcher,String className) {
        return matcher.matches(Type.getObjectType(className));
    }

    private static boolean isAncestor(ClassLoader ancestor, ClassLoader cl) {
        return ancestor != null
                && cl != null
                && (ancestor.equals(cl) || isAncestor(ancestor, cl.getParent()));
    }
}
