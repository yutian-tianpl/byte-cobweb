package com.tianpl.opcode.matcher.method;

import com.tianpl.opcode.matcher.ConnectorMatcher;
import com.tianpl.opcode.matcher.Matcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import java.lang.annotation.Annotation;

/**
 * MethodMatchers
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/20 15:38
 */
public class MethodMatchers {
    /**
     * The internal name of a Java constructor.
     */
    private static final String CONSTRUCTOR_INTERNAL_NAME = "<init>";

    /**
     * The internal name of a Java static initializer.
     */
    private static final String TYPE_INITIALIZER_INTERNAL_NAME = "<clinit>";

    public static ConnectorMatcher<MethodNode> named(String match) {
        return new MethodNamedMatcher(match);
    }

    public static ConnectorMatcher<MethodNode> nameStartsWith(String match) {
        return new MethodNameStartsWithMatcher(match);
    }

    public static ConnectorMatcher<MethodNode> nameEndsWith(String match) {
        return new MethodNameEndsWithMatcher(match);
    }

    public static ConnectorMatcher<MethodNode> nameContains(String match) {
        return new MethodNameContainsMatcher(match);
    }

    public static ConnectorMatcher<MethodNode> isConstructor() {
        return new MethodNameContainsMatcher(CONSTRUCTOR_INTERNAL_NAME);
    }

    public static ConnectorMatcher<MethodNode> isTypeInitializer() {
        return new MethodNameContainsMatcher(TYPE_INITIALIZER_INTERNAL_NAME);
    }

    public static MethodAccessMatch isAbstract() {
        return new MethodAccessMatch(Opcodes.ACC_ABSTRACT);
    }

    public static MethodAccessMatch isStatic() {
        return new MethodAccessMatch(Opcodes.ACC_STATIC);
    }

    public static MethodAccessMatch isPrivate() {
        return new MethodAccessMatch(Opcodes.ACC_PRIVATE);
    }

    public static MethodAccessMatch isProtect() {
        return new MethodAccessMatch(Opcodes.ACC_PROTECTED);
    }

    public static MethodAccessMatch isPublic() {
        return new MethodAccessMatch(Opcodes.ACC_PUBLIC);
    }

    public static MethodAccessMatch isFinal() {
        return new MethodAccessMatch(Opcodes.ACC_FINAL);
    }

    public static MethodAccessMatch isNative() {
        return new MethodAccessMatch(Opcodes.ACC_NATIVE);
    }

    public static MethodWithAnnotationMatcher withAnnotation(Class<? extends Annotation> clz) {
        return new MethodWithAnnotationMatcher(Type.getType(clz));
    }

    public static <T> ConnectorMatcher<T> not(Matcher<? super T> matcher) {
        return Matchers.not(matcher);
    }

    public static <T> ConnectorMatcher<T> any() {
        return Matchers.any();
    }

    public static <T> ConnectorMatcher<T> none() {
        return Matchers.none();
    }
}
