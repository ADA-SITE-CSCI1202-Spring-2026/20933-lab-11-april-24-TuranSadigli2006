class MathTask implements Runnable {
    @Override
    public void run() {
        double result = 0;
        for (int i = 0; i < 10_000_000; i++) {
            result += Math.pow(i, 3) + (i * 2); 
        }
    }
}

public class DynamicScaling {
    public static void main(String[] args) {

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Detected Logical Processors: " + cores);


        long startTime = System.currentTimeMillis();
        
        Thread[] threads = new Thread[cores];

        for (int i = 0; i < cores; i++) {
            threads[i] = new Thread(new MathTask());
            threads[i].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken with " + cores + " threads: " + (endTime - startTime) + "ms");

        startTime = System.currentTimeMillis();
        new MathTask().run(); 
        endTime = System.currentTimeMillis();
        System.out.println("Time taken with 1 thread: " + (endTime - startTime) + "ms");
    }
}