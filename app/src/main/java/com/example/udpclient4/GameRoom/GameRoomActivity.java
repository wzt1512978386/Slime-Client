package com.example.udpclient4.GameRoom;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Client.Client;
import com.example.udpclient4.Config.GameC;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.R;
import com.example.udpclient4.Util.SysU;

/**
 * @author: wzt
 * @date: 2021/5/4
 */
public class GameRoomActivity extends Activity implements SensorEventListener {
    MoveThread moveThread;
    public TextView heart;
    public Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gameroom);
        //myOrientoinListener = new MyOrientoinListener(this);
        //myOrientoinListener.enable();
        MyApp.gameRoomActivity=this;
        this.context=this;
        test();
        moveThread=new MoveThread();
        moveThread.start();
        //MyApp.spirit.manNum+=1;
        init();
        MyApp.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MyApp.client.sendMsg(UserC.ACCOUNT+"进入游戏", Client.TYPE.GAME_ENTER);
            }
        }, 200);


    }

    private MyOrientoinListener myOrientoinListener;

    @Override
    public void onSensorChanged(SensorEvent event) {
        //float sensorX=event.values[0];
        //float sensorY=event.values[1];
        //if(MyApp.spirit!=null)
            //MyApp.spirit.userMove(sensorX,sensorY);
        //MyApp.client.sendMsg(sensorX+" "+sensorY,Client.TYPE.GAME_MOVE);
        if(moveThread.sensorFlag)
            return;
        GameC.SENSOR_X=event.values[0];
        GameC.SENSOR_Y=event.values[1];
        moveThread.sensorFlag=true;

        //MyApp.spirit.move(0);
        //SysU.IN101(event.values[0]+" "+event.values[1]+" "+event.values[2]);
    }
    private void init(){
        heart=(TextView)findViewById(R.id.textview_gameroom_HP);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    class MyOrientoinListener extends OrientationEventListener {
        private int preOrientation=0;
        public MyOrientoinListener(Context context) {
            super(context);
        }

        public MyOrientoinListener(Context context, int rate) {
            super(context, rate);
        }
        @Override
        public void onOrientationChanged(int orientation) {
            if(Math.abs(orientation-preOrientation)>0||true) {
                SysU.IN101("or"+orientation);
                //if(StaticUtil.frameMan!=null)
                 //   StaticUtil.frameMan.setWave(orientation);
                preOrientation=orientation;
            }
        }
    }
    private SensorManager sensorManager;
    private Sensor sensor;

    private void test(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        //sensorManager.registerListener(this, sensor,30000);
        //sensorManager.registerListener(this, sensor,50000);
        //sensorManager.registerListener(this, sensor,40000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        MyApp.client.sendMsg("", Client.TYPE.DELETE_ADDR);
        moveThread.stop();
    }
}
