package com.tianpl.opcode.utils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class Helper {
    /**
     * 格式化输出字节码
     * @param bytecode
     */
    public static void viewByteCode(byte[] bytecode) {
        ClassReader cr = new ClassReader(bytecode);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        final List<MethodNode> mns = cn.methods;
        Printer printer = new Textifier();
        TraceMethodVisitor mp = new TraceMethodVisitor(printer);
        for (MethodNode mn : mns) {
            InsnList inList = mn.instructions;
            System.out.println(mn.name);
            for (int i = 0; i < inList.size(); i++) {
                inList.get(i).accept(mp);
                StringWriter sw = new StringWriter();
                printer.print(new PrintWriter(sw));
                printer.getText().clear();
                System.out.print(sw.toString());
            }
        }
    }
}
