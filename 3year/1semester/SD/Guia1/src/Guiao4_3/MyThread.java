package Guiao4_3;


public class MyThread extends Thread {

    private Barreira b;

    public MyThread(Barreira b){
        super();
        this.b = b;
    }

    @Override
    public void run() {
        b.esperar();
        System.out.println("Hello There fella!");
    }
}
