package com.example.udpclient4.GameRoom;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.udpclient4.App.MyApp;

/**
 * @author: wzt
 * @date: 2021/5/5
 */
public class Food {
    public float X,Y;
    public int type;
    public boolean change;
    public boolean init;
    public ImageView view;
    public Food(){
        init=true;
        view=new ImageView(MyApp.gameLayout.context);
        this.view.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        this.view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        /*
        this.view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
         */
        MyApp.handler.post(new Runnable() {
            @Override
            public void run() {
                //view.setImageResource(GameC.imageFood[(int)(Math.random()*100)%9]);
                MyApp.gameLayout.addView(view);
            }
        });
    }
}
