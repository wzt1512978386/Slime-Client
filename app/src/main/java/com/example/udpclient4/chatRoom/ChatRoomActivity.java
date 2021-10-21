package com.example.udpclient4.chatRoom;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Client.Client;
import com.example.udpclient4.Client.Server;
import com.example.udpclient4.Config.EmoC;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.Config.WallC;
import com.example.udpclient4.Data.MyDate;
import com.example.udpclient4.R;
import com.example.udpclient4.Util.SysU;
import com.example.udpclient4.chatRoom.Emo.EmoAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import pl.droidsonroids.gif.GifImageButton;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class ChatRoomActivity extends Activity {
    //发送控件
    protected EditText et_input;
    protected Button bt_send;
    //会话列表有关
    //protected List<ChatEnity> chatList;
    public ChatAdapter chatAdapter;
    protected RecyclerView chatRecycler;
    //消息操作弹窗
    protected PopupWindow chatMsgPopWin;
    protected Button bt_delete;
    //消息查询弹窗
    protected boolean queryFlag=false;
    protected PopupWindow queryPopWin;
    protected ImageButton bt_setting,bt_query;
    protected EditText et_txt;
    //壁纸切换
    protected ImageButton bt_wall;
    protected ImageView walView;
    //表情操作弹窗
    protected PopupWindow emoPopWin;
    protected RecyclerView emoListView;
    protected List<Integer> emoList;
    protected EmoAdapter emoAdapter;
    protected ImageView bt_emo;
    //protected ChatMsgPopWin chatMsgPopWin;
    //外部调用
    protected View view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chatroom);
        //view=getCon
        MyApp.chatRoomActivity=this;

        UserC.HEAD_ID=MyApp.preferences.getInt("HEAD", 0);
        view=getWindow().getDecorView();
        sendInit();
        chatRoomInit();
        popupWindowInit();
        new ChatEvents(this);   //设置聊天室事件
        MyApp.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MyApp.client.sendMsg("213123", Client.TYPE.NONE);
                chatAdapter.notifyDataSetChanged();
            }
        }, 300);

    }

    @Override
    protected void onResume() {
        super.onResume();
        WallC.WALL_IDX=MyApp.preferences.getInt("WALL_IDX", 0);
        walView.setImageResource(WallC.getP());
        startNone();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noneFlag=false;
    }

    private boolean noneFlag;
    private void startNone(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    noneFlag = true;
                    while (noneFlag) {
                        try {
                            wait(500);
                            MyApp.client.sendMsg("", Client.TYPE.CHAT_NONE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
    private void popupWindowInit(){
        //消息操作
        View chatMsgView= LayoutInflater.from(this).inflate(R.layout.popwindow_chatroom,null);
        bt_delete=chatMsgView.findViewById(R.id.button_popwin_delete);

        //chatMsgPopWin=new PopupWindow(linearLayout);
        //TextView textView = new TextView(this);
        //textView.setText("测试文本");
        chatMsgPopWin = new PopupWindow(chatMsgView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);//参数为1.View 2.宽度 3.高度
        chatMsgPopWin.setOutsideTouchable(true);//设置点击外部区域可以取消popupWindow
        //popupWindow

        //消息查询
        bt_setting=(ImageButton)findViewById(R.id.imagebutton_chatroom_setting);
        View queryView=LayoutInflater.from(this).inflate(R.layout.popwindow_query,null);
        bt_query=queryView.findViewById(R.id.imagebutton_chatroom_query);
        et_txt=queryView.findViewById(R.id.edittext_chatroom_txt);
        queryPopWin=new PopupWindow(queryView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);//参数为1.View 2.宽度 3.高度
        queryPopWin.setOutsideTouchable(true);
        //queryPopWin.setWidth(view.getWidth());

        //情绪操作
        bt_emo=findViewById(R.id.imagebutton_chatroom_emo);
        View emoView=LayoutInflater.from(this).inflate(R.layout.popwindow_emotion,null);
        emoListView=(RecyclerView)emoView.findViewById(R.id.recyclerview_popwin_emotion);
        emoList=new ArrayList<>();
        for(int i=0;i<EmoC.EMO_NUM;i++)
            emoList.add(i);
        //SysU.IN101(EmoC.EMO_NUM+"个");
        emoAdapter=new EmoAdapter(this,emoList);
        emoListView.setAdapter(emoAdapter);
        RecyclerView.LayoutManager  layoutManager= new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        emoListView.setLayoutManager(layoutManager);
        emoPopWin=new PopupWindow(emoView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        emoPopWin.setOutsideTouchable(true);

        //壁纸切换
        bt_wall=(ImageButton)findViewById(R.id.imagebutton_chatroom_wall);
        walView=(ImageView)findViewById(R.id.imageview_chatroom_bg);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Server.stop();
    }


    private void sendInit(){
        et_input=(EditText)findViewById(R.id.edittext_chatroom_input);
        bt_send=(Button)findViewById(R.id.button_chatroom_send);

    }
    private void chatRoomInit(){
      //chatList=MyApp.chatList;
      //chatList.add(new ChatEnity(ChatEnity.TYPE.RIGHT,"你好呀","me"));
      //chatList.add(new ChatEnity(ChatEnity.TYPE.LEFT,"你码呀","TT"));
      chatAdapter=new ChatAdapter(this,MyApp.chatList);
      LinearLayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
      chatRecycler=findViewById(R.id.recyclerview_chatroom);
      chatRecycler.setAdapter(chatAdapter);
      chatRecycler.setLayoutManager(layoutManager);
    }
    public void addChat(String account,String msg,String date,int head,Client.TYPE type) {
        MyApp.handler.post(new Runnable() {
            @Override
            public void run() {
                ChatEnity chatEnity = null;
                if(type== Client.TYPE.CHAT_MSG) {
                    if (account.equals(UserC.ACCOUNT))
                        chatEnity = new ChatEnity(ChatEnity.TYPE.RIGHT_MSG, msg, account, date,head);
                    else
                        chatEnity = new ChatEnity(ChatEnity.TYPE.LEFT_MSG, msg, account, date,head);
                }
                else if(type== Client.TYPE.CHAT_GIF){
                    if (account.equals(UserC.ACCOUNT))
                        chatEnity = new ChatEnity(ChatEnity.TYPE.RIGHT_GIF, msg, account, date,head);
                    else
                        chatEnity = new ChatEnity(ChatEnity.TYPE.LEFT_GIF, msg, account, date,head);
                }
                else if(type== Client.TYPE.CHAT_REVOKE){

                    chatEnity = new ChatEnity(ChatEnity.TYPE.REVOKE, msg, account, date,head);

                }
                else{
                    SysU.IN101("addChat 出现重大错误");
                }
                MyApp.chatList.add(chatEnity);

                MyApp.chatList.sort((o1, o2) -> {
                    if(o1.date.ms>o2.date.ms)
                        return 1;
                    else if(o1.date.ms==o2.date.ms)
                        return 0;
                    else
                        return -1;
                });
                MyApp.tmplist=MyApp.chatList;
                chatAdapter.notifyDataSetChanged();
                chatRecycler.scrollToPosition(MyApp.chatList.size() - 1);
            }


        });
    }
    public void deleteChat(String account,String msg,String date){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<MyApp.chatList.size();i++){
                    ChatEnity chat=MyApp.chatList.get(i);
                    if(chat.account.equals(account)&&(chat.msg!=null&&chat.msg.equals(msg))&&chat.date.equals(new MyDate(date))){
                        SysU.IN101("找到"+chat.type);
                        //MyApp.chatList.remove(i);
                        MyApp.chatList.get(i).type=ChatEnity.TYPE.REVOKE;
                        MyApp.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chatAdapter.notifyDataSetChanged();
                            }
                        });

                        //chatRecycler.scrollToPosition(MyApp.chatList.size() - 1);
                        break;
                    }
                }
            }


        }).start();
    }
    public void leaveClick(View v){
        finish();
    }
    public void settingClick(View v){

    }

    @Override
    public void finish() {
        super.finish();
        MyApp.client.sendMsg("", Client.TYPE.DELETE_ADDR);
        MyApp.chatList.clear();
    }
}
