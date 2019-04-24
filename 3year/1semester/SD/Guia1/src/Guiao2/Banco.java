package Guiao2;

public class Banco {
    private int[] contas;

    public Banco(int N){
        this.contas = new int[N];
    }

    public int n_slots(){
        return this.contas.length;
    }

    public int consulta(int i){
        return this.contas[i];
    }

    public void credito(int i, int quantia){
        this.contas[i] += quantia;
    }

    public void debito(int i, int quantia){
        this.contas[i] -= quantia;
    }

    public void transferir(int o, int d, int quantia){
        this.contas[d] -= quantia;
        this.contas[o] += quantia;
    }


}
