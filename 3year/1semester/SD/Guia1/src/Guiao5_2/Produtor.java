package Guiao5_2;

public class Produtor extends Thread{

    private Warehouse wh;
    private int ops;

    public Produtor(Warehouse wh, int ops) {
        this.wh = wh;
        this.ops = ops;
    }

    @Override
    public void run(){
        for(int i = 0; i < ops; i++){
            int n= (0 + (int)(Math.random() * ((0 - 10) + 1)));
            String s = Integer.toString(n);
            wh.supply(s, 1);
        }
    }
}
