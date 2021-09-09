package com.example.lib;

import java.util.HashMap;

public class TestPrivate {
    public void test(){
        StringBuffer stringBuffer = new StringBuffer();
    }

    public static void go(){
        System.out.println("f out");
    }

}

class A extends TestPrivate{
    public static void go(){
        System.out.println("s out");
    }

    public static void main(String[] args) {
        A a = new A();
        a.go();
        TestPrivate testPrivate = new TestPrivate();
        testPrivate.go();
        TestPrivate b = new A();
        b.go();
    }
}
