package com.tianpl.opcode;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassModifier 方法修改器
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/23 16:26
 */
public class MethodModifier {
    /***
     * MethodModifier上下文
     * 1.持有当前拦截器
     * 2.持有当前ClassNode
     * 3.持有当前MethodNode
     * 4.负责记录本地变量的索引
     * 5.负责计算本地变量表当前位置(offset)
     * 6.负责记录当前方法标识在变量表中的索引(executionIdVarIndex)
     */
    static class MethodModifierContext {
        final Interceptor interceptor;
        final ClassNode cn;
        final MethodNode mn;
        private final AtomicInteger offset;
        /*
         * 当前被增强方法的Method实例在本地变量表存储的可用位置索引
         * 1.该索引之前有this在本地变量表的索引
         * 2.该索引之前有方法参数在本地变量表的索引
         * 3.此位置为本地变量表第一个可用的可操作的
         */
        private int invocationVarIndex = -1;
        /*
        * 当前被增强的方法标识(executionId)的在本地变量表中的索引位置
        * */
        private int executionIdVarIndex = -1;

        public MethodModifierContext(Interceptor interceptor, ClassNode cn, MethodNode mn) {
            this.interceptor = interceptor;
            this.cn = cn;
            this.mn = mn;
            this.offset = new AtomicInteger(isStatic(mn) ? 0 : 1);
        }

        public int getOffset() {
            return offset.get();
        }

        public int addAndGetOffset(int delta) {
            return offset.addAndGet(delta);
        }

        public int incrementAndGetOffset() {
            return offset.incrementAndGet();
        }

        public void calculateInvocationVarIndex() {
            if (this.invocationVarIndex != -1) return;
            this.invocationVarIndex = getFistAvailablePosition();
        }

        public void calculateExecutionIdVarIndex() {
            if (this.executionIdVarIndex != -1) return;
            this.executionIdVarIndex = getFistAvailablePosition();
        }

        public int getInvocationVarIndex() {
            if (this.invocationVarIndex == -1) {
                throw new InterceptorException("Method Invocation variable index使用前必须先计算.calculateInvocationVarIndex()");
            }
            return this.invocationVarIndex;
        }

        public int getExecutionIdVarIndex() {
            if (this.executionIdVarIndex == -1) {
                throw new InterceptorException("Method executionId variable index使用前必须先计算.calculateExecutionIdVarIndex()");
            }
            return this.executionIdVarIndex;
        }

        /**
         * 获得方法的本地变量表(LocalVariable)中第一个可用的位置(index)
         * @return
         */
        int getFistAvailablePosition() {
            return mn.maxLocals + offset.get();
        }
    }

    /**
     * 修改目标方法
     * @param interceptor
     * @param cn
     * @param mn
     * @return
     */
    public static boolean modifyMethod(Interceptor interceptor, ClassNode cn , MethodNode mn) {
        if (interceptor == null || cn == null || mn == null) {
            throw new InterceptorException("错误的参数:参数不能null");
        }
        if (!interceptor.matchMethod(cn, mn) || isAbstract(mn)) {
            return false;
        }

        MethodModifierContext context = new MethodModifierContext(interceptor,cn,mn);
        LabelNode startNode = new LabelNode();
        asmMethodStart(context,startNode);
        asmMethodReturn(context);
        asmMethodThrow(context);
        asmMethodThrowablePassed(context,startNode);

        return true;
    }

