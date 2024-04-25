import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LLP {
    // Feel free to add any methods here. Common parameters (e.g. number of processes)
    // can be passed up through a super constructor. Your code will be tested by creating
    // an instance of a sub-class, calling the solve() method below, and then calling the
    // sub-class's getSolution() method. You are free to modify anything else as long as
    // you follow this API (see SimpleTest.java)
    public static int n;
    public static CyclicBarrier barrier;

    // Checks whether process j is forbidden in the state vector G
    public abstract boolean forbidden(int j);

    // Advances on process j
    public abstract void advance(int j);

    public void solve() {
        // Implement this method. There are many ways to do this but you
        // should follow the following basic steps:
        // 1. Compute the forbidden states
        // 2. Advance on forbidden states in parallel
        // 3. Repeat 1 and 2 until there are no forbidden states
        Thread[] threads = new Thread[n];
        AtomicInteger a = new AtomicInteger(0);
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(new LLPRunnable(i, a));
            threads[i].start();
        }
        for (int i = 0; i < n; i++) {
            try {
                threads[i].join();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class LLPRunnable implements Runnable {

        int i;
        public AtomicInteger a;

        LLPRunnable(int i, AtomicInteger a){
            this.i = i;
            this.a = a;
        }

        @Override
        public void run() {
            try {
                while(true) {
                    //Check if forbidden
                    boolean toAdvance = true;
                    if (!forbidden(i)) {
                        a.incrementAndGet();
                        toAdvance = false;
                    }
                    barrier.await();
                    //Advance
                    if (toAdvance) {
                        advance(i);
                    }
                    barrier.await();
                    //Check if no forbidden states left
                    if (a.get() == n) {
                        break;
                    }
                    else {
                        a.set(0);
                    }
                    barrier.await();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}