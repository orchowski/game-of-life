package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CarrierUnPinned {
    public static void main(String... arg) throws InterruptedException {
        Lock l = new ReentrantLock();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 20 + 1; i++) {
            var t = Thread.startVirtualThread(() -> {
                System.out.println(Thread.currentThread() + ": Before synchronized block");
                l.lock();
//                synchronized (CarrierPinned.class) {
                    System.out.println(Thread.currentThread() + ": Inside synchronized block");
                l.unlock();
            });
            threads.add(t);
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
