package com.example.udpclient4.Ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.R;
import com.example.udpclient4.chatRoom.ChatHolder;

import java.util.List;
import java.util.zip.Inflater;

/**
 * @author: wzt
 * @date: 2021/5/16
 */
public class RankAdapter extends RecyclerView.Adapter<RankHolder>{
    //外部调用
    protected List<RecordEnity> recordList;
    protected Context context;
    public RankAdapter(Context context,List<RecordEnity> list){
        this.context=context;
        this.recordList=list;
    }
    @NonNull
    @Override
    public RankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView= LayoutInflater.from(context).inflate(R.layout.item_record,parent,false);
        return new RankHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RankHolder holder, int position) {
        RecordEnity record=recordList.get(position);
        holder.no.setText("NO."+record.no);
        holder.score.setText(record.score+"");
        holder.account.setText(record.account);
        if(record.rankingFlag) {
            if(UserC.ACCOUNT.equals(record.account)){
                holder.no.setTextColor(0xef00a2e8);
                holder.account.setTextColor(0xef00a2e8);
                holder.score.setTextColor(0xef00a2e8);
            }
            else {
                holder.no.setTextColor(0xefffffff);
                holder.account.setTextColor(0xefffffff);
                holder.score.setTextColor(0xefffffff);
            }
        }
        else{
            if(position==2) {
                holder.no.setTextColor(0xefd75f47);
                holder.account.setTextColor(0xefd75f47);
                holder.score.setTextColor(0xefd75f47);
            }
            else{
                holder.itemView.setAlpha(0.7f);
                holder.no.setTextColor(0xefffffff);
                holder.account.setTextColor(0xefffffff);
                holder.score.setTextColor(0xefffffff);
            }
        }
        if(record.no==-1&&record.score==-1){
            holder.no.setText("——");
            holder.score.setText("——");
            holder.account.setText("——");
        }
    }

    @Override
    public int getItemCount() {
        return recordList==null?0:recordList.size();
    }
}
