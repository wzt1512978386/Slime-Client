package com.example.udpclient4.App;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;

import com.example.udpclient4.Account.AccountActivity;
import com.example.udpclient4.Client.Client;
import com.example.udpclient4.GameRoom.GameLayout;
import com.example.udpclient4.GameRoom.GameRoomActivity;
import com.example.udpclient4.GameRoom.Over.OverActivity;
import com.example.udpclient4.GameRoom.Spirit;
import com.example.udpclient4.Login.LoginActivity;
import com.example.udpclient4.Ranking.RankActivity;
import com.example.udpclient4.Ranking.RecordEnity;
import com.example.udpclient4.Server.ServerActivity;
import com.example.udpclient4.chatRoom.ChatEnity;
import com.example.udpclient4.chatRoom.ChatRoomActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class MyApp extends Application {
    private static MyApp mInstance;
    //全局调用
    public static final Handler handler = new Handler();
    public static Activity activity;
    public static Client client;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    public static boolean connectFlag=false;
    //活动
    public static ServerActivity serverActivity=null;
    public static ChatRoomActivity chatRoomActivity=null;
    public static LoginActivity loginActivity;
    public static AccountActivity accountActivity;
    public static GameRoomActivity gameRoomActivity;
    public static RankActivity rankActivity;
    public static OverActivity overActivity;
    //聊天室
    public static List<ChatEnity> chatList=new ArrayList<>();
    public static List<ChatEnity> tmplist;
    //游戏室
    public static GameLayout gameLayout;
    public static Spirit spirit;
    //排行榜
    public static List<RecordEnity> rankList=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;

    }

}
