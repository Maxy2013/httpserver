package com.http.server.webserver;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Response {

    private OutputStream output;
    private String fileName;
    private String userInf;

    private static final Integer BUFFER_SIZE = 1024;

    public Response(OutputStream output, String fileName) {
        this.output = output;
        this.fileName = fileName;
    }

    public Response(String userInf, OutputStream output, String fileName) {
        this.output = output;
        this.fileName = fileName;
        this.userInf = userInf;
    }

    public void response() throws Exception{
        String path = "F:";
        byte[] buffer = new byte[BUFFER_SIZE];

        String content = null;
        int ch;

        FileInputStream fis = null;
        if (null != path && null != fileName){
            File file = new File(path, fileName);

            if(file.exists()){
               fis = new FileInputStream(file);
               content = "HTTP/1.1 200 OK \r\n";
               content += "Content-Type: text/html\r\n";
               content += "Server: lz Server/0.1\r\n";
               output.write(content.getBytes());
               ch = fis.read(buffer);
               while (-1 != ch){
                   output.write(buffer, 0, ch);
                   ch = fis.read(buffer, 0, BUFFER_SIZE);
               }
            }else {
                String msg = "<h1>404 File Not Found</h1>" +
                        "<p style='color:red;'>" +((null == userInf) ? "new user..." : userInf)+"</p>";
                content = "HTTP/1.1 200 File Not Found \r\n";
                content += "Content-Type: text/html\r\n";
                content += "Server: Leader Server/0.1\r\n";
                if(null == userInf){
                    content += genCookieHeader();
                }
                content += "Content-Length: " + (msg.length() -4) + "\r\n";
                content += "\r\n";
                content += msg;
                output.write(content.getBytes());
            }
        }
        output.close();
    }

    private String genCookieHeader(){
        String header = "Set-Cookie: jsessionid=" + System.currentTimeMillis() + ".lz; domain=localhost\r\n";
        header += "Set-Cookie: autologin=true; domain=localhost\n";
        return header;
    }
}
