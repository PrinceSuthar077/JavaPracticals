public class Main {
    static Object a = new Object();
    static Object b = new Object();

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            synchronized (a) {
                System.out.println("Thread 1 locked A");

                synchronized (b) {
                    System.out.println("Thread 1 locked B");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (b) {
                System.out.println("Thread 2 locked B");

                synchronized (a) {
                    System.out.println("Thread 2 locked A");
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
