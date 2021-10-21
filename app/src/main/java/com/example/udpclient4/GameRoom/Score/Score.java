package com.example.udpclient4.GameRoom.Score;

import com.example.udpclient4.GameRoom.Player;

/**
 * @author: wzt
 * @date: 2021/5/6
 */
public class Score implements Comparable<Score>{
    public int score;
    public String account;
    public Player.MAN_TYPE type;
    public Score(String account,Player.MAN_TYPE type,int score){
        this.score=score;
        this.account=account;
        this.type=type;
    }
    @Override
    public int compareTo(Score o) {
        return o.score-score;
    }
}
