package Guiao5_2;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Warehouse wh = new Warehouse();
        int produtores = 2;
        int consumidores = 8;
        int total = produtores + consumidores;

        ArrayList<Thread> thr = new ArrayList<>(total);
        for(int i = 0; i < produtores; i++){
            Produtor tr = new Produtor(wh, 50);
            thr.add(tr);
        }

        for(int i = 0; i < consumidores; i++){
            Consumidor tr = new Consumidor(wh, 20);
            thr.add(tr);
        }

        System.out.println("Starting Warehouse......");
        for(Thread t : thr){
            t.start();
        }

        for(Thread t : thr){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Finished at " + System.currentTimeMillis());
    }

}
