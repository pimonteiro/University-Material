package Guiao2_5;

import java.util.concurrent.locks.ReentrantLock;

public class Conta {

    private int saldo;
    private ReentrantLock lockConta;

    public Conta(){
        this.saldo = 0;
        this.lockConta = new ReentrantLock();
    }

    public  void depositar(int valor){
        lockConta.lock();
        this.saldo += valor;
        lockConta.unlock();
    }

    public  void levantar(int valor){
        lockConta.lock();
        this.saldo -= valor;
        lockConta.unlock();
    }

    public  int consultar(){
        int valor;
        lockConta.lock();
        valor = this.saldo;
        lockConta.unlock();
        return valor;
    }

    public void lock(){
        lockConta.lock();
    }

    public void unlock(){
        lockConta.unlock();
    }
}
