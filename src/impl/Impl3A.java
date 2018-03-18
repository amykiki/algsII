package impl;

import interfaces.App1;
import interfaces.App3;

public class Impl3A implements App1, App3 {
    @Override
    public void out() {
        App1.super.out();
        App3.super.out();
        System.out.println("This is in Class Impl3A!");
    }
}
