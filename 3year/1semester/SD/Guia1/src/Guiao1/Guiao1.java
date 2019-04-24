package Guiao1;

import java.util.ArrayList;

public class Guiao1 {

    public static void main(String[] args) {
        int I = Integer.parseInt
                (args[0]);
        int N = Integer.parseInt(args[1]);

        Counter c = new Counter();

        ArrayList<Thread> thr = new ArrayList<>(N);
        for(int i = 0; i < N; i++){
            Ex1 ex = new Ex1(I, c);
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
