package thread.deadlock;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Intersection intersection = new Intersection();
        Thread threadA = new Thread(new TrainA(intersection));
        Thread threadB = new Thread(new TrainB(intersection));

        threadA.start();
        threadB.start();
    }

    static class TrainA implements Runnable {

        private final Intersection intersection;
        private final Random random = new Random();

        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                int sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                }

                intersection.takeRoadA();
            }
        }
    }

    static class TrainB implements Runnable {

        private final Intersection intersection;
        private final Random random = new Random();

        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                int sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                }

                intersection.takeRoadB();
            }
        }
    }

    static class Intersection {

        private final Object roadA = new Object();
        private final Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing through road A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public void takeRoadB() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing through road B");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
