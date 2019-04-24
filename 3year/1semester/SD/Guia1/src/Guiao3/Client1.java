package Guiao3;

class Client1 implements Runnable {
    Bank b;
    int i;

    public Client1(Bank b) {
        this.i = 0;
        this.b = b;
    }

    public void run() {
        this.i = this.b.criarConta(1000);
        this.b.transferir(i,1,1000);
        System.out.println("Cliente 1: " + this.i + " --> " + this.b.query(i));
    }
}