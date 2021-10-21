package com.example.udpclient4.chatRoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.example.udpclient4.R;

/**
 * @author: wzt
 * @date: 2021/5/2
 */
public class ChatMsgPopWin extends PopupWindow {
    private View view;
    public ChatMsgPopWin(Context context){
        this.view= LayoutInflater.from(context).inflate(R.layout.popwindow_chatroom, null);
        this.setOutsideTouchable(true);
    }
}
