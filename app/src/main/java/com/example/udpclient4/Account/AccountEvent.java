package com.example.udpclient4.Account;

import android.view.View;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Client.Client;
import com.example.udpclient4.Config.HeadC;
import com.example.udpclient4.Config.UserC;

/**
 * @author: wzt
 * @date: 2021/5/6
 */
public class AccountEvent {
    private static final String SP=" ___ ";
    private AccountActivity AA;
    public AccountEvent(AccountActivity AA){
        this.AA=AA;
        setEvent();
    }
    private void setEvent(){
        AA.bt_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AA.headPopWin.showAsDropDown(v);
            }
        });

        for(int i=0;i<9;i++){
            int finalI = i;
            AA.heads[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AA.bt_head.setImageResource(HeadC.HEAD_P[finalI]);
                    UserC.HEAD_ID=finalI;
                    MyApp.editor.putInt("HEAD",UserC.HEAD_ID);
                    MyApp.editor.apply();
                    AA.headPopWin.dismiss();
                }
            });
        }

        AA.bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AA.ACCOUNT=AA.et_account.getText().toString().trim();
                AA.PASSWORD=AA.et_password.getText().toString().trim();
                MyApp.client.sendMsg(AA.ACCOUNT+SP+AA.PASSWORD+SP+UserC.HEAD_ID, Client.TYPE.LOGIN_UPDATE);
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
