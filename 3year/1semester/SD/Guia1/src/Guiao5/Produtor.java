package Guiao5;

import Guiao4.BoundedBuffer;

import java.util.Random;

public class Produtor extends Thread {

    private BoundedBuffer b;
    private int ops;

    public Produtor(BoundedBuffer b, int n){
        this.b = b;
        this.ops = n;
    }

    @Override
    public void run() {
        for(int i = 0; i < this.ops; i++){
            Random rnd = new Random();
            int x =  rnd.nextInt(10 + 1) + 1;
            b.put(x);
            try {
                sleep(500);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
