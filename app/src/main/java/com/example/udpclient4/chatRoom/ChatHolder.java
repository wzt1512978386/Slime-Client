package com.example.udpclient4.chatRoom;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class ChatHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    //外部调用
    protected View itemView;
    protected ChatAdapter CA;
    //控件
    protected TextView msgLeft, msgRight,tips,nameLeft,nameRight;
    protected GifImageView gifLeft,gifRight;
    protected ImageButton headLeft,headRight;
    protected LinearLayout layoutLeft,layoutRight,layoutTips;
    //protected FrameLayout infoLeft,infoRight;

    public ChatHolder(@NonNull View itemView,ChatAdapter chatAdapter) {
        super(itemView);
        this.itemView=itemView;
        this.CA=chatAdapter;
        //绑定
        msgLeft =(TextView)itemView.findViewById(R.id.textview_item_chatroom_left);
        msgRight =(TextView)itemView.findViewById(R.id.textview_item_chatroom_right);
        nameLeft=(TextView)itemView.findViewById(R.id.textview_item_charroom_left_name);
        nameRight=(TextView)itemView.findViewById(R.id.textview_item_charroom_right_name);
        gifLeft=(GifImageView)itemView.findViewById(R.id.gif_item_chatroom_left);
        gifRight=(GifImageView)itemView.findViewById(R.id.gif_item_chatroom_right);

        tips=(TextView)itemView.findViewById(R.id.textview_item_chatroom_tips);
        headLeft=(ImageButton) itemView.findViewById(R.id.imagebutton_item_chatroom_left);
        headRight=(ImageButton) itemView.findViewById(R.id.imagebutton_item_chatroom_right);

        layoutLeft=(LinearLayout)itemView.findViewById(R.id.linearlayout_item_chatroom_left);
        layoutRight=(LinearLayout)itemView.findViewById(R.id.linearlayout_item_chatroom_right);
        //infoLeft=(FrameLayout)itemView.findViewById(R.id.framelayout_item_chatroom_left);
        //infoRight=(FrameLayout)itemView.findViewById(R.id.framelayout_item_chatroom_right);

        layoutTips=(LinearLayout)itemView.findViewById(R.id.linearlayout_item_chatroom_tips);
        //设置事件
        msgRight.setOnLongClickListener(this);
        gifRight.setOnLongClickListener(this);
    }


    @Override
    public boolean onLongClick(View v) {
        if (CA.chatListener!=null) {
            if (v != null &&v.getTag(R.id.tag_pos)!=null)
                CA.chatListener.onChatMsgClick(v,(Integer) v.getTag(R.id.tag_pos));
        }
        return false;
    }
}
