package com.linxuan.demo03;

public class BLogin extends Thread {
    
    @Override
    public void run() {
        LoginServlet.doPost("b", "bb");
    }
}
