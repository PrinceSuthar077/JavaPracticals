import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {}

class TestClass {

    @Run
    void test1() {
        System.out.println("Test 1 running");
    }

    void test2() {
        System.out.println("Test 2 running");
    }

    @Run
    void test3() {
        System.out.println("Test 3 running");
    }
}

public class Main {
    public static void main(String[] args) throws Exception {

        TestClass obj = new TestClass();
        int count = 0;

        for (Method method : TestClass.class.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Run.class)
                    && method.getParameterCount() == 0) {

                method.invoke(obj);
                count++;
            }
        }

        System.out.println("Tests ran: " + count);
    }
}
