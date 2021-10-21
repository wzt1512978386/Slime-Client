package com.example.udpclient4.GameRoom.Over;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Config.GameC;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.Login.User;
import com.example.udpclient4.R;
import com.example.udpclient4.Ranking.RankAdapter;
import com.example.udpclient4.Ranking.RecordEnity;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * @author: wzt
 * @date: 2021/5/16
 */
public class OverActivity extends Activity {
    protected RecyclerView recordView;
    protected RankAdapter rankAdapter;
    public List<RecordEnity> recordList;
    //gif 动画
    private boolean runFlag;
    private GifImageView gif_ratate;
    //private
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_over);
        MyApp.overActivity=this;
        init();
        waitRanking();
    }

    private void waitRanking(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    boolean waitFlag = true;
                    do {
                        for (int i = MyApp.rankList.size() - 1; i >= 0; i--) {
                            RecordEnity r = MyApp.rankList.get(i);
                            if (r.account.equals(UserC.ACCOUNT) && r.score == UserC.SCORE) {
                                waitFlag = false;
                                MyApp.overActivity.recordList.clear();
                                for (int j = i - 2; j <= i + 2; j++) {
                                    RecordEnity rtmp;
                                    if (j >= 0 && j < MyApp.rankList.size()) {
                                        rtmp = new RecordEnity(MyApp.rankList.get(j));
                                        rtmp.rankingFlag=false;
                                    } else {
                                        rtmp = new RecordEnity(-1, "", -1,false);
                                    }
                                    MyApp.overActivity.recordList.add(rtmp);

                                }
                                MyApp.overActivity.rankAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        try {
                            wait(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    while (waitFlag);
                }
            }
        }).start();
    }

    @Override
    public void finish() {
        super.finish();
        MyApp.gameRoomActivity.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRotate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        runFlag=false;
    }
    private void startRotate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    runFlag=true;
                    int tic=0;
                    while (runFlag){
                        int finalTic = tic;
                        MyApp.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                gif_ratate.setImageResource(GameC.gifMan2[finalTic]);
                            }
                        });

                        tic=(tic+1)%4;
                        try {
                            wait(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void init(){
        recordList=new ArrayList<>();
        recordView=(RecyclerView)findViewById(R.id.recyclerview_over);
        rankAdapter=new RankAdapter(this,recordList);
        RecyclerView.LayoutManager  layoutManager= new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recordView.setLayoutManager(layoutManager);
        recordView.setAdapter(rankAdapter);

        //gif
        gif_ratate=(GifImageView)findViewById(R.id.gifview_over);
    }
}
