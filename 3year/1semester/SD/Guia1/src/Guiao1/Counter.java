package Guiao1;

public class Counter {

    private int value;

    public Counter(){
        this.value = 0;
    }

    public void increment(){
        this.value++;
    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int x){
        this.value = x;
    }
}
