package com.example.udpclient4.Client;

import com.example.udpclient4.Util.SysU;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * @author: wzt
 * @date: 2021/4/29
 */
public class Server {
    private static boolean runFlag=true;
    private static final int MAX_LENGTH=1024;
    public static void start(){
        runFlag=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SysU.IN101("聊天室接收服务启动");
                while(runFlag){
                    byte[] buffer = new byte[MAX_LENGTH];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    try {
                        Client3.udpSocket.receive(packet);
                        String receStr = new String(packet.getData(), 0 , packet.getLength(),"GBK");
                        SysU.IN101(receStr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public static void stop(){
        runFlag=false;
    }
}
