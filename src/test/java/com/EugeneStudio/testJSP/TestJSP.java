package com.EugeneStudio.testJSP;

import jasper.ServletParser;

import java.io.File;

public class TestJSP {
    public static void main(String[] args) {
        ServletParser s = new ServletParser(new File("webroot/index.jsp"));
        try {
            System.out.println(s.parse());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
