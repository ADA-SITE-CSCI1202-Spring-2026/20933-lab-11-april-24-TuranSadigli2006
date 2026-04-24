class SharedResource {
    private int data;
    private boolean bChanged = false;

    // 2 & 4. The Wait Logic & Synchronized Constraint
    public synchronized int get() {
        // While loop is used to handle "spurious wakeups"
        while (!bChanged) {
            try {
                wait(); // Consumer waits here until producer calls notify()
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        bChanged = false; // Reset flag after consuming
        notify(); // Optional: notify producer that space is available
        return data;
    }

    // 3 & 4. The Notification & Synchronized Constraint
    public synchronized void set(int value) {
        while (bChanged) {
            try {
                wait(); // Producer waits if data hasn't been consumed yet
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        this.data = value;
        this.bChanged = true;
        System.out.println("Produced: " + value);
        notify(); // Wake up the waiting consumer
    }
}

public class CoordinationExample {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        // Producer Thread
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                resource.set(i);
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        });

        // Consumer Thread
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Consumed: " + resource.get());
            }
        });

        producer.start();
        consumer.start();
    }
}