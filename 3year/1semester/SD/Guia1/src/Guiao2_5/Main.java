package Guiao2_5;

public class Main {
    public static void main(String[] args){
        int N = 3; // Number of accounts
        Bank b = new Bank(N);
        b.put(0,1000);

        Client1 m = new Client1(b);
        Client2 c = new Client2(b);

        new Thread(m).start();
        new Thread(c).start();


    }
}
