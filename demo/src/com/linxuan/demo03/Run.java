package com.linxuan.demo03;

public class Run {
    public static void main(String[] args) {
        // new ALogin().start();
        // new BLogin().start();
        ALogin aLogin = new ALogin();
        BLogin bLogin = new BLogin();
        aLogin.start();
        bLogin.start();
    }
}
