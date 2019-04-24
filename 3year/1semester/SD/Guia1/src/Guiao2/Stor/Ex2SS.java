package Guiao2.Stor;

import Guiao2.Banco;

public class Ex2SS extends Thread {
    private Banco b;
    private static int take = 52;
    private static int give = 100;

    public Ex2SS(Banco b){
        this.b = b;
    }

    public void run(){
        int j = 5;
        int i = 90;

        //b.transferir(i, , give);
    }
}
