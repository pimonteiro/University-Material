package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {

        try {
            ServerSocket ss = new ServerSocket(1234);
            while (true) {
                Socket s = ss.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                PrintWriter out = new PrintWriter(s.getOutputStream());

                String t;
                do {
                    t = in.readLine();
                    out.println(t);
                    out.flush();
                } while(t != null);

                s.shutdownInput();
                s.shutdownOutput();
                s.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
