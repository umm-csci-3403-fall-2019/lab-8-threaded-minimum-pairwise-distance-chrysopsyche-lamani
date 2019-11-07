package mpd;

import java.sql.SQLOutput;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    int[] values;
    long globalResult;
    int N;

    @Override
    public long minimumPairwiseDistance(int[] values) {
        this.values = values;
        N = this.values.length;
        Thread[] threads = new Thread[4];
        threads[0] = new Thread(new LowerLeft());
        threads[1] = new Thread(new MidLeft());
        threads[2] = new Thread(new MidRight());
        threads[3] = new Thread(new TopRight());
        try {
            for (int i = 0; i < 4; i++) {
                threads[i].start();
            }
            for (int i = 0; i < 4; i++) {
                threads[i].join();
            }
        } catch (InterruptedException ie) {
            System.out.println("Thread was interrupted");
            System.err.println(ie);
        }
        return globalResult;
    }

    private class LowerLeft implements Runnable {
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int i = 0; i < N/2; i++) {
                System.out.println(i);
                System.out.println(values[i]);
                for (int j = 0; j < i; j++) {
                    System.out.println(j);
                    System.out.println(values[j]);
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < localResult) {
                        localResult = diff;
                    }
                }
            }
            if (localResult < globalResult) {
                globalResult = localResult;
            }
        }
    }

    private class MidLeft implements Runnable {
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int j = 0; j < N/2; j++) {
                for (int i = N/2; i <= j + N/2; j++) {
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < localResult) {
                        localResult = diff;
                    }
                }
            }
            if (localResult < globalResult) {
                globalResult = localResult;
            }
        }
    }

    private class MidRight implements Runnable {
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int i = N/2; i < N; ++i) {
                for (int j = N/2; j + N/2 < i; ++j) {
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < localResult) {
                        localResult = diff;
                    }
                }
            }
            if (localResult < globalResult) {
                globalResult = localResult;
            }
        }
    }

    private class TopRight implements Runnable {
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int i = N/2; i < N; ++i) {
                for (int j = N/2; j < i; ++j) {
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < localResult) {
                        localResult = diff;
                    }
                }
            }
            if (localResult < globalResult) {
                globalResult = localResult;
            }
        }
    }
}
