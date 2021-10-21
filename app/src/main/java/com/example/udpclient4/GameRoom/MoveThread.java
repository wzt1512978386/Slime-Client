package com.example.udpclient4.GameRoom;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Config.GameC;
import com.example.udpclient4.Util.SysU;

/**
 * @author: wzt
 * @date: 2021/5/5
 */
public class MoveThread {
    private boolean runFlag;
    protected boolean sensorFlag=false;
    public void start(){
        runFlag=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    while (runFlag) {
                        try {
                            wait(25);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //if(!sensorFlag)
                            //SysU.IN101("sensor没更新！！！");
                        if (MyApp.spirit != null&&MyApp.spirit.man!=null) {
                            MyApp.spirit.userMove(GameC.SENSOR_X, GameC.SENSOR_Y);
                            sensorFlag=false;
                        }
                    }
                }
            }
        }).start();
    }
    public void stop(){
        runFlag=false;
    }
}
