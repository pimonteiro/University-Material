package Guiao2;

public class Main2 {

    public static void main(String[] args) {
        int N = 10000;

        Thread[] thrs = new Thread[N];
        Banco b = new Banco(100);

        for(int i = 0; i < N; i++){
            thrs[i] = new Ex2(b);
            thrs[i].start();
        }

        for(int i = 0; i < N; i++){
            try {
                thrs[i].join();
            }
            catch (InterruptedException e){
                System.out.println(e);
            }
        }

        for(int i = 0; i < N; i++){
            thrs[i] = new Ex3(b);
            thrs[i].start();
        }

        for(int i = 0; i < N; i++){
            try {
                thrs[i].join();
            }
            catch (InterruptedException e){
                System.out.println(e);
            }
        }


        for(int i = 0; i < 100; i++){
            System.out.println(b.consulta(i));
        }
    }
}