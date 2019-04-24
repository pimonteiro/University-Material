package Guiao3;

class Client2 implements Runnable {
    Bank b;
    int i;

    public Client2(Bank b) {
        this.i = 1;
        this.b = b;
    }

    public void run() {
        this.b.criarConta(500);
        this.b.take(i,1000);
        System.out.println("Cliente 2: " + this.i + " --> " + this.b.query(i));

    }
}
