package com.tianpl.opcode;

/**
 * InterceptorException
 *
 * @Author yu.tian@tianpl.com
 * @Date 17/11/16 10:06
 */
public class InterceptorException extends RuntimeException {
    public InterceptorException() {
    }

    public InterceptorException(String message) {
        super(message);
    }

    public InterceptorException(String message, Throwable cause) {
        super(message, cause);
    }
}
