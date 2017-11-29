package com.tianpl.opcode.example.javaagent;

import java.lang.instrument.Instrumentation;

/**
 * ASMCoreAgent
 *
 * @Author yu.tian@tianpl.com
 *         blog.tianpl.com
 * @Date 17/11/17 15:47
 */
public class ASMTreeAgent {
    static void install(AgentArgs agentArgs, Instrumentation inst) {
        TransformerMaker.make(agentArgs,inst);
    }
}
