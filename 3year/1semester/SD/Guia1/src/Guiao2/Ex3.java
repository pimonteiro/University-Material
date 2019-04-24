package Guiao2;

public class Ex3 extends Thread {
    private Banco b;

    public Ex3(Banco b){
        this.b = b;
    }

    public void run() {
        synchronized (b) {
            for (int i = 0; i < b.n_slots(); i++) {
                b.transferir(i, 90, 100);
            }
        }
    }
}
