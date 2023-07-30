package thread.semaphore.study;

import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Semaphore semaphore = new Semaphore(0);

        Thread thread0 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": start: " + semaphore.availablePermits());
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":   end: " + semaphore.availablePermits());
        });
        thread0.start();

        Thread.sleep(100);

        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": start: " + semaphore.availablePermits());
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":   end: " + semaphore.availablePermits());
        });
        thread1.start();
        Thread.sleep(100);

        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": start: " + semaphore.availablePermits());
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":   end: " + semaphore.availablePermits());
        });
        thread2.start();
        Thread.sleep(100);

        Thread thread3 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": before release: " + semaphore.availablePermits());
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + ":  after release: " + semaphore.availablePermits());
        });
        thread3.start();
        Thread.sleep(100);

        Thread thread4 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": before release: " + semaphore.availablePermits());
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + ":  after release: " + semaphore.availablePermits());
        });
        thread4.start();
        Thread.sleep(100);

        Thread thread5 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": before release: " + semaphore.availablePermits());
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + ":  after release: " + semaphore.availablePermits());
        });
        thread5.start();
        Thread.sleep(100);

        Thread thread6 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": before release: " + semaphore.availablePermits());
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + ":  after release: " + semaphore.availablePermits());
        });
        thread6.start();
        Thread.sleep(100);

        System.out.println(Thread.currentThread().getName() + ": " + semaphore.availablePermits());
    }

}
