package Guiao5_2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Warehouse {

    private ReentrantLock l;
    private Condition available;
    private Map<String,Integer> stock;

    public Warehouse(){
        this.l = new ReentrantLock();
        this.stock = new HashMap<>();
        this.available = l.newCondition();

    }

    public void supply(String item, int quantity){
        l.lock();
        int i;
        System.out.println("Thread " + Thread.currentThread().getId() + "supplying " + item + "with " + quantity);
        i = stock.get(item);
        i += quantity;
        stock.put(item,i);
        available.signal();
        System.out.println("Thread " + Thread.currentThread().getId() + "done supplying " + item);
        l.unlock();
    }

    public void consume(String[] items){
        l.lock();
        for(String i : items){
            System.out.println("Thread " + Thread.currentThread().getId() + "consuming " + i);
            while(stock.get(i) == 0) {
                try {
                    System.out.println("Thread " + Thread.currentThread().getId() + "waiting for stock on item " + i + "....... ");
                    available.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int item = stock.get(i);
            item--;
            stock.put(i,item);
            System.out.println("Thread " + Thread.currentThread().getId() + "done consuming " + i);
        }
        l.unlock();
    }
}