    /**
     * 组装被拦截方法开始的动作
     */
    private static void asmMethodStart(MethodModifierContext context, LabelNode startNode) {
        Type[] methodArguments = Type.getArgumentTypes(context.mn.desc);
        InsnList il = new InsnList();
        int methodParametersIndex = calculateParametersIndex(context,il,methodArguments);
        asmInvocation(context,il,methodArguments);
        //
        context.calculateInvocationVarIndex();
        il.add(new VarInsnNode(Opcodes.ASTORE, context.getInvocationVarIndex())); //将当前Method实例存入本地变量表
        context.mn.maxLocals++;
        asmInterceptor(context,il);

        il.add(new VarInsnNode(Opcodes.ALOAD, context.getInvocationVarIndex()));
        il.add(new VarInsnNode(Opcodes.ALOAD, methodParametersIndex));
        il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                Interceptor.type.getInternalName(), "onStart",
                "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/String;", false));

        context.calculateExecutionIdVarIndex();
        il.add(new VarInsnNode(Opcodes.ASTORE, context.getExecutionIdVarIndex()));
        context.mn.maxLocals++;
        context.mn.instructions.insert(startNode);
        context.mn.instructions.insert(il);
    }

    /**
     * 组装被拦截方法正常返回时的拦截动作
     */
    @SuppressWarnings("unchecked")
    private static void asmMethodReturn(MethodModifierContext context) {
        InsnList il = context.mn.instructions;

        Iterator<AbstractInsnNode> it = il.iterator();
        while (it.hasNext()) {
            AbstractInsnNode abstractInsnNode = it.next();

            switch (abstractInsnNode.getOpcode()) {
                case Opcodes.RETURN:
                    il.insertBefore(abstractInsnNode, createVoidReturnInstructions(context));
                    break;
                case Opcodes.IRETURN:
                case Opcodes.LRETURN:
                case Opcodes.FRETURN:
                case Opcodes.ARETURN:
                case Opcodes.DRETURN:
                    il.insertBefore(abstractInsnNode, createReturnInstructions(context));
            }
        }
    }

    /**
     * 组装被拦截方法在方法内throw时的拦截动作
     */
    @SuppressWarnings("unchecked")
    private static void asmMethodThrow(MethodModifierContext context) {
        InsnList il = context.mn.instructions;
        Iterator<AbstractInsnNode> it = il.iterator();
        while (it.hasNext()) {
            AbstractInsnNode abstractInsnNode = it.next();
            switch (abstractInsnNode.getOpcode()) {
                case Opcodes.ATHROW:
                    il.insertBefore(abstractInsnNode, createThrowInstructions(context));
                    break;
            }
        }

    }

    /**
     * 组装被拦截方法在未捕获异常throw时的拦截动作
     */
    private static void asmMethodThrowablePassed(MethodModifierContext context, LabelNode startNode) {
        InsnList il = context.mn.instructions;
        LabelNode endNode = new LabelNode();
        il.add(endNode);
        asmCatchBlock(context,startNode, endNode);

    }

    /**
     * 组装Method Invocation
     * @see java.lang.reflect.Method
     * 将会传递到
     * @param context
     * @param il
     * @param methodArguments
     */
    private static void asmInvocation(MethodModifierContext context, InsnList il, Type[] methodArguments) {
        il.add(TreeInsn.getPushInsn(methodArguments.length));
        il.add(new TypeInsnNode(Opcodes.ANEWARRAY, "java/lang/Class"));
        int parameterClassesIndex = context.getFistAvailablePosition();
        il.add(new VarInsnNode(Opcodes.ASTORE, parameterClassesIndex));
        context.mn.maxLocals++;
        for (int i = 0; i < methodArguments.length; i++) {
            il.add(new VarInsnNode(Opcodes.ALOAD, parameterClassesIndex));
            il.add(TreeInsn.getPushInsn(i));
            il.add(TreeInsn.getClassReferenceInsn(methodArguments[i], context.cn.version & 0xFFFF));
            il.add(new InsnNode(Opcodes.AASTORE));
        }
        il.add(TreeInsn.getClassConstantReference(Type.getObjectType(context.cn.name), context.cn.version & 0xFFFF));
        il.add(new LdcInsnNode(context.mn.name));
        il.add(new VarInsnNode(Opcodes.ALOAD, parameterClassesIndex));
        il.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                Interceptor.type.getInternalName(), "getInvocation",
                "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;", false));
    }

    /**
     * 计算方法参数列表中的元素在本地变量表的位置
     * @param context
     * @param il
     * @param methodArguments
     * @return
     */
    private static int calculateParametersIndex(MethodModifierContext context, InsnList il, Type[] methodArguments) {
        il.add(TreeInsn.getPushInsn(methodArguments.length));
        il.add(new TypeInsnNode(Opcodes.ANEWARRAY, "java/lang/Object"));
        int methodParametersIndex = context.getFistAvailablePosition();
        il.add(new VarInsnNode(Opcodes.ASTORE, methodParametersIndex));
        context.mn.maxLocals++;
        for (int i = 0; i < methodArguments.length; i++) {
            il.add(new VarInsnNode(Opcodes.ALOAD, methodParametersIndex));
            il.add(TreeInsn.getPushInsn(i));
            il.add(TreeInsn.getLoadInsn(methodArguments[i],
                    calculateArgPosition(context.getOffset(), methodArguments,i)));
            MethodInsnNode mNode = TreeInsn
                    .getWrapperConstructionInsn(methodArguments[i]);
            if (mNode != null) {
                il.add(mNode);
            }
            il.add(new InsnNode(Opcodes.AASTORE));
        }
        return methodParametersIndex;
    }

    /**
     * 计算给定参数位置的索引
     * @param offset
     * @param arguments
     * @param argNo
     * @return
     */
    private static int calculateArgPosition(int offset, Type[] arguments, int argNo) {
        int ret = argNo + offset;
        for (int i = 0; i < arguments.length && i < argNo; i++) {
            char charType = arguments[i].getDescriptor().charAt(0);
            if (charType == 'J' || charType == 'D') {
                ret++;
            }
        }
        return ret;
    }

    /**
     * 组装拦截器
     * @param context
     * @param il
     */
    private static void asmInterceptor(MethodModifierContext context , InsnList il) {
        il.add(new LdcInsnNode(context.interceptor.getId()));
        il.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                Interceptor.type.getInternalName(), "getInterceptor",
                "(Ljava/lang/String;)" + Interceptor.type.getDescriptor(), false));
    }

    /**
     * 组装Catch块
     * @param context
     * @param startNode
     * @param endNode
     */
    @SuppressWarnings("unchecked")
    private static void asmCatchBlock(MethodModifierContext context, LabelNode startNode, LabelNode endNode) {
        InsnList il = new InsnList();
        LabelNode handlerNode = new LabelNode();
        il.add(handlerNode);

        int exceptionVariablePosition = context.getFistAvailablePosition();
        il.add(new VarInsnNode(Opcodes.ASTORE, exceptionVariablePosition));
        context.incrementAndGetOffset(); // Actualizamos el offset
        asmInterceptor(context,il);
        il.add(new VarInsnNode(Opcodes.ALOAD, context.getInvocationVarIndex()));
        il.add(new VarInsnNode(Opcodes.ALOAD, exceptionVariablePosition));
        il.add(new VarInsnNode(Opcodes.ALOAD, context.getExecutionIdVarIndex()));
        il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                Interceptor.type.getInternalName(), "onThrowableSurprise",
                "(Ljava/lang/Object;Ljava/lang/Throwable;Ljava/lang/String;)V", false));

        il.add(new VarInsnNode(Opcodes.ALOAD, exceptionVariablePosition));
        il.add(new InsnNode(Opcodes.ATHROW));

        TryCatchBlockNode blockNode = new TryCatchBlockNode(startNode, endNode, handlerNode, null);

        context.mn.tryCatchBlocks.add(blockNode);
        context.mn.instructions.add(il);
    }

    /**
     * 创建无返回值情况下返回的指令序列
     * @see org.objectweb.asm.Opcodes#RETURN
     */
    private static InsnList createVoidReturnInstructions(MethodModifierContext context) {
        InsnList il = new InsnList();
        asmInterceptor(context,il);
        il.add(new VarInsnNode(Opcodes.ALOAD, context.getInvocationVarIndex()));
        il.add(new VarInsnNode(Opcodes.ALOAD, context.getExecutionIdVarIndex()));
        il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                Interceptor.type.getInternalName(), "onVoidFinish",
                "(Ljava/lang/Object;Ljava/lang/String;)V", false));

        return il;
    }

    /**
     * 创建无返回值情况下返回的指令序列
     * @see org.objectweb.asm.Opcodes#IRETURN
     * @see org.objectweb.asm.Opcodes#LRETURN
     * @see org.objectweb.asm.Opcodes#FRETURN
     * @see org.objectweb.asm.Opcodes#ARETURN
     * @see org.objectweb.asm.Opcodes#DRETURN
     */
    private static InsnList createReturnInstructions(MethodModifierContext context) {
        Type methodReturnType = Type.getReturnType(context.mn.desc);
        InsnList il = new InsnList();

        int returnedVariablePosition = context.getFistAvailablePosition();
        il.add(TreeInsn.getStoreInsn(methodReturnType, returnedVariablePosition));

        char charType = methodReturnType.getDescriptor().charAt(0);
        if (charType == 'J' || charType == 'D') {
            context.addAndGetOffset(2);
        } else {
            context.addAndGetOffset(1);
        }
        asmInterceptor(context,il);
        il.add(new VarInsnNode(Opcodes.ALOAD, context.getInvocationVarIndex()));
        il.add(TreeInsn.getLoadInsn(methodReturnType, returnedVariablePosition));
        MethodInsnNode mNode = TreeInsn.getWrapperConstructionInsn(methodReturnType);
        if (mNode != null) {
            il.add(mNode);
        }
        il.add(new VarInsnNode(Opcodes.ALOAD, context.getExecutionIdVarIndex()));
        il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                Interceptor.type.getInternalName(), "onFinish",
                "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;)V", false));

        il.add(TreeInsn.getLoadInsn(methodReturnType, returnedVariablePosition));

        return il;
    }

    /**
     * 创建抛出异常时的指令序列
     * @see org.objectweb.asm.Opcodes#ATHROW
     */
    private static InsnList createThrowInstructions(MethodModifierContext context) {
        InsnList il = new InsnList();

        int exceptionVariablePosition = context.getFistAvailablePosition();
        il.add(new VarInsnNode(Opcodes.ASTORE, exceptionVariablePosition));

        context.incrementAndGetOffset(); // Actualizamos el offset
        asmInterceptor(context,il);
        il.add(new VarInsnNode(Opcodes.ALOAD, context.getInvocationVarIndex()));
        il.add(new VarInsnNode(Opcodes.ALOAD, exceptionVariablePosition));
        il.add(new VarInsnNode(Opcodes.ALOAD, context.getExecutionIdVarIndex()));
        il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                Interceptor.type.getInternalName(), "onThrowableThrown",
                "(Ljava/lang/Object;Ljava/lang/Throwable;Ljava/lang/String;)V", false));

        il.add(new VarInsnNode(Opcodes.ALOAD, exceptionVariablePosition));

        return il;
    }

    private static boolean isAbstract(MethodNode m) {
        return (m.access & Opcodes.ACC_ABSTRACT) != 0;
    }

    private static boolean isStatic(MethodNode m) {
        return (m.access & Opcodes.ACC_STATIC) != 0;
    }

    private static boolean isPublic(MethodNode m) {
        return (m.access & Opcodes.ACC_PUBLIC) != 0;
    }
}
