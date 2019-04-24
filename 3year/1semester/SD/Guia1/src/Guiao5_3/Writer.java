package Guiao5_3;

public class Writer extends Thread {

    private RWLock lock;

    public Writer(RWLock l){
        this.lock = l;
    }

    @Override
    public void run(){
        lock.writeLock();
        try {
            System.out.println("Thread " + Thread.currentThread().getId() + "writting.");
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread " + Thread.currentThread().getId() + "done writting.");
        lock.writeUnlock();
    }
}
