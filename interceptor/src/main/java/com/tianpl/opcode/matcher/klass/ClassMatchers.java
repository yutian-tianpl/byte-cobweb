package com.tianpl.opcode.matcher.klass;

import com.tianpl.opcode.matcher.Matcher;
import com.tianpl.opcode.matcher.ConnectorMatcher;
import com.tianpl.opcode.matcher.method.Matchers;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;

/**
 * MethodMatchers
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/20 15:38
 */
public class ClassMatchers {
    public static ClassAccessMatcher isAbstract() {
        return new ClassAccessMatcher(Opcodes.ACC_ABSTRACT);
    }

    public static ClassAccessMatcher isPrivate() {
        return new ClassAccessMatcher(Opcodes.ACC_PRIVATE);
    }

    public static ClassAccessMatcher isProtect() {
        return new ClassAccessMatcher(Opcodes.ACC_PROTECTED);
    }

    public static ClassAccessMatcher isPublic() {
        return new ClassAccessMatcher(Opcodes.ACC_PUBLIC);
    }

    public static ClassAccessMatcher isFinal() {
        return new ClassAccessMatcher(Opcodes.ACC_FINAL);
    }

    public static ClassAccessMatcher isInterface() {
        return new ClassAccessMatcher(Opcodes.ACC_INTERFACE);
    }

    public static ClassWithAnnotationMatcher withAnnotation(Class<? extends Annotation> clz) {
        return new ClassWithAnnotationMatcher(Type.getType(clz));
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

    public static void main(String[] args) {

    }
}
