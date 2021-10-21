package com.example.udpclient4.Client;

import android.content.Intent;
import android.widget.Toast;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Config.GameC;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.GameRoom.Over.OverActivity;
import com.example.udpclient4.MainActivity;
import com.example.udpclient4.R;
import com.example.udpclient4.Ranking.RecordEnity;
import com.example.udpclient4.Util.SysU;
import com.example.udpclient4.chatRoom.ChatEnity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author: wzt
 * @date: 2021/4/29
 */
public class Client {
    //消息编码
    public static enum TYPE{
        TRY,
        INFO,
        CHAT_MSG,
        CHAT_GIF,
        CHAT_REVOKE,
        CHAT_NONE,
        NONE,
        DELETE_MSG,
        DELETE_GIF,
        DELETE_ADDR,
        GAME_ENTER,
        GAME_INFO,
        GAME_MOVE,
        GAME_RANKING,
        LOGIN_IN,
        LOGIN_REGISTER,
        LOGIN_UPDATE
    }
    private static final String SP=" ___ ";
    //网络参数
    private String SIP,CIP;
    private int SPORT,CPORT;
    //报文参数
    private final int MAX_LEN=1024*4;
    //public byte[]BUFFER;
    //public int BUFFER_LEN;
    //
    protected  DatagramSocket UdpSocket;//
    private  DatagramPacket UdpPacket;
    //客户端

    //服务端
    private boolean runFlag;

