package com.http.server.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    private static final Integer PORT = 8080;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        }catch (IOException e){
            e.printStackTrace();
        }

        while (true){
            try{
                socket = serverSocket.accept();
                System.out.println("Request: [" + socket + "] connected!");

                OutputStream os = socket.getOutputStream();
                InputStream is = socket.getInputStream();

                LineNumberReader in = new LineNumberReader(new InputStreamReader(is));
                String inputLine = in.readLine();
                String content = null;

                System.out.println(inputLine);
                if(in.getLineNumber() == 1){
                    content = inputLine.substring(inputLine.indexOf('/') + 1, inputLine.lastIndexOf(' '));
                    System.out.println(content);
                }

                String userInf = null;
                if(inputLine.startsWith("Cookie: ")){
                    userInf = inputLine;
                    System.out.println("new User...");
                }
//
//                Request request = new Request(is);
//
//                String fileName = request.getURI();
                Response response = new Response(userInf, os, content);
                response.response();

            }catch (Exception e){
                e.printStackTrace();
            }finally {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }
}
