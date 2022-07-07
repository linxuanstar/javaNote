package com.linxuan.demo01;

public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();

        Thread t1 = new Thread(myThread);
        Thread t2 = new Thread(myThread);
        Thread t3 = new Thread(myThread);
        Thread t4 = new Thread(myThread);
        Thread t5 = new Thread(myThread);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
