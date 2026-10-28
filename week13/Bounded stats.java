public class Main {
    public static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            return 0.0;
        }

        double sum = 0;

        for (N value : values) {
            sum += value.doubleValue();
        }

        return sum / values.length;
    }

    public static void main(String[] args) {
        Integer[] a = {10, 20, 30};
        Double[] b = {2.5, 3.5, 4.0};
        Long[] c = {100L, 200L, 300L};
        System.out.println("Integer Average: " + average(a));
        System.out.println("Double Average: " + average(b));
        System.out.println("Long Average: " + average(c));
    }
}
