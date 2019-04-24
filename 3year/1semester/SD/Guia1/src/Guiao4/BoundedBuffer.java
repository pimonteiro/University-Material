package Guiao4;

import java.util.ArrayList;

public class BoundedBuffer {
    private Integer[] buffer;
    private int max_size;
    private int i;

    public BoundedBuffer(int n){
        this.buffer = new Integer[n];
        this.max_size = n;
        this.i = 0;
    }

    public void put(int v){
        synchronized (this) {
            while (this.i == this.max_size) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.buffer[i] = v;
            this.notifyAll();
            i++;
        }
    }

    public int get(){
        int v;
        synchronized (this){
            while(this.i == 0){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i--;
            v = this.buffer[i];
            this.notifyAll();
        }
        return v;
    }
}
