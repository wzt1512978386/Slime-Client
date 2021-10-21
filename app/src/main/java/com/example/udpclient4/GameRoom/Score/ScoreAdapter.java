package com.example.udpclient4.GameRoom.Score;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.R;

import java.util.List;

/**
 * @author: wzt
 * @date: 2021/5/6
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {
    private Context context;
    private List<Score> scoreList;
    public ScoreAdapter(Context context,List<Score> scoreList){
        this.context=context;
        this.scoreList=scoreList;
    }
    @NonNull
    @Override
    public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView= LayoutInflater.from(context).inflate(R.layout.item_score,parent,false);
        return new ScoreHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreHolder holder, int position) {
        Score score=scoreList.get(position);
        switch (score.type) {
            case L:
                holder.image.setImageResource(R.drawable.gif_game_man1_down);
                break;
            case Q:
                holder.image.setImageResource(R.drawable.gif_game_man2_down);
                break;
        }
        holder.account.setText(score.account);
        holder.score.setText(score.score+"");
    }

    @Override
    public int getItemCount() {
        return scoreList!=null?scoreList.size():0;
    }



}
