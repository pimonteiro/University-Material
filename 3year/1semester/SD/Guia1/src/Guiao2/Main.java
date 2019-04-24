package Guiao2;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int I = Integer.parseInt
                (args[0]);
        int N = Integer.parseInt(args[1]);

        Counter2 c = new Counter2();

        ArrayList<Thread> thr = new ArrayList<>(N);
        for(int i = 0; i < N; i++){
            Syncronized ex = new Syncronized(I, c);
            thr.add(ex);
            ex.start();
        }

        for(Thread tr : thr){
            try {
                tr.join();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        System.out.println(c.getValue());
    }

}
