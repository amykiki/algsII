package interfaces;

public interface App1 {
    default void out() {
        System.out.println("This is In Interface App1!");
    }

    static void out2() {
        System.out.println("This is Out2 In Interface App1");
    }

}
