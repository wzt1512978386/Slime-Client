package com.example.udpclient4.GameRoom;

import android.view.ViewGroup;
import android.widget.TextView;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.R;


import pl.droidsonroids.gif.GifImageView;

public class Player {
    public enum MAN_TYPE{Q,L,NONE};
    public String account;
    public float []pos=new float[4];
    public MAN_TYPE type;
    public GifImageView view;
    public TextView name;
    public Spirit.DIR dir;
    public int score,HP;
    public Player(){

    }
    public Player(String account,MAN_TYPE type) {
        this.account=account;
        this.type=type;
        this.pos[0]= (float) (Math.random()*MyApp.gameLayout.W_F);
        this.pos[1]=(float)(Math.random()* MyApp.gameLayout.H_F);
        this.dir= Spirit.DIR.DOWN;
        this.HP=100;
        MyApp.handler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
        this.view=new GifImageView(MyApp.gameLayout.context);
        this.view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        switch(type) {
            case L:
                this.view.setImageResource(R.drawable.gif_game_man1_down);
                //this.view = MyApp.gameLayout.findViewById(R.id.gif_gameroom_man1);
                break;
            case Q:
                this.view.setImageResource(R.drawable.gif_game_man2_down);
                //this.view = MyApp.gameLayout.findViewById(R.id.gif_gameroom_man1);
                break;
        }
        this.name=new TextView(MyApp.gameLayout.context);
        this.name.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        this.name.setText(account);
        this.name.setTextSize(10);
        this.name.setAlpha(1f);
        MyApp.handler.post(new Runnable() {
            @Override
            public void run() {
                MyApp.gameLayout.addView(view);
                MyApp.gameLayout.addView(name);
            }
        });

        //this.view.bringToFront();
    }
}
