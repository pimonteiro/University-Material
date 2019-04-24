package Guiao4;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        BoundedBuffer b = new BoundedBuffer(10);
        int produtores = 3;
        int consumidores = 7;
        int total = produtores + consumidores;

        ArrayList<Thread> thr = new ArrayList<>(total);
        for(int i = 0; i < produtores-1; i++){
            Produtor tr = new Produtor(b, 33);
            thr.add(tr);
        }
        Produtor trs = new Produtor(b, 34);
        thr.add(trs);

        for(int i = 0; i < consumidores-1; i++){
            Consumidor tr = new Consumidor(b,14);
            thr.add(tr);
        }
        Consumidor trt = new Consumidor(b, 16);
        thr.add(trt);

        long start = System.currentTimeMillis();
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
        long end = System.currentTimeMillis();
        System.out.println((end-start));
    }
}
