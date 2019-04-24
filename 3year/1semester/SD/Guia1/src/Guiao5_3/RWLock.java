package Guiao5_3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class RWLock {

    private ReentrantLock l;
    private Condition Readlock;
    private Condition WriteLock;

    private int r_waiting;
    private int w_waiting;
    private final int max_operations = 5;
    private int readersOperations;
    private int writersOperations;

    private int writers;
    private int readers;

    public RWLock(){
        this.l = new ReentrantLock();
        this.Readlock = l.newCondition();
        this.WriteLock = l.newCondition();
        this.r_waiting = 0;
        this.w_waiting = 0;
        this.readersOperations = 0;
        this.writersOperations = 0;
        this.readers = 0;
        this.writers = 0;
    }

    void readLock(){
        l.lock();
        while( writers > 0 || (w_waiting > 0 &&  readersOperations >= max_operations)){
            try {
                r_waiting++;
                Readlock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        readers++;                      //Podem tar várias
        r_waiting--;



        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void readUnlock(){
       // if(w_waiting) operations--;

        WriteLock.signalAll();
        readers--;
        r_waiting--;
        l.unlock();
    }

    void writeLock(){

        l.lock();
        try {
            w_waiting++;
            while (readers > 0 || writers > 0 || (r_waiting > 0 && writersOperations >= max_operations)) {
                try {
                    WriteLock.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //Entra na secçao crítica e avisa que está alguém a executar
            writers = 1;
            w_waiting--;

            //Atualiza contadores de prioridade
            readersOperations = 0;
            writersOperations++;

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finally {
            l.unlock();
        }

    }

    void writeUnlock(){
        l.lock();
        try {
            writers--;

            if(writers == 0)
                WriteLock.signal();
        }
        finally {
            l.unlock();
        }
        l.unlock();
    }
}
