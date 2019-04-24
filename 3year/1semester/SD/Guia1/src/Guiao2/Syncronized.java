package Guiao2;


public class Syncronized extends Thread {
    private int I;
    private Counter2 c;

    public Syncronized(int I, Counter2 c){
        this.I = I;
        this.c = c;
    }

    public void run(){
        for(int i = 0; i < I; i++){
            c.increment();
        }
    }
}
