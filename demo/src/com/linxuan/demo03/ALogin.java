package com.linxuan.demo03;

public class ALogin extends Thread{
    @Override
    public void run() {
        LoginServlet.doPost("a", "aa");
    }
}
