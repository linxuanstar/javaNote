package com.linxuan.demo02;

public class BServer2 extends AServer implements Runnable{

    public void bServerMethod() {
        System.out.println("b中的保存数据方法被执行了");
    }

    @Override
    public void run() {
        bServerMethod();
    }
}