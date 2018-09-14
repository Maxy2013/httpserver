package com.http.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lz
 * 2018/9/14 15:22
 */
public class HttpSocket {

    private static final Integer PORT = 80;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSocket.class);

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer(){
        ServerSocket serverSocket = null;
        try {
            //创建serverSocket实例
            serverSocket = new ServerSocket(PORT);
            while (PORT > 1){
                readSocketInfo(serverSocket);
            }
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }/*finally {
            try {
                if (null != serverSocket){
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    private static void readSocketInfo(ServerSocket serverSocket) throws Exception{
        Socket socket = serverSocket.accept();
        LOGGER.debug("Request: {} connected!", socket);

        LineNumberReader in = new LineNumberReader(new InputStreamReader(socket.getInputStream()));

        String lineInput;
        String requestPage = null;
        while ((lineInput = in.readLine()) != null){
            LOGGER.debug("line information: {}", lineInput);
            if(in.getLineNumber() == 1){
                requestPage = lineInput.substring(lineInput.indexOf('/') + 1, lineInput.lastIndexOf(' '));
                LOGGER.debug("requestPage:{}", requestPage);
            }else {
                if (lineInput.isEmpty()){
                    LOGGER.info("header finished!");
                    doResponseGet(requestPage, socket);
                }
            }
        }

    }

    private static void doResponseGet(String requestPage, Socket socket) throws Exception {
        final String WEB_ROOT = "F:";
        File file = new File(WEB_ROOT, requestPage);

        OutputStream out = socket.getOutputStream();
        if (file.exists()){
            InputStream fileIn = new FileInputStream(file);
            byte[] buf = new byte[fileIn.available()];

            fileIn.read(buf);
            fileIn.close();
            out.write(buf);
            out.flush();
            socket.close();
            LOGGER.info("request complete!----->>");
        }else {
            String msg = " 555555.....\r\n";
            String response = "HTTP/1.1 200 OK\r\n";
            response += "Server: Leader Server/0.1\r\n";
            response += "Content Length: " + (msg.length() - 4) + "\r\n";
            response += "\r\n";
            response += msg;
            out.write(response.getBytes());
            out.flush();
        }
            out.close();
    }
}
