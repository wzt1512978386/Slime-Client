package com.example.udpclient4.GameRoom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.GameRoom.Score.Score;
import com.example.udpclient4.GameRoom.Score.ScoreAdapter;
import com.example.udpclient4.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzt
 * @date: 2021/5/4
 */
public class GameLayout extends FrameLayout {
    //外部调用
    public Context context;
    //虚拟舞台
    protected float W_F=1080f;
    protected float H_F=1920f;
    private float speed=4f;
    //精灵
    private Spirit S;



    //private float X_F=W_F/2;
    //private float Y_F=H_F/2;
    private float W_slm,H_slm;


    //空间变量
    private float W,H;//框架宽高
    private float Xc,Yc;//框架中心坐标

    //分数表
    private RecyclerView scoreView;
    public ScoreAdapter scoreAdapter;
    public List<Score> scoreList;

    public GameLayout(@NonNull Context context) {
        super(context);
        this.context=context;
        MyApp.gameLayout=this;
        setWillNotDraw(false);
    }
    public GameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        MyApp.gameLayout=this;
        setWillNotDraw(false);//让frame可以绘制
    }


    //初始化
    private boolean flagInit=true;
    private void init(){
        S=new Spirit(this);

        Player player=new Player(UserC.ACCOUNT, Player.MAN_TYPE.Q);
        MyApp.spirit.man.add(player);

        //分数表初始化
        scoreView=findViewById(R.id.recyclerview_gameroom_score);
        scoreList=new ArrayList<>();
        scoreAdapter=new ScoreAdapter(context,scoreList);
        RecyclerView.LayoutManager  layoutManager= new LinearLayoutManager(context, RecyclerView.VERTICAL,false);
        scoreView.setLayoutManager(layoutManager);
        scoreView.setAdapter(scoreAdapter);


        /*
        //slm
        slm1=new GifImageView[slm1Num];
        slm1Pos=new float[slm1Num][6];
        slm1Dir=new DIR[slm1Num];
        for(int i=0;i<slm1Num;i++){
            slm1Dir[i]=DIR.DOWN;
            slm1[i]=new GifImageView(context);
            slm1[i].setImageResource(R.drawable.gif_game_slm_down);
            slm1[i].setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            this.addView(slm1[i]);
            slmThread(slm1,slm1Pos,slm1Dir,GameC.gifSlm,slm1Speed,i);
        }
        //slm2
        slm2=new GifImageView[slm2Num];
        slm2Pos=new float[slm2Num][6];
        slm2Dir=new DIR[slm2Num];
        for(int i=0;i<slm2Num;i++){
            slm2Dir[i]=DIR.DOWN;
            slm2[i]=new GifImageView(context);
            slm2[i].setImageResource(R.drawable.gif_game_slm2_down);
            slm2[i].setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            this.addView(slm2[i]);
            slmThread(slm2,slm2Pos,slm2Dir,GameC.gifSlm2,slm2Speed,i);
        }
        //man
        man=new GifImageView[manNum];
        manPos=new float[manNum][2];
        manDir=new DIR[manNum];
        for(int i=0;i<manNum;i++){
            manDir[i]=DIR.DOWN;
            man[i] =findViewById(R.id.gif_gameroom_man1);
            man[i].bringToFront();
        }

         */
        //SysU.IN101(" "+ man[0].getX());
        initMeasure();
    }
    private void initMeasure(){
        W=getWidth();
        H=getHeight();
        Xc=W/2;
        Yc=H/2;
        MyApp.spirit.scaleX=W/W_F;
        MyApp.spirit.scaleY=H/H_F;
        //W_slm=slm1[0].getWidth();
        //H_slm=slm1[0].getHeight();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(flagInit){
            init();
            flagInit=false;
        }
        super.onDraw(canvas);
    }
    /*
    public void move(int idx){
        if(!flagInit) {
            float X_d = -f2S(GameC.SENSOR_X) * speed;
            float Y_d = f2S(GameC.SENSOR_Y) * speed;
            changeDir(man,GameC.gifMan1,X_d,Y_d,manDir,0);
            manPos[idx][0] += X_d;
            manPos[idx][1] += Y_d;

            float W_tmp=man[idx].getWidth();
            float H_tmp=man[idx].getHeight();

            posLimit(manPos[idx],W_tmp,H_tmp);
            //SysU.IN101("X "+X_F+"   Y "+Y_F);
            man[0].setX((manPos[idx][0]-W_tmp/2) * W / W_F);
            man[0].setY((manPos[idx][1]-H_tmp/2) * H / H_F);
        }
    }

     */
    private void calXYd(float []pos,float speed,boolean init){
        if(init) {
            pos[0] = (float) (Math.random() * W_F);
            pos[1] = (float) (Math.random() * H_F);
        }
        else{
            pos[0] = pos[2];
            pos[1] = pos[3];
        }

        do {
            pos[2] = (float) (Math.random() * W_F);
            pos[3] = (float) (Math.random() * H_F);
        }while (distance(pos)>speed*50);

        float XY_dest=distance(pos);
        pos[4]=(float)(speed*(pos[2]-pos[0])/XY_dest);
        pos[5]=(float)(speed*(pos[3]- pos[1])/XY_dest);
    }

    /*
    private void slmThread(GifImageView[]slm,
            float [][]slmPos,DIR[]slmDir,Integer []gifSlm,float slmSpeed,int idx){
        float []pos=slmPos[idx];
        calXYd(pos,slmSpeed,true);
        changeDir(slm,gifSlm,pos[4],pos[5],slmDir,idx);
        //SysU.IN101(pos[2]+"  "+pos[3]);
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    while (true) {
                        float W_tmp=slm[idx].getWidth();
                        float H_tmp=slm[idx].getHeight();
                        slm[idx].setX((slmPos[idx][0]-W_tmp/2) * W / W_F);
                        slm[idx].setY((slmPos[idx][1]-H_tmp) * H / H_F);
                        try {
                            wait(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        slmPos[idx][0] += slmPos[idx][4];
                        slmPos[idx][1] += slmPos[idx][5];
                        if (distance(slmPos[idx]) < slmSpeed) {
                            calXYd(slmPos[idx], slmSpeed,false);
                            changeDir(slm,gifSlm,slmPos[idx][4],slmPos[idx][5],slmDir,idx);


                        }

                    }
                }

            }
        }).start();
    }

     */
    private float distance(float x1,float y1,float x2,float y2){
        return (float)Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
    private float distance(float []pos){
        return (float)Math.sqrt(Math.pow(pos[0]-pos[2],2)+Math.pow(pos[1]-pos[3],2));
    }

    private void posLimit(float []pos,float w,float h){
        if(pos[0]<0)
            pos[0]=0;
        else if(pos[0]>W_F)
            pos[0]=W_F;
        if(pos[1]<0)
            pos[1]=0;
        else if(pos[1]>H_F)
            pos[1]=H_F;
        /*
        if(pos[0]<w/2)
            pos[0]=w/2;
        else if(pos[0]>W_F-w/2)
            pos[0]=W_F-w/2;
        if(pos[1]<h)
            pos[1]=h;
        else if(pos[1]>H_F)
            pos[1]=H_F;
         */
    }
}
