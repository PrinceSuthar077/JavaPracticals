interface Notifier {
    void send(String message);
}

interface Urgent {
}

class UrgentNotifier implements Notifier, Urgent {
    private Notifier notifier;

    UrgentNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    public void send(String message) {
        notifier.send(message);
    }
}

public class Main {
    public static void main(String[] args) {

        Notifier email = message ->
            System.out.println("Email: " + message);

        Notifier sms = message ->
            System.out.println("SMS: " + message);

        Notifier[] senders = {
            new UrgentNotifier(email),
            sms
        };

        String message = "Important notification";

        for (int i = 0; i < senders.length; i++) {
            senders[i].send(message);

            if (senders[i] instanceof Urgent) {
                senders[i].send(message);
            }
        }
    }
}
