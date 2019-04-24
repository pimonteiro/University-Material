package Guiao2.Stor;

public class Bank {
    private Account[] a;

    public Bank(int n) {
        a = new Account[n];
        for(int i = 0; i < n; i++) a[i] = new Account();
    }

    public int balance(int i){
        return a[i].balance();
    }

    public void deposit(int i, int m){
        a[i].deposit(m);
    }

    public void withdraw(int i, int m){
        a[i].withdraw(m);
    }

    public void transfer(int o, int d, int m){
        a[o].withdraw(m);
        a[d].deposit(m);
    }
}
