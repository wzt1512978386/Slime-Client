package com.example.udpclient4.Data;

/**
 * @author: wzt
 * @date: 2021/4/30
 */
public class ChatMsgEnity {
    //消息编码
    private static final String SP=" ___ ";
    //数据
    public String account,msg;
    public MyDate date;

    public ChatMsgEnity(String account, String msg){
        this.account=account;
        this.msg=msg;
    }
    public ChatMsgEnity(String account, String msg,String date) {
        this.account=account;
        this.msg=msg;
        this.date=new MyDate(date);
    }
    public ChatMsgEnity(String account, String msg,MyDate date) {
        this.account=account;
        this.msg=msg;
        this.date=date;
    }
    public String toEncode() {
        return msg+SP;
    }
    public String toEncodeT() {
        return msg+SP+date.toString();
    }
    public ChatMsgEnity(String []Strs) {
        account=Strs[0];

    }
}
