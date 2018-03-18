package impl;

import interfaces.App1;

public class MainTest {
    public static void test1() {
        Impl1A a = new Impl1A();
        a.out();
        Impl1B b = new Impl1B();
        b.out();
        Impl2A a2 = new Impl2A();
        a2.out();
        System.out.println("=======Impl3A===========");
        Impl3A a3 = new Impl3A();
        a3.out();
    }
    public static void main(String[] args) {
        test1();
        App1.out2();
    }
}
