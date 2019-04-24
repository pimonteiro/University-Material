package Guiao4;

import java.util.Random;

public class Consumidor extends Thread {

    private BoundedBuffer b;
    private int ops;

    public Consumidor(BoundedBuffer b, int n){
        this.b = b;
        this.ops = n;
    }

    @Override
    public void run() {
        for(int i = 0; i < ops; i++){
            int x = b.get();
            try {
                sleep(1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }
}
