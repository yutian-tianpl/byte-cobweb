package com.tianpl.opcode;

/**
 * MeyeAgentException
 *
 * @Author yu.tian@tianpl.com
 * @Date 17/11/16 10:06
 */
public class OpcdeException extends RuntimeException {
    public OpcdeException() {
    }

    public OpcdeException(String message) {
        super(message);
    }

    public OpcdeException(String message, Throwable cause) {
        super(message, cause);
    }
}
