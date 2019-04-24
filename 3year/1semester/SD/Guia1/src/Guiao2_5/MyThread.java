package Guiao2_5;

public class MyThread extends Thread {
    private Counter c;

    MyThread(Counter c) {
        this.c = c;
    }

    @Override
    public void run() {
        for(int i = 0; i<10000; i++)
            c.increment();
    }

/**
 public void run() {

 int c=0;

 for(int i = 0; i < 10; i++)
 c++;
 System.out.println(c);
 }
 **/
}
