package Guiao2_5;

public class Bank {

        private Conta[] accounts;

        public Bank(int n){
            this.accounts = new Conta[n];
            for (int i = 0; i<n; i++){
                accounts[i] = new Conta();
            }
        }

        public int slots(){
            return this.accounts.length;
        }

        public void take(int account1, int amount){
            this.accounts[account1].levantar(amount);
        }

        public void put(int account1, int amount ){
            this.accounts[account1].depositar(amount);
        }

        public int query(int i){
            return this.accounts[i].consultar();
        }

        public void transferir(int account1, int account2, int amount){

            int contamenor = Math.min(account1,account2);
            int contamaior = Math.max(account1,account2);

            accounts[contamaior].lock();
            accounts[contamenor].lock();
            this.take(account1, amount);
            this.put(account2,amount);
            accounts[contamaior].unlock();
            accounts[contamenor].unlock();
        }
}
