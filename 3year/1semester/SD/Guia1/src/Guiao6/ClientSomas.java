package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSomas {

    public static void main(String[] args) {
        try {
            System.out.println("> Connecting to server......");
            Socket s = new Socket("localhost", 1234);
            System.out.println("> Connected!!\n");
            System.out.println("(Type exit to leave)\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            PrintWriter out = new PrintWriter(s.getOutputStream());

            Scanner p = new Scanner(System.in);
            String inputS;
            String inputC;
            String response;

            System.out.println("$ ");
            while((inputC = p.next()) != null && !inputC.equals("exit")){
                out.println(inputC);
                out.flush();

                response = in.readLine();
                System.out.println(response);
                System.out.println("\n$ ");
            }

            out.println("exit");
            out.flush();

            //pode ficar mais bonito

            response = in.readLine();
            System.out.print(response);

            s.shutdownOutput();
            s.shutdownInput();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
