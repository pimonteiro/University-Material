package Guiao5_2;

public class Consumidor extends Thread {
    private Warehouse wh;
    private int ops;

    public Consumidor(Warehouse wh, int ops) {
        this.wh = wh;
        this.ops = ops;
    }

    @Override
    public void run(){
        for(int i = 0; i < ops; i++){
            int cc = 3;                                         // consumir 3
            String[] items = new String[cc];
            for(int j = 0; j < cc; j++) {
                int n = (0 + (int) (Math.random() * ((0 - 10) + 1)));
                String s = Integer.toString(n);
                items[j] = s;
            }
             wh.consume(items);
        }
    }
}
