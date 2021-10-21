package com.example.udpclient4.Ranking;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.R;

/**
 * @author: wzt
 * @date: 2021/5/16
 */
public class RankHolder extends RecyclerView.ViewHolder {
    public TextView no,account,score;
    public View itemView;
    public RankHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        no=(TextView)itemView.findViewById(R.id.textview_item_record_no);
        account=(TextView)itemView.findViewById(R.id.textview_item_record_account);
        score=(TextView)itemView.findViewById(R.id.textview_item_record_score);
    }
}
