package com.http.server.tcpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public Integer port = 8081;
    Socket socket = null;

    public static void main(String[] args) {
        new Client();
    }

    public Client(){
        try {
            socket = new Socket("127.0.0.1", port);
            new ClientThread().start();


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg1;
            while ((msg1 = bufferedReader.readLine()) != null){
                System.out.println(msg1);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    class ClientThread extends Thread{
        public void run(){
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                String msg2;
                while (true){
                    msg2 = bufferedReader.readLine();
                    printWriter.write(msg2);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
