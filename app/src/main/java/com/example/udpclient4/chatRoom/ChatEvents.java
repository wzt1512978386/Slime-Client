package com.example.udpclient4.chatRoom;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Client.Client;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.Config.WallC;
import com.example.udpclient4.Data.ChatMsgEnity;
import com.example.udpclient4.R;
import com.example.udpclient4.Util.SysU;
import com.example.udpclient4.chatRoom.Emo.EmoListener;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class ChatEvents {
    private ChatRoomActivity CRA;
    public ChatEvents(ChatRoomActivity CRA){
        this.CRA=CRA;
        setEvents();
    }
    private void setEvents(){
        //发送消息
        CRA.bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=CRA.et_input.getText().toString().trim();
                CRA.et_input.setText(" ", TextView.BufferType.EDITABLE);
                /*
                CRA.chatList.add(new ChatEnity(ChatEnity.TYPE.RIGHT,input,"me"));
                CRA.chatAdapter.notifyDataSetChanged();
                CRA.chatRecycler.scrollToPosition(CRA.chatList.size()-1);
                 */
                //CRA.chatRecycler.scrollToPosition(0);
                //SysU.IN101(UserC.ACCOUNT);
                ChatMsgEnity chatMsg=new ChatMsgEnity(UserC.ACCOUNT,input);
                MyApp.client.sendMsg(chatMsg.toEncode(), Client.TYPE.CHAT_MSG);
            }
        });
        /*消息操作*/
        //打开消息弹窗
        CRA.chatAdapter.setChatListener(new ChatListener() {
            @Override
            public void onChatMsgClick(View v, int pos) {
                //SysU.IN101("按住"+pos);
                CRA.chatMsgPopWin.showAsDropDown(v);
                CRA.bt_delete.setTag(R.id.tag_pos,pos);
            }
        });
        //点击撤销
        CRA.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=(Integer)v.getTag(R.id.tag_pos);
                ChatEnity chatEnity=MyApp.chatList.get(pos);
                ChatMsgEnity chatMsgEnity=new ChatMsgEnity(chatEnity.account,chatEnity.msg,chatEnity.date);
                MyApp.client.sendMsg(chatMsgEnity.toEncodeT(), Client.TYPE.DELETE_MSG);
                CRA.chatMsgPopWin.dismiss();

            }
        });
        /*表情操作*/
        //打开表情弹窗
        CRA.bt_emo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRA.emoPopWin.showAtLocation(CRA.findViewById(R.id.recyclerview_chatroom),Gravity.BOTTOM,0,-CRA.emoPopWin.getHeight());
            }
        });
        //点击表情
        CRA.emoAdapter.setEmoListener(new EmoListener() {
            @Override
            public void onEmoClick(View v, int pos) {
                MyApp.client.sendMsg(""+pos, Client.TYPE.CHAT_GIF);
                CRA.emoPopWin.dismiss();
            }
        });

        /*消息查询*/
        CRA.et_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String txt=s.toString().trim();
                if(txt.isEmpty()) {
                    CRA.queryFlag=false;
                    MyApp.chatList.clear();
                    for (ChatEnity chat:MyApp.tmplist)
                        MyApp.chatList.add(chat);

                }
                else {

                    MyApp.chatList.clear();
                    for (ChatEnity chat : MyApp.tmplist) {
                        //SysU.IN101(chat.msg);
                        if ((chat.account + chat.msg).contains(txt)) {
                            //SysU.IN101("找到");
                            MyApp.chatList.add(chat);
                        }
                    }
                }
                MyApp.chatList.sort(new Comparator<ChatEnity>() {
                    @Override
                    public int compare(ChatEnity o1, ChatEnity o2) {
                        if(o1.date.ms>o2.date.ms)
                            return 1;
                        else if(o1.date.ms==o2.date.ms)
                            return 0;
                        else
                            return -1;
                    }
                });
                MyApp.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        CRA.chatAdapter.notifyDataSetChanged();
                    }
                } );


            }
        });
        //打开搜索
        CRA.bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CRA.queryFlag)
                    MyApp.tmplist = new ArrayList<>(MyApp.chatList);
                CRA.queryFlag=true;
                CRA.queryPopWin.showAtLocation(CRA.findViewById(R.id.recyclerview_chatroom),Gravity.TOP,0,CRA.queryPopWin.getHeight()+300);
                //CRA.queryPopWin.showAsDropDown(v);
            }
        });
        //清空搜索
        CRA.bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRA.et_txt.setText("");
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

        //更换壁纸
        CRA.bt_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallC.addIdx();
                MyApp.editor.putInt("WALL_IDX",WallC.WALL_IDX);
                MyApp.editor.apply();
                CRA.walView.setImageResource(WallC.getP());
            }
        });

    }

}
