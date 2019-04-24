package Guiao4_3;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int tasks = 2;
        int N = 10;
        Barreira b = new Barreira(N);

        ArrayList<Thread> thr = new ArrayList<>(N*tasks);
        for(int i = 0; i < N*tasks; i++) {
            MyThread tr = new MyThread(b);
            thr.add(tr);
        }

        for(Thread tr : thr){
            tr.start();
        }

        for(Thread tr : thr){
            try {
                tr.join();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
