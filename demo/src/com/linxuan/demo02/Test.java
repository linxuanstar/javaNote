package com.linxuan.demo02;

import com.linxuan.demo01.MyThread;

public class Test {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();

        Thread a = new Thread(myThread, "A");
        Thread b = new Thread(myThread, "B");
        Thread c = new Thread(myThread, "C");
        Thread d = new Thread(myThread, "D");
        Thread e = new Thread(myThread, "E");

        a.start();
        b.start();
        c.start();
        d.start();
        e.start();
                
    }
    
}
