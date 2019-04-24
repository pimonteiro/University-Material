package Guiao3;

class Client3 implements Runnable {
    Bank b;
    int i;

    public Client3(Bank b) {
        this.i = 2;
        this.b = b;
    }

    public void run() {
        this.b.criarConta(1500);
        this.b.take(i,1000);
        System.out.println("Cliente 3: " + this.i + " --> " + this.b.query(i));

    }
}
