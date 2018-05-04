package com.EugeneStudio.testSubstring;

public class TestSubstring {
    public static void main(String[] args) {//bytecode里面的字节码的版本就是当前编译器使用的编译器的版本
        String string = "abcdefg";//4.3.1    204
        System.out.println(string.length());
        //System.out.println(string.charAt(string.length()));
        System.out.println(string.substring(8));
    }
}
