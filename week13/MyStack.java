package JavaPracticals.week13;

public class MyStack {
    
}
class MyStack<T> {
    private Object[] arr;
    private int top;

    public MyStack(int size) {
        arr = new Object[size];
        top = -1;
    }

    public void push(T value) {
        if (top == arr.length - 1) {
            System.out.println("Stack is full");
            return;
        }

        arr[++top] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (top == -1) {
            System.out.println("Stack is empty");
            return null;
        }

        T value = (T) arr[top];
        arr[top] = null;
        top--;
        return value;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (top == -1) {
            System.out.println("Stack is empty");
            return null;
        }

        return (T) arr[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }
}

public class Main {
    public static void main(String[] args) {
        MyStack<Integer> s1 = new MyStack<>(5);

        s1.push(10);
        s1.push(20);
        s1.push(30);

        System.out.println("Integer Stack Top: " + s1.peek());
        System.out.println("Popped: " + s1.pop());
        System.out.println("Popped: " + s1.pop());

        MyStack<String> s2 = new MyStack<>(5);

        s2.push("Java");
        s2.push("Python");

        System.out.println("String Stack Top: " + s2.peek());
        System.out.println("Popped: " + s2.pop());

        System.out.println("Is String Stack Empty: " + s2.isEmpty());
    }
}
