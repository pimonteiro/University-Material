package Guiao2_5;

class Client2 implements Runnable {
    Bank b;

    public Client2(Bank b) { this.b = b; }

    public void run() {
        this.b.take(1,1000);
    }
}
