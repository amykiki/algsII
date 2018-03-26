package common;

/**
 * java移位符测试
 */
public class ShiftTest {
    public static String toBinary(int num) {
        return "int i = " + num + Integer.toBinaryString(num);
    }
    public static void main(String[] args) {
        testBook();
    }

    public static void testBook() {
        int i = -1;
        System.out.println(toBinary(i));
    }
}
