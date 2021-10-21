package com.example.udpclient4.Ranking;

/**
 * @author: wzt
 * @date: 2021/5/16
 */
public class RecordEnity {
    public String account;
    public int score,no;
    public boolean rankingFlag;
    public RecordEnity(RecordEnity r){
        this.no=r.no;
        this.account=r.account;
        this.score=r.score;
    }
    public RecordEnity(int no,String account, int score,boolean rankingFlag){
        this.account=account;
        this.score=score;
        this.no=no;
        this.rankingFlag=rankingFlag;
    }
}
