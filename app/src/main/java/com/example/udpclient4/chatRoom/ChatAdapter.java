package com.example.udpclient4.chatRoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.Config.EmoC;
import com.example.udpclient4.Config.HeadC;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.R;

import java.util.List;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatHolder>{
    private Context context;
    //会话列表
    private List<ChatEnity> chatList;
    public ChatAdapter(Context context, List<ChatEnity> list){
        this.context=context;
        this.chatList=list;
    }
    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView= LayoutInflater.from(context).inflate(R.layout.item_chat,parent,false);
        return new ChatHolder(convertView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        ChatEnity chat=chatList.get(position);
        holder.itemView.setTag(R.id.tag_pos,position);
        holder.msgRight.setTag(R.id.tag_pos,position);
        holder.gifRight.setTag(R.id.tag_pos,position);

        //设置可见性
        holder.layoutRight.setVisibility(View.GONE);
        holder.layoutLeft.setVisibility(View.GONE);
        holder.layoutTips.setVisibility(View.GONE);
        switch (chat.type){
            case LEFT_MSG:
                holder.layoutLeft.setVisibility(View.VISIBLE);
                //holder.msgLeft.setVisibility(View.VISIBLE);
                holder.msgLeft.setText(chat.msg);
                holder.nameLeft.setText(chat.account);
                holder.headLeft.setImageResource(HeadC.HEAD_P[chat.head]);
                holder.gifLeft.setVisibility(View.GONE);
                break;
            case RIGHT_MSG:
                holder.layoutRight.setVisibility(View.VISIBLE);
                //holder.msgRight.setVisibility(View.GONE);
                holder.nameRight.setText(chat.account);
                holder.msgRight.setText(chat.msg);
                holder.headRight.setImageResource(HeadC.HEAD_P[UserC.HEAD_ID]);
                holder.gifRight.setVisibility(View.GONE);
                break;
            case LEFT_GIF:
                holder.layoutLeft.setVisibility(View.VISIBLE);
                holder.nameLeft.setText(chat.account);
                holder.msgLeft.setText("");
                holder.headLeft.setImageResource(HeadC.HEAD_P[chat.head]);
                holder.gifLeft.setVisibility(View.VISIBLE);
                holder.gifLeft.setImageResource(EmoC.EMO_P[chat.gifIdx]);
                //holder.msgLeft.setVisibility(View.GONE);
                break;
            case RIGHT_GIF:
                holder.layoutRight.setVisibility(View.VISIBLE);
                holder.nameRight.setText(chat.account);
                holder.msgRight.setText("");
                holder.headRight.setImageResource(HeadC.HEAD_P[UserC.HEAD_ID]);
                holder.gifRight.setVisibility(View.VISIBLE);
                holder.gifRight.setImageResource(EmoC.EMO_P[chat.gifIdx]);
                //holder.msgRight.setVisibility(View.GONE);
                //holder.gifRight.setImageResource(R.);
                break;
            case REVOKE:
                holder.layoutTips.setVisibility(View.VISIBLE);
                holder.tips.setText(chat.account+"撤回一条信息");
                break;
            case TIME:
                holder.tips.setText(chat.tips);
                holder.layoutTips.setVisibility(View.VISIBLE);
                break;
        }

    }
    protected ChatListener chatListener;
    public void setChatListener(ChatListener chatListener){
        this.chatListener=chatListener;
    }
    @Override
    public int getItemCount() {
        return chatList==null?0:chatList.size();
    }
}
