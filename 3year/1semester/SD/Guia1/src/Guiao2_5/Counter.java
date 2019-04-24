package Guiao2_5;

public class Counter {
    public static long c;

    public synchronized void increment(){
        c++;
    }

    public long value() {
        return c;
    }
}
