class Seats {
    int seatsLeft = 5;

    synchronized void book(String name) {
        if (seatsLeft > 0) {
            seatsLeft--;
            System.out.println(name + " booked a seat");
        } else {
            System.out.println(name + " failed");
        }
    }
}

class SeatBooking {
    public static void main(String[] args) throws Exception {
        Seats seats = new Seats();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            int n = i + 1;
            threads[i] = new Thread(() -> seats.book("User " + n));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Seats Left: " + seats.seatsLeft);
    }
}
