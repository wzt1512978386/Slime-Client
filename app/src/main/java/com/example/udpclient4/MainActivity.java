package com.example.udpclient4;

import android.Manifest;
import android.app.Activity;
import android.app.DirectAction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.udpclient4.Account.AccountActivity;
import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Client.Client;
import com.example.udpclient4.GameRoom.GameRoomActivity;
import com.example.udpclient4.Ranking.RankActivity;
import com.example.udpclient4.Server.ServerActivity;
import com.example.udpclient4.Util.SysU;
import com.example.udpclient4.chatRoom.ChatRoomActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    //暂时控件
    Button bt_connect;
    TextView tv_info;
    EditText et_ip;
    //客户端
    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        permissionRequest();//权限申请
        MyApp.activity=this;
        //preferenceInit();
        //initClient();


        //test();
        /*
        HorizontalScrollView scrollView=(HorizontalScrollView)findViewById(R.id.scrollview_main);
        ArrayList<View> viewList=new ArrayList<>();
        viewList.add(findViewById(R.id.imagebutton_main_gameroom));
        viewList.add(findViewById(R.id.imagebutton_main_ranking));
        viewList.add(findViewById(R.id.imagebutton_main_chatroom));
        viewList.add(findViewById(R.id.imagebutton_main_account));
        viewList.add(findViewById(R.id.imagebutton_main_connect));
        scrollView.addFocusables(viewList,View.FOCUS_BACKWARD);
        scrollView.set
        */


    }
    public void rankClick(View v){
        SysU.IN101("进入排行榜！");
        Intent intent=new Intent(this, RankActivity.class);
        startActivity(intent);
    }
    public void gameRoomClick(View v){
        SysU.IN101("创建游戏室！");
        Intent intent=new Intent(this, GameRoomActivity.class);
        startActivity(intent);
    }
    public void accountClick(View v){
        Intent intent=new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
    public void chatRoomClick(View v){
        SysU.IN101("创建聊天室！");
        Intent intent=new Intent(this, ChatRoomActivity.class);
        startActivity(intent);
    }
    public void serverClick(View v){
        Intent intent=new Intent(this, ServerActivity.class);
        startActivity(intent);
    }

    private void permissionRequest(){
        SysU.requestPermisson(this, Manifest.permission.INTERNET,0);
        SysU.requestPermisson(this, Manifest.permission.ACCESS_NETWORK_STATE,0);
        SysU.requestPermisson(this, Manifest.permission.ACCESS_WIFI_STATE,0);
    }

    /*
    private void test(){
        bt_connect=(Button)findViewById(R.id.button_connect);
        tv_info=(TextView)findViewById(R.id.textview_info);
        et_ip=(EditText)findViewById(R.id.edittext_ip);

        client2 =new Client2();

        bt_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String ip_addr=et_ip.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //Log.i("IN101", "客户端ip地址："+ip_addr);
                        //tv_info.setText(ip_addr);
                        String ip_addr=et_ip.getText().toString();
                        client2.sendMsg(ip_addr);

                    }
                }).start();




            }
        });
    }*/
}