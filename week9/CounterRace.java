class Counter {
    int count = 0;

    synchronized void increment() {
        count++;
    }
}

class CounterRace {
    public static void main(String[] args) throws Exception {
        Counter c = new Counter();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    c.increment();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Final Count: " + c.count);
    }
}
