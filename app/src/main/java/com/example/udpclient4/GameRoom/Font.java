package com.example.udpclient4.GameRoom;

/**
 * @author: wzt
 * @date: 2021/5/5
 */
public class Font implements Comparable<Font>{
    enum TYPE{MAN,GW}
    public float Y;
    public TYPE type;
    public int idx;
    public Font(float Y,TYPE type,int idx){
        this.Y=Y;
        this.type=type;
        this.idx=idx;
    }
    @Override
    public int compareTo(Font o) {
        return (int)(Y-o.Y);
    }

}
