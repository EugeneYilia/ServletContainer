package com.EugeneStudio.testReflection;

import javax.servlet.Servlet;
import java.io.File;

public class TestReflection {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName("webroot/ModernServlet.class");
        Servlet servlet = (Servlet) clazz.newInstance();
    }
}
