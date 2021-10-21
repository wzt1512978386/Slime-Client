package com.example.udpclient4.chatRoom;

import com.example.udpclient4.Data.MyDate;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class ChatEnity {
    static enum TYPE{
        LEFT_MSG,
        RIGHT_MSG,
        LEFT_GIF,
        RIGHT_GIF,
        REVOKE,
        TIME
    }
    protected TYPE type;
    public String account,msg;
    public int gifIdx,head;
    //protected String user;
    protected String tips;
    public MyDate date;
    public ChatEnity(TYPE type,String str,String account,String date,int head){
        this.type=type;
        this.account=account;
        this.head=head;
        switch (type){
            case LEFT_MSG:
            case RIGHT_MSG:
                msg=str;
                break;
            case LEFT_GIF:
            case RIGHT_GIF:
                msg=str;
                gifIdx=Integer.parseInt(str);
                break;
            case REVOKE:
            case TIME:
                tips=str;
                break;
        }
        this.date=new MyDate(date);
    }

}
