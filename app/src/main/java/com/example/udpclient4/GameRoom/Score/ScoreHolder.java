package com.example.udpclient4.GameRoom.Score;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.R;

/**
 * @author: wzt
 * @date: 2021/5/6
 */
public class ScoreHolder extends RecyclerView.ViewHolder{
    public View view;
    public TextView account,score;
    public ImageView image;
    public ScoreHolder(@NonNull View itemView) {
        super(itemView);
        this.view=itemView;
        score=(TextView) itemView.findViewById(R.id.textview_item_score_score);
        account=(TextView) itemView.findViewById(R.id.textview_item_score_account);
        image=(ImageView) itemView.findViewById(R.id.imageview_item_score_image);

    }
}
