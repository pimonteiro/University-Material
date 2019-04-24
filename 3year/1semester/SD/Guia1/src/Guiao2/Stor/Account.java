package Guiao2.Stor;

public class Account {
    private int balance;

    public Account(){
        this.balance = 0;
    }

    public void deposit(int m){
        this.balance += m;
    }

    public void withdraw(int m){
        this.balance -= m;
    }

    public int balance(){
        return this.balance;
    }
}