    public Client(){
        //UserC.ACCOUNT=MyApp.preferences.getString("ACCOUNT","123123");
        updateSPar();
        updateCPar();
        socketInit();
    }
    /*更新服务端网络参数*/
    public void updateSPar(){
        SIP=MyApp.preferences.getString("SIP","120.78.216.21");
        SPORT=MyApp.preferences.getInt("SPORT",58010);
    }
    /*更新客户端网络参数*/
    public void updateCPar(){
        CIP= SysU.getIp();
        CPORT=50000+(int)(10000*Math.random());
        //CPORT=58031;
        SysU.IN101("客户端 CIP:"+CIP+"  CPORT:"+CPORT);
    }
    private void socketInit(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UdpSocket=new DatagramSocket(CPORT);
                    SysU.IN101("客户端初始化socket成功！");
                    startServer();
                } catch (SocketException e) {
                    SysU.IN101("客户端初始化socket发生SocketException");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /*开始接收服务*/
    public  void startServer(){
        runFlag=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SysU.IN101("聊天室接收服务启动");
                while(runFlag){

                    byte[] buffer = new byte[MAX_LEN];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    try {
                        UdpSocket.receive(packet);

                        //BUFFER=packet.getData();
                        //BUFFER_LEN=packet.getLength();

                        String receStr = new String(packet.getData(), 0 , packet.getLength(),"GBK");
                        //SysU.IN101(receStr.length()+"len");
                        String []Strs=receStr.split(SP);
                        String CMD=Strs[0];
                        //String []Strs,tmps;
                        //Strs=receStr.split(SP);

                        //提取报文类型
                        //tmps=Strs[0].split(":");
                        //if(tmps[0].equals("cmd"))
                            //CMD=tmps[1];
                        switch(CMD) {
                            case "TRY":
                                SysU.IN101(receStr);
                                if(Strs[1].equals("TRYBACK")) {
                                    MyApp.handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(MyApp.serverActivity!=null) {
                                                MyApp.serverActivity.info.setText("该服务器可连接\n1秒后自动返回");
                                                MyApp.handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        MyApp.serverActivity.finish();
                                                    }
                                                }, 1000);
                                            }
                                            else{
                                                Toast.makeText(MyApp.loginActivity.context,"服务器已连接好",Toast.LENGTH_SHORT).show();
                                            }
                                            MyApp.connectFlag=true;
                                        }
                                    });
                                    //SysU.IN101("可连接");
                                }
                                break;
                            case "CHAT_MSG":
                                //SysU.IN101(receStr);
                                String account=Strs[1];
                                String msg=Strs[2];
                                String date=Strs[3];
                                int head=Integer.parseInt(Strs[4]);
                                if(MyApp.chatRoomActivity!=null)
                                    MyApp.chatRoomActivity.addChat(account,msg,date,head,TYPE.CHAT_MSG);
                                break;
                            case "CHAT_GIF":
                                if(MyApp.chatRoomActivity!=null)
                                    MyApp.chatRoomActivity.addChat(Strs[1],Strs[2],Strs[3],Integer.parseInt(Strs[4]),TYPE.CHAT_GIF);
                                break;
                            case "CHAT_REVOKE":
                                SysU.IN101(receStr);
                                if(MyApp.chatRoomActivity!=null)
                                    MyApp.chatRoomActivity.addChat(Strs[1],Strs[2],Strs[3],Integer.parseInt(Strs[4]),TYPE.CHAT_REVOKE);
                                break;
                            case "DELETE_MSG":
                                SysU.IN101(receStr);
                                if(MyApp.chatRoomActivity!=null)
                                    MyApp.chatRoomActivity.deleteChat(Strs[1],Strs[2],Strs[3]);
                                break;
                            case "GAME_INFO":
                               //SysU.IN101(receStr);
                                if(MyApp.spirit!=null) {
                                    if(GameC.RANINT!=Integer.parseInt(Strs[2])) {
                                        GameC.RANINT=Integer.parseInt(Strs[2]);
                                        MyApp.spirit.toDecode(Strs[3]);
                                        MyApp.spirit.updatePos();
                                    }
                                }
                                break;
                            case "GAME_DEATH":
                                SysU.IN101(receStr);
                                UserC.SCORE=Integer.parseInt(Strs[2]);
                                MyApp.handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyApp.gameRoomActivity.heart.setBackgroundResource(R.drawable.p_gameroom_heart_1);
                                        MyApp.gameRoomActivity.heart.setText("×");
                                        Intent intent=new Intent(MyApp.gameRoomActivity.context, OverActivity.class);
                                        MyApp.gameRoomActivity.startActivity(intent);
                                    }
                                });
                                sendMsg("",TYPE.GAME_RANKING);
                                break;
                            case "GAME_RANKING":
                                SysU.IN101(receStr);
                                String []tmp=Strs[2].split(" ");
                                MyApp.rankList.clear();
                                if(tmp.length%3==0) {
                                    for (int i = 0; i < tmp.length/3;i++){
                                        RecordEnity record=new RecordEnity(Integer.parseInt(tmp[i*3]),tmp[i*3+1],Integer.parseInt(tmp[i*3+2]),true);
                                        MyApp.rankList.add(record);
                                    }
                                }
                                else{
                                    SysU.IN101("rankData 数量不为三倍数!");
                                }
                                break;
                            case "LOGIN_IN":
                                SysU.IN101(receStr);
                                MyApp.handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        switch(Strs[1]){
                                            case "CORRECT":
                                                Toast.makeText(MyApp.loginActivity.context,"登录成功",Toast.LENGTH_SHORT).show();
                                                UserC.HEAD_ID=Integer.parseInt(Strs[2]);
                                                MyApp.handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent=new Intent(MyApp.loginActivity, MainActivity.class);
                                                        MyApp.loginActivity.startActivity(intent);
                                                    }
                                                }, 500);
                                                break;
                                            case "WRONG":
                                                Toast.makeText(MyApp.loginActivity.context,"密码错误哦~",Toast.LENGTH_SHORT).show();
                                                break;
                                            case "NOT_FOUND":
                                                Toast.makeText(MyApp.loginActivity.context,"用户未注册哦~",Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                });
                                break;
                            case "LOGIN_REGISTER":
                                SysU.IN101(receStr);
                                MyApp.handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        switch (Strs[1]) {
                                            case "REGISTER_OK":
                                                Toast.makeText(MyApp.loginActivity.context, "用户注册好咯！", Toast.LENGTH_SHORT).show();
                                                MyApp.handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(MyApp.loginActivity, MainActivity.class);
                                                        MyApp.loginActivity.startActivity(intent);
                                                    }
                                                }, 500);
                                                break;
                                            case "HAD_BEEN_REGISTERED":
                                                Toast.makeText(MyApp.loginActivity.context, "该用户名已被注册哦~", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                });
                                break;
                            case "LOGIN_UPDATE":
                                MyApp.handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        switch (Strs[1]) {
                                            case "UPDATE_FAIL":
                                                Toast.makeText(MyApp.accountActivity.context, "该用户名被别人注册了哦~", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "UPDATE_SUCCESS":
                                                if(UserC.ACCOUNT.equals(Strs[2])) {
                                                    Toast.makeText(MyApp.accountActivity.context, "用户信息更新成功！", Toast.LENGTH_SHORT).show();
                                                    UserC.ACCOUNT = MyApp.accountActivity.ACCOUNT;
                                                    UserC.PASSWORD = MyApp.accountActivity.PASSWORD;
                                                    MyApp.editor.putString("ACCOUNT", UserC.ACCOUNT);
                                                    MyApp.editor.putString("PASSWORD", UserC.PASSWORD);
                                                    MyApp.editor.apply();
                                                }else{
                                                    for(ChatEnity chat:MyApp.chatList){
                                                        if(chat.account.equals(Strs[2])){
                                                            chat.account=Strs[3];
                                                            chat.head=Integer.parseInt(Strs[4]);
                                                        }
                                                    }
                                                    MyApp.chatRoomActivity.chatAdapter.notifyDataSetChanged();
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                });
                                break;
                        }

                        //SysU.IN101(receStr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e){
                        SysU.IN101("报文空缺");
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                        SysU.IN101("数组越界");
                    }
                }
            }
        }).start();
    }
    /*停止接收服务*/
    public void stopServer(){
        runFlag=false;
    }

    /*客户端发送消息*/
    public  void sendMsg(String MSG,TYPE type){
        //SysU.IN101("点击") ;
        String encodeMSG = encodeHead(MSG,type);//加报文头
        //SysU.IN101(encodeMSG);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    byte[] sendBuf = encodeMSG.getBytes("GBK");//将字符串转Byte型
                    UdpPacket = new DatagramPacket(sendBuf, sendBuf.length,IP2Name(SIP),SPORT);//新建packet
                    UdpSocket.send(UdpPacket);//发送报文
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    SysU.IN101("发送packet时出现UnknownHostException");
                } catch (IOException e) {
                    e.printStackTrace();
                    SysU.IN101("发送packet时出现IOException");
                }catch(NullPointerException e){
                    e.printStackTrace();
                    SysU.IN101("空指针");
                }
            }
        }).start();



    }
    /*转ip格式*/
    private  InetAddress IP2Name(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            SysU.IN101("转ip错误");
            e.printStackTrace();
        }
        return null;
    }
    /*报文编码*/
    private  String encodeHead(String msg, TYPE type){
        //String MSG=UserC.ACCOUNT+SP;
        //MSG=MSG+"port:"+CPORT+SP;
        //String MSG="";
        switch (type){
            case TRY:
                return "TRY";
                //MSG=MSG+"TRY"+SP;
                //break;
            case INFO:
                return "INFO"+SP+UserC.ACCOUNT;
            case CHAT_MSG:
                return "CHAT_MSG"+SP+UserC.ACCOUNT+SP+msg;
                //MSG=MSG+"INFO"+SP+msg;
                //MSG=MSG+"no:0"+SP;
                //break;
            case CHAT_GIF:
                return "CHAT_GIF"+SP+UserC.ACCOUNT+SP+msg;
            case CHAT_NONE:
                return "CHAT_NONE"+SP+UserC.ACCOUNT+SP;
            case NONE:
                return "NONE"+SP+UserC.ACCOUNT;
            case DELETE_MSG:
                return "DELETE_MSG"+SP+UserC.ACCOUNT+SP+msg;
            case DELETE_ADDR:
                return "DELETE_ADDR"+SP+UserC.ACCOUNT;
            case GAME_ENTER:
                return "GAME_ENTER"+SP+UserC.ACCOUNT+SP+msg;
            case GAME_MOVE:
                return "GAME_MOVE"+SP+UserC.ACCOUNT+SP+msg;
            case GAME_RANKING:
                return "GAME_RANKING"+SP+UserC.ACCOUNT+SP+msg;
            case LOGIN_IN:
                return "LOGIN_IN"+SP+UserC.ACCOUNT+SP+msg;
            case LOGIN_REGISTER:
                return "LOGIN_REGISTER"+SP+UserC.ACCOUNT+SP+msg;
            case LOGIN_UPDATE:
                return "LOGIN_UPDATE"+SP+ UserC.ACCOUNT+SP+msg;
            default:
                SysU.IN101("头部编码错误");
                return "no head";
        }
        //return MSG;
    }
}
