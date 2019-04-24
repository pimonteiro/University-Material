package Guiao6;

import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {

    public static void main(String[] args) {
        ServerSocket ss;
        try {
            ss = new ServerSocket(1234);


        while(true){
            ThreadServerConnection thc = new ThreadServerConnection(ss.accept());
            thc.start();
        }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
