package com.tianpl.opcode.example.target;

/**
 * Example
 *
 * @Author yu.tian@mtime.com
 * @Date 17/11/29 11:55
 */
public class Example {
    @ForExample
    public void example() {
        System.out.println(Example.class.getName() + "#example");
    }

    @ForExample1
    public void example1() {
        System.out.println(Example.class.getName() + "#example1");
    }

    @ForExample
    @ForExample1
    public void example12() {
        example();
        example1();
        System.out.println(Example.class.getName() + "#example12");
    }

    public static void main(String[] args) {
        Example example = new Example();
        example.example12();
    }
}
