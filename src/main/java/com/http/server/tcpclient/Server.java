package com.http.server.tcpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    Integer port;
    List<Socket> clients;
    ServerSocket serverSocket;

    public static void main(String[] args) {
        new Server();
    }

    public Server(){
        try{
            port = 8081;
            clients = new ArrayList<>();
            serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();
                clients.add(socket);
                SocketThread socketThread = new SocketThread(socket);
                socketThread.start();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    class SocketThread extends Thread{

        Socket threadSocket;

        private BufferedReader bufferedReader;
        private PrintWriter printWriter;
        public String msg;

        public SocketThread(Socket socket){
            threadSocket = socket;
        }

        public void run(){
            try{
                bufferedReader = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
                msg = "欢迎【" +serverSocket.getInetAddress()+ "】进入聊天室！" +
                        "当前聊天室有【" + clients.size() + "】人。";

                //发送消息
                sendMsg();
                while ((msg = bufferedReader.readLine()) != null){
                    msg = "【" +serverSocket.getInetAddress()+ "】说：" + msg;
                    sendMsg();
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        public void sendMsg(){
            try {
                System.out.println(msg);
                for (int i = clients.size() - 1; i >= 0; i--){
                    printWriter = new PrintWriter(clients.get(i).getOutputStream(), true);
                    printWriter.write(msg);
                    printWriter.flush();
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
