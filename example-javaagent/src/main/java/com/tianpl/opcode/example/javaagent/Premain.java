package com.tianpl.opcode.example.javaagent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * Premain
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/2 17:26
 */
public class Premain {
    public static void premain(String agentArgs,Instrumentation inst)
            throws ClassNotFoundException,UnmodifiableClassException {
        AgentArgs args = AgentArgs.of(agentArgs);
        ASMTreeAgent.install(args,inst);
        }
}
