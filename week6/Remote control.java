interface Switchable {
    void on();
    void off();

    default void toggle() {
        on();
    }
}

class Fan implements Switchable {
    public void on() {
        System.out.println("Fan ON");
    }

    public void off() {
        System.out.println("Fan OFF");
    }
}

class Light implements Switchable {
    public void on() {
        System.out.println("Light ON");
    }

    public void off() {
        System.out.println("Light OFF");
    }
}

interface SwitchRule {
    boolean maySwitchOn(Switchable device, int hour);
}

public class Main {
    public static void main(String[] args) {

        Switchable[] devices = {
            new Fan(),
            new Light()
        };

        for (Switchable device : devices) {
            device.toggle();
        }

        SwitchRule rule1 = new SwitchRule() {
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour <= 22;
            }
        };

        SwitchRule rule2 = (device, hour) -> hour >= 8 && hour <= 20;

        System.out.println(rule1.maySwitchOn(devices[0], 10));
        System.out.println(rule2.maySwitchOn(devices[1], 23));
    }
}
