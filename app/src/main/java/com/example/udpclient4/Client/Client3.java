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
public class Client3 {
    //消息编码
    static enum TYPE{
        TRY,
        INFO,

    }
    private static final String SP="-?*!#";
    //网络参数
    private static String SIP;//服务端IP
    private static String CIP;//客户端IP
    private static int SPORT=58010;//服务端口号
    private static int CPORT=58020;//客户端口号

    protected static DatagramSocket udpSocket;
    private static DatagramPacket udpPacket;
    public static void update(String SIP_,int SPORT_){
        SIP=SIP_;
        SPORT=SPORT_;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CIP=SysU.getIp();

                    //SysU.IN101("客户端  "+CIP+"  "+CPORT);

                    //udpSocket=new DatagramSocket(CPORT,IP2Name(CIP));
                    udpSocket=new DatagramSocket(CPORT);
                    //udpSocket=new DatagramSocket(null);
                    //udpSocket.bind(new InetSocketAddress(CPORT));
                    //udpSocket=new DatagramSocket(CPORT,IP2Name(SIP));
                    //SysU.IN101("客户端 ip地址:"+udpSocket.getLocalAddress().getHostAddress() +"   端口号:"+udpSocket.getLocalPort());
                    //SysU.IN101("port:"+udpSocket.getPort());

                    SysU.IN101("初始化socket成功！");
                } catch (SocketException e) {
                    e.printStackTrace();
                    SysU.IN101("初始化socket时出现SocketException");
                }

            }
        }).start();

    }
    /*客户端发送消息*/
    public static void sendMsg(String MSG){
        //SysU.IN101("点击") ;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] sendBuf = MSG.getBytes("GBK");
                    udpPacket = new DatagramPacket(sendBuf, sendBuf.length,IP2Name(SIP),SPORT);
                    udpSocket.send(udpPacket);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    SysU.IN101("发送packet时出现UnknownHostException");
                } catch (IOException e) {
                    e.printStackTrace();
                    SysU.IN101("发送packet时出现IOException");
                }
            }
        }).start();



    }
    /*转ip格式*/
    private static InetAddress IP2Name(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            SysU.IN101("转ip错误");
            e.printStackTrace();
        }
        return null;
    }
    private static String encode(String msg,TYPE type){
        String MSG="ip:"+SP;
        switch (type){
            case TRY:
                MSG=MSG+"cmd:TRY"+SP;
            case INFO:
                MSG=MSG+"cmd:INFO"+SP;
                //MSG=MSG+"no:0"+SP;
                MSG=MSG+msg;
        }
        return MSG;
    }
}
