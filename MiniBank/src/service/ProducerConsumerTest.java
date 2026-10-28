package service;

public class ProducerConsumerTest {

    private final int[] buffer = new int[3];
    private int count = 0;
    private int in = 0;
    private int out = 0;

    public synchronized void produce(int transaction) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }

        buffer[in] = transaction;
        in = (in + 1) % buffer.length;
        count++;

        System.out.println("Produced transaction: " + transaction);

        notify();
    }

    public synchronized int consume() throws InterruptedException {
        while (count == 0) {
            wait();
        }

        int transaction = buffer[out];
        out = (out + 1) % buffer.length;
        count--;

        System.out.println("Consumed transaction: " + transaction);

        notify();

        return transaction;
    }

    public static void main(String[] args) {
        ProducerConsumerTest buffer = new ProducerConsumerTest();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.produce(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.consume();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All transactions processed.");
    }
}