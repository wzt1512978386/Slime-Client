package com.example.udpclient4.GameRoom;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Client.Client;
import com.example.udpclient4.Config.GameC;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.GameRoom.Score.Score;
import com.example.udpclient4.R;
import com.example.udpclient4.Util.SysU;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * @author: wzt
 * @date: 2021/5/5
 */
public class Spirit {
    private static final String GSP="__=__";
    enum GW_TYPE{
        //GREEN, BLUE, ZZ, NONE
        G, B, Z, N
    }
    enum DIR {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    //调用
    private GameLayout GL;
    //怪物
    private int gwNum=20;
    private float [][] gwPos;
    private DIR[]gwDir;
    private GifImageView[]gw;
    private GW_TYPE []gwType;
    private float gwSpeed=7;

    //人
    public int manNum=0;
    public List<Player> man;
    private final int maxManNum=10;
    private Player []man_=new Player[maxManNum];
    //
    public List<Food> foods;
    public int foodNum=6;

    /*
    private GifImageView []man;
    private DIR[]manDir;
    private float [][]manPos;
    */
    private float manSpeed=13;

    protected float scaleX,scaleY;
    public Spirit(GameLayout gameLayout) {
        //绑定
        MyApp.spirit=this;
        this.GL=gameLayout;
        //gw
        gw=new GifImageView[gwNum];
        gwPos = new float[gwNum][6];
        gwDir=new DIR[gwNum];
        gwType = new GW_TYPE[gwNum];
        for (int i = 0; i < gwNum; i++) {
            gwDir[i]=DIR.DOWN;
            gw[i]=new GifImageView(GL.context);
            gw[i].setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            if (i < 6) {
                gwType[i] = GW_TYPE.B;
                gw[i].setImageResource(R.drawable.gif_game_slm_down);
            }
            else if(i<gwNum){
                gwType[i] = GW_TYPE.G;
                gw[i].setImageResource(R.drawable.gif_game_slm2_down);
            }
            else{
                gwType[i] = GW_TYPE.Z;
                gw[i].setImageResource(R.drawable.gif_game_zz_down);
            }
            GL.addView(gw[i]);
        }
        //man
        //man
        man=new ArrayList<>();
        for(int i=0;i<maxManNum;i++)
            man_[i]=new Player();
        /*
        man=new GifImageView[manNum];
        manPos=new float[manNum][4];
        manDir=new DIR[manNum];
        for(int i=0;i<manNum;i++){
            manDir[i]=DIR.DOWN;
            man[i] =GL.findViewById(R.id.gif_gameroom_man1);
            man[i].bringToFront();
        }*/
        //food
        foods=new ArrayList<>();
        for(int i=0;i<foodNum;i++)
            foods.add(new Food());

    }
    public void userMove(float sensorX,float sensorY){
        float X_d = -f2S(sensorX) * manSpeed;
        float Y_d = f2S(sensorY) * manSpeed;
        for(Player p:man){
            if(p.account.equals(UserC.ACCOUNT)){
                float posX=p.pos[0] + X_d;
                float posY=p.pos[1] + Y_d;
                MyApp.client.sendMsg(posX+" "+posY+" "+X_d+" "+Y_d, Client.TYPE.GAME_MOVE);
                break;
            }
        }

    }
    private List<Font> fonts;
    private final float YD=100;
    /**/
    public void updatePos(){
        MyApp.handler.post(new Runnable() {
            @Override
            public void run() {
                fonts=new ArrayList<>();
                //gw
                for(int i=0;i<gwNum;i++){

                    float W_gw=gw[i].getWidth();
                    float H_gw=gw[i].getHeight();
                    gw[i].setX((gwPos[i][0]-W_gw/2)*scaleX);
                    gw[i].setY((gwPos[i][1]+YD-H_gw)*scaleY);
                    if(gwType[i]==GW_TYPE.G)
                        changeDir(gw, GameC.gifSlm,gwPos[i][4],gwPos[i][5],gwDir,i);
                    else if(gwType[i]==GW_TYPE.B)
                        changeDir(gw, GameC.gifSlm2,gwPos[i][4],gwPos[i][5],gwDir,i);
                    else if(gwType[i]==GW_TYPE.Z) {
                        SysU.IN101("??");
                        changeDir(gw, GameC.gifZz, gwPos[i][4], gwPos[i][5], gwDir, i);
                    }
                    fonts.add(new Font(gwPos[i][1]-H_gw, Font.TYPE.GW,i));
                }
                //man
                for(int i=0;i<man.size();i++) {
                    Player p = man.get(i);
                    float W_man = p.view.getWidth();
                    float H_man = p.view.getHeight();
                    p.view.setX((p.pos[0] - W_man / 2) * scaleX);
                    p.view.setY((p.pos[1]+YD - H_man) * scaleY);
                    p.view.bringToFront();

                    p.name.setX(p.view.getX());
                    p.name.setY(p.view.getY()-60);
                    p.name.bringToFront();

                    if (p.type == Player.MAN_TYPE.L)
                        changeDir(p, GameC.gifMan1);
                    else if (p.type == Player.MAN_TYPE.Q)
                        changeDir(p, GameC.gifMan2);
                    fonts.add(new Font(p.pos[1] - H_man, Font.TYPE.MAN, i));
                    //SysU.IN101(p.view.getX()+" "+p.view.getY());
                }
                Collections.sort(fonts);
                for(int i=0;i<fonts.size();i++) {
                    Font F = fonts.get(i);
                    if (F.type == Font.TYPE.GW)
                        gw[F.idx].bringToFront();
                    else if (F.type == Font.TYPE.MAN) {
                        man.get(F.idx).view.bringToFront();
                        man.get(F.idx).name.bringToFront();
                    }
                }
                //food
                for(Food F:foods){
                    /*
                    if(F.change||F.init) {
                        float W_food = F.view.getWidth();
                        float H_food = F.view.getHeight();
                        F.view.setImageResource(GameC.imageFood[F.type]);
                        //F.view.setX((F.X - W_food / 2) * scaleX);
                        //F.view.setY((F.Y - H_food / 2) * scaleY);
                        F.view.setX(F.X * scaleX);
                        F.view.setY(F.Y * scaleY);
                        //SysU.IN101(F.view.getX()+" "+F.view.getY());
                        F.init = false;
                    }
                     */
                    F.view.setImageResource(GameC.imageFood[F.type]);
                    F.view.setX((F.X-50) * scaleX);
                    F.view.setY((F.Y+YD-100) * scaleY);
                }
                /*
                for(int i=0;i<manNum;i++){
                    float W_man=man[i].getWidth();
                    float H_man=man[i].getHeight();
                    man[i].setX((manPos[i][0]-W_man/2)*scaleX);
                    man[i].setY((manPos[i][1]-H_man)*scaleY);
                    changeDir(man, GameC.gifMan1,manPos[i][2],manPos[i][3],manDir,i);

                }

                 */
            }
        });

    }
    /*改变方向*/
    private void changeDir(Player p,Integer []gifDir){
        float X_d=p.pos[2];
        float Y_d=p.pos[3];
        if(Math.abs(X_d)<1&&Math.abs(Y_d)<1)
            X_d=X_d;
        else if(X_d>0&&X_d> Math.abs(Y_d)) {
            if(p.dir!=DIR.RIGHT) {
                p.dir=DIR.RIGHT;
                p.view.setImageResource(gifDir[3]);
            }
        }
        else if(X_d<0&&Math.abs(X_d)>Math.abs(Y_d)) {
            if(p.dir!=DIR.LEFT) {
                p.dir=DIR.LEFT;
                p.view.setImageResource(gifDir[1]);
            }
        }
        else if(Y_d>0&&Y_d>Math.abs(X_d)) {
            if(p.dir!=DIR.DOWN) {
                p.dir=DIR.DOWN;
                p.view.setImageResource(gifDir[0]);
            }
        }
        else if(Y_d<0&&Math.abs(Y_d)>Math.abs(X_d)){
            if(p.dir!=DIR.UP) {
                p.dir=DIR.UP;
                p.view.setImageResource(gifDir[2]);
            }
        }
    }
    private void changeDir(ImageView[]item, Integer []gifDir, float X_d, float Y_d, DIR []dir, int idx){
        if(Math.abs(X_d)<1&&Math.abs(Y_d)<1)
            X_d=X_d;
        else if(X_d>0&&X_d> Math.abs(Y_d)) {
            if(dir[idx]!=DIR.RIGHT) {
                dir[idx]=DIR.RIGHT;
                item[idx].setImageResource(gifDir[3]);
            }
        }
        else if(X_d<0&&Math.abs(X_d)>Math.abs(Y_d)) {
            if(dir[idx]!=DIR.LEFT) {
                dir[idx]=DIR.LEFT;
                item[idx].setImageResource(gifDir[1]);
            }
        }
        else if(Y_d>0&&Y_d>Math.abs(X_d)) {
            if(dir[idx]!=DIR.DOWN) {
                dir[idx]=DIR.DOWN;
                item[idx].setImageResource(gifDir[0]);
            }
        }
        else if(Y_d<0&&Math.abs(Y_d)>Math.abs(X_d)){
            if(dir[idx]!=DIR.UP) {
                dir[idx]=DIR.UP;
                item[idx].setImageResource(gifDir[2]);
            }
        }
    }


