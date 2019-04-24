package Guiao4_3;

public class Barreira {
    private int n;
    private int counter;
    private int round;

    public Barreira(int n){
        this.n = n;
        this.counter = 0;
        this.round = 0;
    }

    public synchronized void esperar(){
        int myid = ++this.counter;
        int myround = this.round;

        System.out.println("My id: " + myid);
        if(myid == this.n){
            //reset counter
            this.counter = 0;
            this.round++;
            this.notifyAll();
            System.out.println("Last thread is notifying: " + myid);
        }
        else {
            while(myround == this.round) {
                try {
                    System.out.println("Waiting for last thread: " + myid);
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Exiting........ " + myid);
        }
    }
}
