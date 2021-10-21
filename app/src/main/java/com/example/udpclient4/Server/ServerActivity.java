package com.example.udpclient4.Server;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.R;

/**
 * @author: wzt
 * @date: 2021/4/28
 */
public class ServerActivity extends Activity {
    //控件
    protected EditText []et_ip;
    protected EditText et_port;//,et_account;
    protected ImageButton bt_update;
    public TextView info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.serverActivity=this;
        setContentView(R.layout.layout_server);
        serverInit();
        new ServerEvents(this);

    }
    private void serverInit(){


        //初始化服务端IP
        //String SIP="172.26.211.138";
        //获取本地数据
        String SIP=MyApp.preferences.getString("SIP","120.78.216.21");
        int SPORT=MyApp.preferences.getInt("SPORT",58010);
        String ACCOUNT=MyApp.preferences.getString("ACCOUNT","123123");
        UserC.ACCOUNT=ACCOUNT;
        String []ips=SIP.split("\\.");

        //输入框绑定
        et_ip=new EditText[4];
        Integer []IDS={R.id.edittext_setting_ip1,R.id.edittext_setting_ip2,R.id.edittext_setting_ip3,R.id.edittext_setting_ip4};
        for(int i=0;i<4;i++) {
            et_ip[i] = (EditText) findViewById(IDS[i]);
            et_ip[i].setText(ips[i]);
        }
        et_port=(EditText)findViewById(R.id.edittext_setting_port);
        et_port.setText(""+SPORT);

        //et_account=(EditText)findViewById(R.id.edittext_setting_account);
        //et_account.setText(""+ACCOUNT);

        bt_update=(ImageButton)findViewById(R.id.imagebutton_server_update);
        info=(TextView)findViewById(R.id.textview_setting_info);

    }
}
