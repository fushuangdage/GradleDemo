package com.example.gradlejavademo;


public class InsertHelper {
    public static final String TAG = "InsertHelper";

    public static void before( int a) {
//        Log.d(TAG, (" before class " + target + "  " + a));
        System.out.println( (" before class "  + "  " + a));
    }

    public static void after( int a) {
//        Log.d(TAG, " after class " + target + "  " + a);
       System.out.println( " after class " + "  " + a);
    }

}
