package com.tianpl.opcode;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Interceptor
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/22 15:26
 */
public abstract class Interceptor {
    static Type type = Type.getType(Interceptor.class);

    private static final Map<String, Interceptor> INSTANCES = new HashMap<>();

    private static final AtomicInteger interceptorCounter = new AtomicInteger(1);

    private final ThreadLocal<Boolean> ALREADY_NOTIFIED_FLAG = ThreadLocal.withInitial(() -> false);

    private final ThreadLocal<Integer> COUNTER = ThreadLocal.withInitial(() -> 1);

    private String id;

    public Interceptor() {
        this.id = String.valueOf(interceptorCounter.getAndIncrement());
    }

    public String getId() {
        return this.id;
    }

    private synchronized  String getExecutionId() {
        int counter = COUNTER.get();
        COUNTER.set(counter + 1);
        return Thread.currentThread().getId() + ":" + counter;
    }

    /**
     * 方法开始时执行
     * @param source
     * @param arg
     * @return
     */
    public final String onStart(Object source, Object[] arg) {
        if (ALREADY_NOTIFIED_FLAG.get()) {
            return null;
        }
        ALREADY_NOTIFIED_FLAG.set(true);
        String executionId = getExecutionId();
        try {
            doOnStart(source, arg, executionId);
        } catch (Throwable th) {
            th.printStackTrace(System.err);
        } finally {
            ALREADY_NOTIFIED_FLAG.set(false);
        }
        return executionId;
    }

    /**
     * 代码中抛出异常执行
     * @param source
     * @param throwable
     * @param executionId
     */
    public final void onThrowableThrown(Object source, Throwable throwable, String executionId) {
        if (ALREADY_NOTIFIED_FLAG.get()) {
            return;
        }
        ALREADY_NOTIFIED_FLAG.set(true);
        try {
            doOnThrowableThrown(source, throwable, executionId);
            doOnFinish(source, null, executionId);
        } catch (Throwable th) {
            th.printStackTrace(System.err);
        }
    }

    /**
     * 未捕获的异常时执行
     * @param source
     * @param throwable
     * @param executionId
     */
    public final void onThrowableSurprise(Object source, Throwable throwable, String executionId) {
        if (ALREADY_NOTIFIED_FLAG.get()) {
            return;
        }
        ALREADY_NOTIFIED_FLAG.set(true);
        try {
            doOnThrowableSurprise(source, throwable, executionId);
            doOnFinish(source, null, executionId);
        } catch (Throwable th) {
            th.printStackTrace(System.err);
        } finally {
            ALREADY_NOTIFIED_FLAG.set(false);
        }
    }

    /**
     * void返回时执行
     * @param source
     * @param executionId
     */
    public final void onVoidFinish(Object source, String executionId) {
        if (ALREADY_NOTIFIED_FLAG.get()) {
            return;
        }
        ALREADY_NOTIFIED_FLAG.set(true);
        try {
            doOnFinish(source, null, executionId);
        } catch (Throwable th) {
            th.printStackTrace(System.err);
        } finally {
            ALREADY_NOTIFIED_FLAG.set(false);
        }
    }

    /**
     *
     * @param source
     * @param o
     * @param executionId
     */
    public final void onFinish(Object source, Object o, String executionId) {
        if (ALREADY_NOTIFIED_FLAG.get()) {
            return;
        }
        ALREADY_NOTIFIED_FLAG.set(true);
        try {
            doOnFinish(source, o, executionId);
        } catch (Throwable th) {
            th.printStackTrace(System.err);
        } finally {
            ALREADY_NOTIFIED_FLAG.set(false);
        }
    }

    /**
     * 获得已注册的Interceptor实例
     * @param id
     * @return
     */
    public static Interceptor getInterceptor(String id) {
        return INSTANCES.get(id);
    }

    /**
     * 注册Interceptor实例
     * @param interceptor
     * @return
     */
    public static String registerInterceptor(Interceptor interceptor) {
        if (interceptor == null || interceptor.getId() == null || interceptor.getId().length() == 0) {
            throw new IllegalArgumentException("interceptor 不合法");
        }
        if (INSTANCES.containsKey(interceptor.getId())) {
            throw new IllegalArgumentException(interceptor.getId() + " already registered");
        }
        INSTANCES.put(interceptor.getId(), interceptor);
        return interceptor.getId();
    }

    /**
     * 删除已注册的Interceptor实例
     * @param id
     */
    public static void removeInterceptor(String id) {
        if (!INSTANCES.containsKey(id)) {
            throw new IllegalArgumentException(id + " is not registered");
        }
        INSTANCES.remove(id);
    }

    /**
     * 通过定义的Class获取Method
     * @param declaredClass
     * @param name
     * @param paramTypes
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object getInvocation(Class declaredClass, String name, Class... paramTypes) {
        if (name.equals("<init>")) {
            try {
                return declaredClass.getConstructor(paramTypes);
            } catch (Exception e) {
                // Anonymous classes
                return "init()";
            }
        }
        if (name.equals("<clinit>")) {
            return "clinit()";
        }
        try {
            return declaredClass.getDeclaredMethod(name, paramTypes);
        } catch (Exception e) {
            return name;
        }
    }

    /**
     * 拦截的目标方法开始位置执行操作
     * @param invocation 目标方法method对象
     * @param arg 目标方法参数
     * @param executionId 目标方法执行上下文，用于标记同一个方法
     */
    protected abstract void doOnStart(Object invocation, Object[] arg, String executionId);

    /**
     * 拦截的目标代码主动抛出异常时执行的操作
     * @param invocation 目标方法method对象
     * @param throwable 抛出的异常
     * @param executionId 目标方法执行上下文，用于标记同一个方法
     */
    protected abstract void doOnThrowableThrown(Object invocation, Throwable throwable, String executionId);

    /**
     * 拦截的目标代码意外异常抛出时执行的操作
     * @param invocation 目标方法method对象
     * @param throwable 抛出的异常
     * @param executionId 目标方法执行上下文，用于标记同一个方法
     */
    protected abstract void doOnThrowableSurprise(Object invocation, Throwable throwable, String executionId);

    /**
     * 希望在finally中执行的操作
     * 1.正常结束时执行
     * 2.代码异常抛出时执行
     * 3.代码未捕获异常抛出时执行
     * @param invocation 目标方法method对象
     * @param result 方法结果
     * @param executionId 目标方法执行上下文，用于标记同一个方法
     */
    protected abstract void doOnFinish(Object invocation, Object result, String executionId);

    /**
     * 使用instrument修改类时
     * ASM读取类之前匹配
     *
     * @param className the name of the class in the internal form of fully
     * qualified class and interface names as defined in
     * <i>The Java Virtual Machine Specification</i>. For example,
     * <code>"java/util/List"</code>.
     * @param byteCode class的字节
     * @return boolean
     */
    public boolean matchBeforeReadClass(String className, byte[] byteCode) {
        return className != null && byteCode != null;
    }

    /**
     * 使用instrument修改类时
     * ASM读取类之后匹配
     *
     * @param cn 已读入ASM的ClassNode实例
     * @return boolean
     */
    public abstract boolean matchAfterReadClass(ClassNode cn);

    /**
     * 使用instrument修改类时
     * ASM读取类的字节码以后开始访问方法时匹配方法
     *
     * @param cn ClassNode
     * @param mn MethodNode
     * @return boolean
     */
    public abstract boolean matchMethod(ClassNode cn, MethodNode mn);
}
