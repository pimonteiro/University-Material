package Guiao5_3;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        RWLock lock = new RWLock();

        List<Thread> thr = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            Writer rd1 = new Writer(lock);
            Reader rd2 = new Reader(lock);
            thr.add(rd2);
            thr.add(rd1);
        }

        for(Thread t : thr) {
            t.start();
        }

        for(Thread t : thr){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
