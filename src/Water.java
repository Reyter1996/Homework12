import java.util.concurrent.*;

public class Water {
    private static CyclicBarrier barrier = new CyclicBarrier(3);
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        Hydrogen hydrogen = new Hydrogen();
        Oxygen oxygen = new Oxygen();
        for (int i = 0; i < 9; i++) {
            executor.submit(hydrogen);
            executor.submit(oxygen);
            executor.submit(hydrogen);
        }
        executor.shutdown();
    }
    public static class Hydrogen implements Runnable{
        Semaphore semaphore = new Semaphore(2);
        @Override
        public void run() {
            releaseHydrogen();
        }
        private void releaseHydrogen()  {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                barrier.await();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("H");
            semaphore.release();
        }
    }
    public static class Oxygen implements Runnable{
        Semaphore semaphore = new Semaphore(1);
        @Override
        public void run() {
            releaseOxygen();
        }
        private void releaseOxygen(){
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.print("O");
            semaphore.release();
        }
    }
}
