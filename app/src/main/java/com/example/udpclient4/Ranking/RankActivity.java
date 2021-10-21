package com.example.udpclient4.Ranking;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Client.Client;
import com.example.udpclient4.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzt
 * @date: 2021/5/16
 */
public class RankActivity extends Activity {

    protected RecyclerView rankView;
    protected RankAdapter rankAdapter;
    //闪光
    private boolean runFlag;
    private ImageView shine;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rank);
        MyApp.rankActivity=this;
        init();
        MyApp.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MyApp.client.sendMsg("", Client.TYPE.GAME_RANKING);
                MyApp.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rankAdapter.notifyDataSetChanged();
                    }
                }, 200);

            }
        }, 200);
    }
    private void init(){
        rankView=(RecyclerView)findViewById(R.id.recyclerview_ranking);
        rankAdapter=new RankAdapter(this,MyApp.rankList);
        RecyclerView.LayoutManager  layoutManager= new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rankView.setLayoutManager(layoutManager);
        rankView.setAdapter(rankAdapter);

        //闪光
        shine=(ImageView)findViewById(R.id.imageview_rank_award);
    }

    @Override
    protected void onStop() {
        super.onStop();
        runFlag=false;
    }

    @Override
    protected void onResume() {
        super.onResume();


        startShine();
    }
    private void startShine(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    runFlag=true;
                    int tic=0;
                    while(runFlag){
                        try {
                            wait(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        tic+=1;
                        int finalTic = tic;
                        MyApp.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                shine.setAlpha(Math.abs(finalTic %20-10)*1f/10);
                            }
                        });

                    }
                }
            }
        }).start();

    }
}
