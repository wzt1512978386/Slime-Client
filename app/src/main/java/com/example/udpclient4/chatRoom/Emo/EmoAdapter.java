package com.example.udpclient4.chatRoom.Emo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.Config.EmoC;
import com.example.udpclient4.R;


import java.util.List;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class EmoAdapter extends RecyclerView.Adapter<EmoHolder>{
    private Context context;
    //会话列表
    private List<Integer> emoList;
    public EmoAdapter(Context context, List<Integer> list){
        this.context=context;
        this.emoList=list;
    }
    @NonNull
    @Override
    public EmoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView= LayoutInflater.from(context).inflate(R.layout.item_emo,parent,false);
        return new EmoHolder(convertView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull EmoHolder holder, int position) {
        int index=emoList.get(position);
        if(position==6) {
            position=position*1;
        }
        holder.bt_gif.setTag(R.id.tag_pos,position);
        holder.bt_gif.setImageResource(EmoC.EMO_P[index]);
    }
    protected EmoListener emoListener;
    public void setEmoListener(EmoListener emoListener){
        this.emoListener=emoListener;
    }
    @Override
    public int getItemCount() {
        return emoList==null?0:emoList.size();
    }
}
