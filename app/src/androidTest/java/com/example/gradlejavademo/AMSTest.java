package com.example.gradlejavademo;

public class AMSTest {

   public static void main(String[] args) {
       test("main");
    }

    public static void test(String name){
        AMSTest amsTest = new AMSTest();
        int result = (int) amsTest.add(123, 456.2f);

        System.out.println(String.format("result: %f",result));

//        System.out.println(String.format("visit method : %s",name));
//        System.out.println("aaa");
    }

    public  float add(int a,float b){
       return a+b;
    }
}
