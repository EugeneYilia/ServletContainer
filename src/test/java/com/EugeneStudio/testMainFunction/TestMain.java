package com.EugeneStudio.testMainFunction;

public class TestMain {
    public static void main(String...args) {
        System.out.printf("21");
        int[] a = {1,1,1,1,1,2,2};
        a(a);
        a(1,1,1,312,312,312);
    }

    public static  void a(int...a){
        for(int i = 0; i <a.length;i++)
        System.out.println(a[i]);
    }
}
