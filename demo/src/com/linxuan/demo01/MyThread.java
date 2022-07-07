package com.linxuan.demo01;

public class MyThread extends Thread {
    private int i = 5;

    @Override
    public void run() {
        System.out.println("i = " + (i--) 
                            + " threadName = " 
                            + Thread.currentThread().getName());
    }

}
