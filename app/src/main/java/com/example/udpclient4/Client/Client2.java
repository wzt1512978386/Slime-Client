package com.example.udpclient4.Client;

import com.example.udpclient4.Util.SysU;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class Client2 {
    //private String SIP="192.168.50.1";
    private String SIP="";
    private String CIP="";
    private int PORT = 8010;
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    public Client2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //InetAddress address = InetAddress.getByName("127.0.0.1");
                    //datagramSocket = new DatagramSocket(PORT,address);
                    //String local_Ip="192.168.137.152";

                    String  local_ip= SysU.getIp();
                    SysU.IN101("本地ip"+local_ip);
                    //InetAddress address = InetAddress.getByName(local_ip);
                    datagramSocket=new DatagramSocket(PORT+2);
                    //datagramSocket=new DatagramSocket(PORT+2,address);
                    SysU.IN101("datagramSocket创建成功！");
                } catch (SocketException e) {
                    SysU.IN101("datagramSocket创建失败！");
                    e.printStackTrace();
                }/* catch (UnknownHostException e) {
                    Util.IN101("datagramSocket创建失败！");
                    e.printStackTrace();
                }
                */
            }
        }).start();
    }
    public void sendMsg(String msg){
        SysU.IN101("点击") ;
        //Util.IN101("是否可连"+Util.isHostConnectable("192.168.50.1",8010));
        byte[] sendBuf = msg.getBytes();
        try {
            InetAddress address = InetAddress.getByName(SIP);
            datagramPacket = new DatagramPacket(sendBuf, sendBuf.length,address,PORT);
            SysU.IN101("packet:"+datagramPacket.getSocketAddress().toString());
            SysU.IN101("scoket:"+datagramSocket.getLocalAddress().toString());
            datagramSocket.send(datagramPacket);
            SysU.IN101(msg);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
