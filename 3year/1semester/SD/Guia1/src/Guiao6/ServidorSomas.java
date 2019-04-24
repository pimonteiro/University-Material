package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSomas {

    public static void main(String[] args) {

        try {
            ServerSocket ss = new ServerSocket(1234);
            while (true) {
                Socket s = ss.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                PrintWriter out = new PrintWriter(s.getOutputStream());

                // Estado do servidor por sessão
                int total = 0;
                int i = 0;
                String t;


                out.println("Type exit to leave.");
                out.flush();

                while((t = in.readLine()) != null && !t.equals("exit")){
                    total += Integer.parseInt(t);
                    i++;
                    out.println("Soma total: " + total);
                    out.flush();
                }

                // fazer a media
                total = total/i;
                out.println("Media total: " + total);
                out.flush();

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
