package Guiao5_3;

public class Reader extends Thread {

    private RWLock lock;

    public  Reader(RWLock l){
        this.lock = l;
    }

    @Override
    public void  run(){
        lock.readLock();
        try {
            System.out.println("Thread " + Thread.currentThread().getId() + "reading.");
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.readUnlock();
    }
}
