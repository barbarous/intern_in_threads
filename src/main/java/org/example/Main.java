package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static int counter = 0;
    static int max_count = 0;

    public static void main(String[] args) throws InterruptedException {

        new Main().run();

    }

    void run() throws InterruptedException {

        for(int i=0;i<1_500_000;i++){
            if (i == 101) continue;
            String.valueOf(i).intern();
        }


        List<Thread> threads = new ArrayList();
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(() -> {
                int i1 = 101;
                try {
                    synchronized ("102") {
                        "102".wait();
                        synchronized (String.valueOf(i1).intern()) {
                            counter = counter + 1;
                            if (counter > max_count) max_count = counter;
                            counter = counter - 1;
                        }
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t.start();
            threads.add(t);
        }

        Thread.currentThread().sleep(2000);
        synchronized ("102") {
            "102".notifyAll();
        }
        threads.forEach(t-> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(max_count);
    }
}