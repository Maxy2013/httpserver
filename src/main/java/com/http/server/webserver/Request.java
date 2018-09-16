package com.http.server.webserver;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class Request {

    private InputStream input;

    public Request(InputStream input){
        this.input = input;
    }

    public String getURI() throws Exception{
        String content = null;
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[2048];

        //将内容存入buffer数组中
        int length = input.read(buffer);

        //将buffer数组转换成字符串
        for(int i = 0; i < length; i++){
            stringBuilder.append((char)buffer[i]);
        }
        content = stringBuilder.toString();
        return getFileName(content);
    }

    private String getFileName(String content){

        int a = content.indexOf(' ');
        int b;
        if(-1 != a){
            b = content.indexOf('?', a + 1);
            if(-1 == b){
                b = content.indexOf(' ', a + 1);
                return content.substring(a + 2, b);
            }
        }
        return null;
    }
}
