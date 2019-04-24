package Guiao5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer {
    private Integer[] buffer;
    private int max_size;
    private int i;
    private ReentrantLock lock;
    private Condition Empty;
    private Condition Full;

    public BoundedBuffer(int n){
        this.buffer = new Integer[n];
        this.max_size = n;
        this.i = 0;
        this.lock = new ReentrantLock();
        this.Empty = lock.newCondition();
        this.Full = lock.newCondition();
    }

    public void put(int v){
        this.lock.lock();
        while (this.i == this.max_size) {
            try {
                this.Full.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.buffer[i] = v;
        this.Empty.signal();
        i++;
        this.lock.unlock();
    }

    public int get() {
        this.lock.lock();
        int v;
        while (this.i == 0) {
            try {
                this.Empty.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        i--;
        v = this.buffer[i];
        this.Full.signal();
        this.lock.unlock();
        return v;
    }
}
