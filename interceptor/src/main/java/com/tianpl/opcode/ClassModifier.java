package com.tianpl.opcode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * ClassModifier
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/23 16:26
 */
public class ClassModifier {
    /**
     * 类修改器
     * 修改方法
     * @param className 类名
     * @param originalClass 原始类字节数组
     * @param interceptorId 注册的拦截器id
     * @return modified类字节数组
     */
    @SuppressWarnings("unchecked")
    public static byte[] modify(String className, byte[] originalClass,String interceptorId) {
        Interceptor interceptor = Interceptor.getInterceptor(interceptorId);
        if (!interceptor.matchBeforeReadClass(className, originalClass)) {
            return originalClass;
        }
        ClassReader cr = new ClassReader(originalClass);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        if (!interceptor.matchAfterReadClass(cn)) {
            return originalClass;
        }

        List<MethodNode> methods = cn.methods;
        boolean transformed = false;
        for (MethodNode node : methods) {
            if (MethodModifier.modifyMethod(interceptor,cn,node)) {
                transformed = true;
            }
        }
        if (!transformed) {
            return originalClass;
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        cn.accept(cw);

        return cw.toByteArray();

    }
}
