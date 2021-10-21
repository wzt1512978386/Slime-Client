package com.example.udpclient4.chatRoom.Emo;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.R;
import com.example.udpclient4.chatRoom.ChatAdapter;

import pl.droidsonroids.gif.GifImageButton;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class EmoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //外部调用
    protected View itemView;
    protected EmoAdapter EA;
    //控件

    protected GifImageButton bt_gif;


    public EmoHolder(@NonNull View itemView, EmoAdapter emoAdapter) {
        super(itemView);
        this.itemView=itemView;
        this.EA=emoAdapter;
        //绑定
        bt_gif=(GifImageButton)itemView.findViewById(R.id.gifbutton_item_chatroom_emo);

        //设置事件
        bt_gif.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        if (EA.emoListener!=null) {
            if (v != null &&v.getTag(R.id.tag_pos)!=null)
                EA.emoListener.onEmoClick(v,(Integer) v.getTag(R.id.tag_pos));
        }
    }
}
