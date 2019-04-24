package Guiao3;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {

        private HashMap<Integer,Conta> accounts;
        private int n;
        private ReentrantLock lockBank;

        public Bank(){
            this.accounts = new HashMap<>();
            this.n = 0;
            this.lockBank = new ReentrantLock();
        }

        public int criarConta(int saldoInicial){
            this.lockBank.lock();
            int i = n;                      //Tem de ser obtido depois do lock para prevenir que duas threads peguem ao mesmo tempo o valor
            this.accounts.put(i,new Conta(i, saldoInicial));
            this.lockBank.unlock();
            return i++;
        }

        public int fecharConta(int id) throws ContaInvalida {
            int valor;
            this.lockBank.lock();

            if(accounts.containsKey(id)){
                Conta c = this.accounts.get(id);
                c.lock();
                valor = c.consultar();
                this.accounts.remove(id);
                n--;
                c.unlock();
                this.lockBank.unlock();
                return valor;
            } else {
                this.lockBank.unlock();
                throw new ContaInvalida("Conta invalida");
            }
        }


        public int slots(){
            return this.accounts.size();
        }

        public void take(int account1, int amount){
            this.lockBank.lock();
            Conta c = this.accounts.get(account1);
            this.lockBank.unlock();
            c.levantar(amount);
        }

        public void put(int account1, int amount ){
            this.lockBank.lock();
            Conta c = this.accounts.get(account1);
            this.lockBank.unlock();
            c.depositar(amount);
        }

        public int query(int i){
            this.lockBank.lock();
            Conta c = this.accounts.get(i);
            this.lockBank.unlock();
            int v =  this.accounts.get(i).consultar();
            return v;
        }

        public void transferir(int account1, int account2, int amount){
            int contamenor = Math.min(account1,account2);
            int contamaior = Math.max(account1,account2);

            accounts.get(contamaior).lock();
            accounts.get(contamenor).lock();
            this.take(account1, amount);
            this.put(account2,amount);
            accounts.get(contamaior).unlock();
            accounts.get(contamenor).unlock();
        }
}
