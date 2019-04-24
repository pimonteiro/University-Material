package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 1234);
            System.out.println("> Connecting to server......");
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            PrintWriter out = new PrintWriter(s.getOutputStream());

            Scanner p = new Scanner(System.in);
            String t;
            do {
                t = p.next();
                out.println(t);
                out.flush();
                System.out.println(in.readLine());
            } while(t != null);

            s.shutdownOutput();
            s.shutdownInput();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
