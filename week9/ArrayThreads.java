class SharedTotal {
    static long total = 0;

    static synchronized void add(long value) {
        total += value;
    }
}

class ArrayThreads {
    public static void main(String[] args) throws Exception {
        int[] arr = new int[100000];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + 1;
        }

        Thread[] threads = new Thread[4];
        int size = arr.length / 4;

        long start = System.nanoTime();

        for (int i = 0; i < 4; i++) {
            int startIndex = i * size;
            int endIndex = (i + 1) * size;
            threads[i] = new Thread(() -> {
                for (int j = startIndex; j < endIndex; j++) {
                    SharedTotal.add(arr[j]);
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long end = System.nanoTime();
        System.out.println("Total: " + SharedTotal.total);
        System.out.println("Time: " + (end - start) + " ns");
    }
}
