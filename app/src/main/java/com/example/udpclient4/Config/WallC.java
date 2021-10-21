package com.example.udpclient4.Config;

import com.example.udpclient4.R;

/**
 * @author: wzt
 * @date: 2021/5/16
 */
public class WallC {
    public final static Integer []WALL_P={
            R.drawable.p_chatroom_bg_0,
            R.drawable.p_chatroom_bg_1,
            R.drawable.p_chatroom_bg_2,
            R.drawable.p_chatroom_bg_3,
            R.drawable.p_chatroom_bg_4,
            R.drawable.p_chatroom_bg_5,
            R.drawable.p_chatroom_bg_6,
            R.drawable.p_chatroom_bg_7,
            R.drawable.p_chatroom_bg_8,
            R.drawable.p_chatroom_bg_9
    };
    private static final int WALL_NUM=WALL_P.length;
    public static int WALL_IDX=1;
    public static void addIdx(){
        WALL_IDX=(WALL_IDX+1)%WALL_NUM;
    }
    public static Integer getP(){
        return WALL_P[WALL_IDX];
    }
}
