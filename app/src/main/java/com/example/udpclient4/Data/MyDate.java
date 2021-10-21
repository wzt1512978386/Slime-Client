package com.example.udpclient4.Data;

/**
 * @author: wzt
 * @date: 2021/5/2
 */
import java.util.Date;

public class MyDate {
    public int Y,M,D,h,m,s;
    public long ms;
    @SuppressWarnings("deprecation")
    public MyDate() {
        Date date=new Date();
        Y=date.getYear()+1900;
        M=date.getMonth()+1;
        D=date.getDate();
        h=date.getHours();
        m=date.getMinutes();
        s=date.getSeconds();
        ms=getTime();
    }
    public MyDate(String str) {
        String []strs=str.split(" ");
        Y=Integer.parseInt(strs[0]);
        M=Integer.parseInt(strs[1]);
        D=Integer.parseInt(strs[2]);
        h=Integer.parseInt(strs[3]);
        m=Integer.parseInt(strs[4]);
        s=Integer.parseInt(strs[5]);
        ms=getTime();


    }
    @Override
    public String toString() {
        return Y+" "+M+" "+D+" "+h+" "+m+" "+s;
    }
    @Override
    public boolean equals(Object o) {
        MyDate md=(MyDate)o;
        //if(Y==md.Y&&M==md.M&&D==md.D&&h==md.h&&m==md.m&&s==md.s)
        if(this.ms==md.ms)
            return true;
        else
            return false;
    }
    private Long getTime() {
        String h_,m_,s_;
        if(h<10)
            h_="0"+h;
        else
            h_=""+h;
        if(m<10)
            m_="0"+m;
        else
            m_=""+m;
        if(s<10)
            s_="0"+s;
        else
            s_=""+s;
        return Long.parseLong(Y+M+D+h_+m_+s_);

    }
    
}
