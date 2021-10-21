package com.example.udpclient4.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Client.Client;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.R;
import com.example.udpclient4.Server.ServerActivity;
import com.example.udpclient4.Util.SysU;

import static com.example.udpclient4.App.MyApp.client;

/**
 * @author: wzt
 * @date: 2021/5/6
 */
public class LoginActivity extends Activity {
    public Context context;
    private static final String SP=" ___ ";
    protected EditText et_account,et_password;
    protected Button bt_login,bt_register;
    //阴影
    protected FrameLayout shadow;
    private Boolean shadowFlag=false;
    private int shadowTic;
    //头像
    protected ImageView head;
    //
    public enum TYPE{LOGIN,REGISTER,NONE}
    public TYPE type=TYPE.NONE;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        MyApp.loginActivity=this;
        context=this;
        preferenceInit();
        initClient();
        init();

    }

    private void preferenceInit(){
        SharedPreferences preferences=this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        MyApp.preferences=preferences;
        MyApp.editor=editor;



    }
    private void initClient(){
        client=new Client();
        client=client;
        if(!MyApp.connectFlag){
            MyApp.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    client.sendMsg("", Client.TYPE.TRY);
                    MyApp.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!MyApp.connectFlag) {
                                Toast.makeText(MyApp.loginActivity.context, "服务器还未连接", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(MyApp.loginActivity, ServerActivity.class);
                                startActivity(intent);
                            }
                        }
                    }, 1000);
                }
            }, 500);

        }

    };
    private void init(){
        et_account=(EditText)findViewById(R.id.edittext_login_account);
        et_password=(EditText)findViewById(R.id.edittext_login_password);

        shadow=(FrameLayout)findViewById(R.id.framelayout_login_shadow);

        head=(ImageView)findViewById(R.id.imageview_login_head);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startShadow();
        //SysU.IN101("开始阴影");
        String account=MyApp.preferences.getString("ACCOUNT","");
        String password=MyApp.preferences.getString("PASSWORD","");
        et_account.setText(account);
        et_password.setText(password);
    }

    @Override
    protected void onPause() {
        super.onPause();
        shadowFlag=false;
        //SysU.IN101("停止阴影");
    }
    /*开始背景阴影晃动*/
    private void startShadow(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    shadowFlag = true;
                    shadowTic = 0;
                    while (shadowFlag) {
                        try {
                            wait(50);
                            if (shadow == null)
                                continue;
                            shadow.setAlpha(Math.abs(shadowTic % 40 - 20) * 0.4f / 20);
                            shadow.setScrollX((shadowTic % 24 - 12)*2);
                            head.setTranslationY((float)(Math.pow(Math.sin(shadowTic/20.0*Math.PI),2)*30));
                            shadowTic++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //SysU.IN101("阴影已结束");
                }
            }
        }).start();
    }
    /*检查用户名和密码是否合法*/
    private boolean inuputLegal(){
        UserC.ACCOUNT=et_account.getText().toString().trim();
        UserC.PASSWORD=et_password.getText().toString().trim();

        MyApp.editor.putString("ACCOUNT",UserC.ACCOUNT);
        MyApp.editor.putString("PASSWORD",UserC.PASSWORD);
        MyApp.editor.apply();

        if(UserC.ACCOUNT.isEmpty()) {
            Toast.makeText(this, "用户名空的哦~", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(UserC.ACCOUNT.split(" ").length>1) {
            Toast.makeText(this, "用户名不能带空格哦~", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(UserC.PASSWORD.isEmpty()) {
            Toast.makeText(this, "密码空的哦~", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(UserC.PASSWORD.split(" ").length>1) {
            Toast.makeText(this, "密码不能带空格哦~", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /*注册事件*/
    public void register(View v){
        if(!inuputLegal())
            return;
        client.sendMsg(UserC.PASSWORD, Client.TYPE.LOGIN_REGISTER);
    }
    /*登录事件*/
    public void loginIn(View v){
        if(!inuputLegal())
            return;
        client.sendMsg(UserC.PASSWORD, Client.TYPE.LOGIN_IN);

        /*
        if(!MyApp.connectFlag){
            client.sendMsg("", Client.TYPE.TRY);
            MyApp.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!MyApp.connectFlag) {
                        Toast.makeText(MyApp.loginActivity.context, "服务器还未连接", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MyApp.loginActivity, ServerActivity.class);
                        startActivity(intent);
                    }
                }
            }, 1000);
        }*/







        /*
        SysU.IN101("登录成功！");
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);*/

    }
}
