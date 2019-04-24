package Guiao2;

public class Counter2 {

    private int value;

    public Counter2(){
        this.value = 0;
    }

    public synchronized void increment(){
        this.value++;
    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int x){
        this.value = x;
    }
}
