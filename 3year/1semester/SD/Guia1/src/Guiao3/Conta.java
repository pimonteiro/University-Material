package Guiao3;

import java.util.concurrent.locks.ReentrantLock;

public class Conta {

    private int saldo;
    private int id;
    private ReentrantLock lockConta;

    public Conta(){
        this.saldo = 0;
        this.lockConta = new ReentrantLock();
    }

    public Conta(int i, int saldoInicial){
        this.saldo = saldoInicial;
        this.id = i;
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
