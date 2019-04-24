package Guiao3;

public class Main {
    public static void main(String[] args){
        Bank b = new Bank();

        Client1 m = new Client1(b);
        Client2 c = new Client2(b);

        new Thread(m).start();
        new Thread(c).start();


    }
}