    /*解码*/
    public void toDecode_wrong(String str){
        String []strs=str.split(GSP);
        manNum=Integer.parseInt(strs[0]);
        //man=new ArrayList<>();
        String []tmp=strs[1].split(" ");
        //SysU.IN101(manNum+" ");
        for(int i=0;i<manNum;i++) {
            //man.add(new Player(tmp[i], Player.MAN_TYPE.NONE));
            man_[i].account=tmp[i];
        }

        char []tmpC=strs[2].toCharArray();//.split(" ");
        for(int i=0;i<manNum;i++) {
            switch (tmpC[i]){
                case 'L':
                    //man.get(i).type= Player.MAN_TYPE.MAN1;
                    man_[i].type= Player.MAN_TYPE.L;
                    //man.get(i).view=GL.findViewById(R.id.gif_gameroom_man1);
                    break;
                case 'Q':
                    man_[i].type= Player.MAN_TYPE.Q;
                    //man.get(i).type= Player.MAN_TYPE.MAN2;
                    break;
            }
        }
        //tmp=strs[3].split(" ");
        /*
        tmpC=strs[3].toCharArray();
        for(int i=0;i<manNum;i++){
            man_[i].pos[0]=FF1(tmpC[8*i+0],tmpC[8*i+1],tmpC[8*i+2]);
            man_[i].pos[1]=FF1(tmpC[8*i+3],tmpC[8*i+4],tmpC[8*i+5]);
            man_[i].pos[2]=FF2(tmpC[8*i+6]);
            man_[i].pos[3]=FF2(tmpC[8*i+7]);
            SysU.IN101(man_[i].pos[0]+"  "+man_[i].pos[1]+"  "+(int)tmpC[8*i+0]+" "+(byte)(tmpC[8*i+1]&0xff)+" "+(int)tmpC[8*i+2]);
            //for(int j=0;j<4;j++)
                //man.get(i).pos[j]=Float.parseFloat(tmp[i*4+j]);
                //man_[i].pos[j]=Float.parseFloat(tmp[i*4+j]);

        }
         */



        //gw
        gwNum=Integer.parseInt(strs[3]);
        tmpC=strs[4].toCharArray();//.split(" ");
        for(int i=0;i<gwNum;i++){
            switch (tmpC[i]){
                case 'G':
                    gwType[i]=GW_TYPE.G;
                    break;
                case 'B':
                    gwType[i]=GW_TYPE.B;
                    break;
                case 'Z':
                    gwType[i]=GW_TYPE.Z;
                    break;
                default:
                    gwType[i]=GW_TYPE.N;
            }
        }
        /*
        tmpC=strs[6].toCharArray();//.split(" ");
        for(int i=0;i<gwNum;i++){
            gwPos[i][0]=FF1(tmpC[8*i+0],tmpC[8*i+1],tmpC[8*i+2]);
            gwPos[i][1]=FF1(tmpC[8*i+3],tmpC[8*i+4],tmpC[8*i+5]);
            gwPos[i][4]=FF2(tmpC[8*i+6]);
            gwPos[i][5]=FF2(tmpC[8*i+7]);
            //for(int j=0;j<6;j++){
              //  gwPos[i][j]=Float.parseFloat(tmp[i*6+j]);

        }

         */
        //food
        foodNum=Integer.parseInt(strs[5]);
        tmp=strs[6].split(" ");
        for(int i=0;i<foodNum;i++){
            foods.get(i).X=Float.parseFloat(tmp[2*i]);
            foods.get(i).Y=Float.parseFloat(tmp[2*i+1]);
        }

        //SysU.IN101(foods.get(0).X+"  "+foods.get(0).Y);

        tmpC=strs[7].toCharArray();//.split(" ");
        for(int i=0;i<foodNum;i++){
            foods.get(i).type=Integer.parseInt(tmpC[i]+"");
        }
        /*
        tmp=strs[10].split(" ");
        for(int i=0;i<foodNum;i++){
            foods.get(i).change=Boolean.parseBoolean(tmp[i]);
        }*/
        tmp=strs[8].split(" ");
        for(int i=0;i<manNum;i++) {
            man_[i].score=Integer.parseInt(tmp[i]);
        }

        /*
        int idx=MyApp.client.BUFFER_LEN-8*(manNum+gwNum);
        for(int i=0;i<manNum;i++){
            man_[i].pos[0]=FF1(MyApp.client.BUFFER,idx+8*i);
            man_[i].pos[1]=FF1(MyApp.client.BUFFER,idx+8*i+3);
            man_[i].pos[2]=FF2(MyApp.client.BUFFER,idx+8*i+6);
            man_[i].pos[3]=FF2(MyApp.client.BUFFER,idx+8*i+7);
            SysU.IN101(man_[i].pos[0]+"  "+man_[i].pos[1]+"  "+(int)tmpC[8*i+0]+" "+(byte)(tmpC[8*i+1]&0xff)+" "+(int)tmpC[8*i+2]);
            //for(int j=0;j<4;j++)
            //man.get(i).pos[j]=Float.parseFloat(tmp[i*4+j]);
            //man_[i].pos[j]=Float.parseFloat(tmp[i*4+j]);
        }
        for(int i=0;i<gwNum;i++){
            gwPos[i][0]=FF1(MyApp.client.BUFFER,idx+8*(i+manNum));
            gwPos[i][1]=FF1(MyApp.client.BUFFER,idx+8*(i+manNum)+3);
            gwPos[i][4]=FF2(MyApp.client.BUFFER,idx+8*(i+manNum)+6);
            gwPos[i][5]=FF2(MyApp.client.BUFFER,idx+8*(i+manNum)+7);

            //for(int j=0;j<6;j++){
            //  gwPos[i][j]=Float.parseFloat(tmp[i*6+j]);

        }

         */




        for(int i=0;i<manNum;i++){
            boolean findFlag=false;
            for(Player p:man){
                if(p.account.equals(man_[i].account)){
                    findFlag=true;
                    for(int j=0;j<4;j++){
                        p.pos[j]=man_[i].pos[j];
                    }
                    p.score=man_[i].score;
                    break;
                }
            }
            if(!findFlag){
                Player player=new Player(man_[i].account,man_[i].type);
                man.add(player);
            }
        }
        for(int i=man.size()-1;i>=0;i--){
            boolean findFlag=false;
            for(int j=0;j<manNum;j++){
                if(man.get(i).account.equals(man_[j].account)){
                    findFlag=true;
                    break;
                }
            }
            if(!findFlag){
                man.get(i).view.setX(-1000);
                man.get(i).name.setX(-1000);
                man.remove(i);
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                GL.scoreList.clear();
                GL.scoreAdapter.notifyDataSetChanged();
                for(Player p:man){
                    GL.scoreList.add(new Score(p.account,p.type,p.score));
                }
                Collections.sort(GL.scoreList);
                GL.scoreAdapter.notifyDataSetChanged();
            }
        }).start();



    }

