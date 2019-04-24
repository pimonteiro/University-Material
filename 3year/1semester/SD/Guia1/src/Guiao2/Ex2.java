package Guiao2;

public class Ex2 extends Thread {
    private Banco b;
    private static int take = 52;
    private static int give = 100;

    public Ex2(Banco b){
        this.b = b;
    }

    public void run(){
        synchronized (b) {
            for (int j = 0; j < b.n_slots(); j++) {
                b.credito(j, give);
            }

            for (int i = 0; i < b.n_slots(); i++) {
                b.debito(i, take);
            }
        }
    }
}
