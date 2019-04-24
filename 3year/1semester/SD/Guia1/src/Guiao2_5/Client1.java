package Guiao2_5;

class Client1 implements Runnable {
    Bank b;

    public Client1(Bank b) { this.b = b; }

    public void run() {
        this.b.transferir(0,1,1000);
    }
}