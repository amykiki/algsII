package common;

import java.util.function.Consumer;

public class LambdaScopeTest {
    private int x = 0;
    class FirstLevel {
        private int x = 1;
        public void methodInFirstLevel(int x) {
//            x = 99;
            Consumer<Integer> myConsumer = (y) -> {
                System.out.println("x=" + x);
                System.out.println("y=" + y);
                System.out.println("this.x=" + this.x);
                System.out.println("LambdaScopeTest.this.x=" + LambdaScopeTest.this.x);
            };
            myConsumer.accept(x);
        }
    }



    public static void main(String[] args) {
        LambdaScopeTest st = new LambdaScopeTest();
        LambdaScopeTest.FirstLevel fl = st.new FirstLevel();
        fl.methodInFirstLevel(100);
    }
}