    /*解码*/
    public void toDecode(String str){
        String []strs=str.split(GSP);
        manNum=Integer.parseInt(strs[0]);
        //man=new ArrayList<>();
        String []tmp=strs[1].split(" ");
        //SysU.IN101(manNum+" ");
        for(int i=0;i<manNum;i++) {
            //man.add(new Player(tmp[i], Player.MAN_TYPE.NONE));
            man_[i].account=tmp[i];
        }

        char[]tmpC=strs[2].toCharArray();//.split(" ");
        for(int i=0;i<manNum;i++) {
            switch (tmpC[i]){
                case 'L':
                    //man.get(i).type= Player.MAN_TYPE.MAN1;
                    man_[i].type= Player.MAN_TYPE.L;
                    //man.get(i).view=GL.findViewById(R.id.gif_gameroom_man1);
                    break;
                case 'Q':
                    man_[i].type= Player.MAN_TYPE.Q;
                    //man.get(i).type= Player.MAN_TYPE.MAN2;
                    break;
            }
        }
        tmp=strs[3].split(" ");
        for(int i=0;i<manNum;i++){
            for(int j=0;j<4;j++){
                //man.get(i).pos[j]=Float.parseFloat(tmp[i*4+j]);
                man_[i].pos[j]=Float.parseFloat(tmp[i*4+j]);
            }
        }



        //gw
        gwNum=Integer.parseInt(strs[4]);
        tmpC=strs[5].toCharArray();//.split(" ");
        for(int i=0;i<gwNum;i++){
            switch (tmpC[i]){
                case 'G':
                    gwType[i]=GW_TYPE.G;
                    break;
                case 'B':
                    gwType[i]=GW_TYPE.B;
                    break;
                case 'Z':
                    gwType[i]=GW_TYPE.Z;
                    break;
                default:
                    gwType[i]=GW_TYPE.N;
            }
        }
        tmp=strs[6].split(" ");
        for(int i=0;i<gwNum;i++){
            gwPos[i][0]=Float.parseFloat(tmp[i*4+0]);
            gwPos[i][1]=Float.parseFloat(tmp[i*4+1]);
            gwPos[i][4]=Float.parseFloat(tmp[i*4+2]);
            gwPos[i][5]=Float.parseFloat(tmp[i*4+3]);
        }
        //food
        foodNum=Integer.parseInt(strs[7]);
        tmp=strs[8].split(" ");
        for(int i=0;i<foodNum;i++){
            foods.get(i).X=Float.parseFloat(tmp[2*i]);
            foods.get(i).Y=Float.parseFloat(tmp[2*i+1]);
        }

        //SysU.IN101(foods.get(0).X+"  "+foods.get(0).Y);

        tmpC=strs[9].toCharArray();//.split(" ");
        for(int i=0;i<foodNum;i++){
            foods.get(i).type=Integer.parseInt(tmpC[i]+"");
        }
        /*
        tmp=strs[10].split(" ");
        for(int i=0;i<foodNum;i++){
            foods.get(i).change=Boolean.parseBoolean(tmp[i]);
        }*/
        tmp=strs[10].split(" ");
        for(int i=0;i<manNum;i++) {
            man_[i].score=Integer.parseInt(tmp[i*2+0]);
            int newHp=Integer.parseInt(tmp[i*2+1]);
            if(man_[i].HP!=newHp) {
                MyApp.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyApp.gameRoomActivity.heart.setText(newHp+"");
                    }
                });
            }
            man_[i].HP=newHp;

        }


        for(int i=0;i<manNum;i++){
            boolean findFlag=false;
            for(Player p:man){
                if(p.account.equals(man_[i].account)){
                    findFlag=true;
                    for(int j=0;j<4;j++){
                        p.pos[j]=man_[i].pos[j];
                    }
                    p.score=man_[i].score;
                    break;
                }
            }
            if(!findFlag){
                Player player=new Player(man_[i].account,man_[i].type);
                man.add(player);
            }
        }
        for(int i=man.size()-1;i>=0;i--){
            boolean findFlag=false;
            for(int j=0;j<manNum;j++){
                if(man.get(i).account.equals(man_[j].account)){
                    findFlag=true;
                    break;
                }
            }
            if(!findFlag){
                man.get(i).view.setX(-1000);
                man.get(i).name.setX(-1000);
                man.remove(i);
            }
        }

        MyApp.handler.post(new Runnable() {
            @Override
            public void run() {
                GL.scoreList.clear();
                GL.scoreAdapter.notifyDataSetChanged();
                for(Player p:man){
                    GL.scoreList.add(new Score(p.account,p.type,p.score));
                }
                Collections.sort(GL.scoreList);
                GL.scoreAdapter.notifyDataSetChanged();
            }
        });



    }

    private float FF2(byte []b,int idx){
        return ((int)(b[idx])*1f/10f-10f);
    }
    private float FF1(byte[]b,int idx){
        return ((int)(b[idx+2])+(int)(b[idx+1])*256+(int)(b[idx])*256*256)*1f/100f;
    }
    /*映射函数*/
    private float f2S(float a){
        if(a>2)
            return 1;
        else if(a>0)
            return -0.5f*(float)(Math.cos(a/2*Math.PI)-1);
        else if(a>-2)
            return 0.5f*(float)(Math.cos(a/2*Math.PI)-1);
        else
            return -1;
    }
}
