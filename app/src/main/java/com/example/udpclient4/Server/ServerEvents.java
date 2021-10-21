package com.example.udpclient4.Server;

import android.view.View;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Client.Client;
import com.example.udpclient4.Util.SysU;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class ServerEvents {
    private ServerActivity SA;
    public ServerEvents(ServerActivity SA){
        this.SA=SA;
        setEvents();
    }
    private void setEvents(){
        SA.bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SIP="";
                String CIP=SysU.getIp();
                for(int i=0;i<4;i++){
                    if(i!=0)
                        SIP=SIP+".";
                    SIP=SIP+SA.et_ip[i].getText().toString().trim();
                }
                //v.setRotation(90);
                int SPORT=Integer.parseInt(SA.et_port.getText().toString().trim());
                //String ACCOUNT=SA.et_account.getText().toString().trim();

                //String ACCOUNT= UserC.ACCOUNT;
                //UserC.ACCOUNT=ACCOUNT;

                MyApp.editor.putString("SIP",SIP);
                MyApp.editor.putInt("SPORT",SPORT);
                //MyApp.editor.putString("ACCOUNT",ACCOUNT);
                MyApp.editor.apply();

                SysU.IN101("服务器的IP地址和端口号已更新，为  SIP:"+SIP+"  SPORT:"+SPORT);
                //SA.loc_ip.setText("本地ip:"+CIP);
                MyApp.client.updateSPar();
                MyApp.client.sendMsg("", Client.TYPE.TRY);
                MyApp.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!MyApp.connectFlag) {
                            //Toast.makeText(MyApp.loginActivity.context, "服务器还未连接", Toast.LENGTH_SHORT).show();
                            SA.info.setText("该服务器不可连接\n请检测网络或重新输入地址");
                        }
                    }
                }, 2000);
                //Client3.update(SIP,SPORT);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this){
                            for(int i=0;i<90;i++){
                                try {
                                    wait(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                v.setRotation(i*4);
                            }
                        }
                    }
                }).start();
            }
        });
    }
}
