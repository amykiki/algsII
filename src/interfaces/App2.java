package interfaces;

public interface App2 extends App1 {
    @Override
    default void out() {
        System.out.println("This is App2 extends App1");
    }
}
