class Buffer {
    int data;
    boolean available = false;

    synchronized void produce(int n) throws InterruptedException {
        while (available)
            wait();

        data = n;
        available = true;
        System.out.println("Produced: " + n);
        notify();
    }

    synchronized int consume() throws InterruptedException {
        while (!available)
            wait();
        int n = data;
        available = false;
        System.out.println("Consumed: " + n);
        notify();
        return n;
    }
}
public class Main {
    public static void main(String[] args) {
        Buffer b = new Buffer();
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++)
                    b.produce(i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++)
                    b.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
