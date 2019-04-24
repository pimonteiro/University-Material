package Guiao1;

public class Ex1 extends Thread{

    private int I;
    private Counter c;

    public Ex1(int I, Counter c){
        this.I = I;
        this.c = c;
    }

    public void run(){
        for(int i = 0; i < I; i++){
            c.increment();
        }
    }
}
