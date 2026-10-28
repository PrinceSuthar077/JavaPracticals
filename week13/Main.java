class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class Main {
    public static <T> T firstElement(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {
        Box<Integer> b1 = new Box<>();
        b1.set(100);
        System.out.println("Box Integer: " + b1.get());

        Box<String> b2 = new Box<>();
        b2.set("Java");
        System.out.println("Box String: " + b2.get());

        Pair<String, Integer> p1 = new Pair<>("Prince", 20);
        System.out.println("First: " + p1.getFirst());
        System.out.println("Second: " + p1.getSecond());

        Integer[] numbers = {10, 20, 30};
        String[] words = {"Hello", "Java", "World"};

        System.out.println("First Number: " + firstElement(numbers));
        System.out.println("First Word: " + firstElement(words));
    }
}
