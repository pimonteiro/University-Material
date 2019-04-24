package Guiao7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientBanco {


    public static void main(String[] args) {
        ArrayList<String> cm = new ArrayList<>();
        cm.add("- descobrir_iD");
        cm.add("- criar conta <saldo>");
        cm.add("- fechar conta <id>");
        cm.add("- consultar saldo <id>");
        cm.add("- levantar dinheiro <id> <valor>");
        cm.add("- depositar dinheiro <id> <valor>");
        cm.add("- transferir dinheiro <id_origem> <id_destino> <valor>");


        try {
            System.out.println("> Connecting to the Bank server......");
            Socket s = new Socket("localhost",1234);
            System.out.println("> Connected!\n");

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());

            Scanner sn = new Scanner(System.in);
            String c_input;
            while(true){

                System.out.println("- descobrir_iD");
                System.out.println("- criar conta <saldo>");
                System.out.println("- fechar conta <id>");
                System.out.println("- consultar saldo <id>");
                System.out.println("- levantar dinheiro <id> <valor>");
                System.out.println("- depositar dinheiro <id> <valor>");
                System.out.println("- transferir dinheiro <id_origem> <id_destino> <valor>");

                c_input = sn.next().toLowerCase();

                if(cm.get(1).contains(c_input)){
                    getID(in, out, s);
                }
                if(cm.get(2).contains(c_input)){
                    createAcc(in, out, s);
                }
                if(cm.get(3).contains(c_input)){
                    closeAcc(in, out, s);
                }
                if(cm.get(4).contains(c_input)){
                    checkAcc(in, out, s);
                }
                if(cm.get(5).contains(c_input)){
                    widthraw(in, out, s);
                }
                if(cm.get(6).contains(c_input)){
                    put(in, out, s);
                }



            }


            s.shutdownInput();
            s.shutdownOutput();
            s.close();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void put(BufferedReader in, PrintWriter out, Socket s) {
        out.write("put_money");
        out.flush();

        try {
            String response = in.readLine();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkAcc(BufferedReader in, PrintWriter out, Socket s) {
        out.write("check_money");
        out.flush();

        try {
            String response = in.readLine();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeAcc(BufferedReader in, PrintWriter out, Socket s) {
        out.write("close_acc");
        out.flush();

        try {
            String response = in.readLine();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAcc(BufferedReader in, PrintWriter out, Socket s) {
        out.write("create_acc " + );
        out.flush();

        try {
            String response = in.readLine();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void widthraw(BufferedReader in, PrintWriter out, Socket s) {
        out.write("get_money");
        out.flush();

        try {
            String response = in.readLine();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void getID(BufferedReader in, PrintWriter out, Socket s) {
        out.write("get_id");
        out.flush();

        try {
            String response = in.readLine();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
