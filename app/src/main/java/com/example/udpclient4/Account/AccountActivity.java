package com.example.udpclient4.Account;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import com.example.udpclient4.App.MyApp;
import com.example.udpclient4.Config.HeadC;
import com.example.udpclient4.Config.UserC;
import com.example.udpclient4.R;

/**
 * @author: wzt
 * @date: 2021/5/6
 */
public class AccountActivity extends Activity {
    //外部调用
    public Context context;
    public String ACCOUNT,PASSWORD;
    //控件
    protected EditText et_account,et_password;
    protected ImageButton bt_head,bt_update;
    protected ImageButton []heads;
    //弹窗
    protected PopupWindow headPopWin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account);
        MyApp.accountActivity=this;
        context=this;
        init();
        popupWindowInit();
        new AccountEvent(this);
    }
    private void popupWindowInit(){
        View headView= LayoutInflater.from(this).inflate(R.layout.popwindow_head,null);
        headPopWin = new PopupWindow(headView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);//参数为1.View 2.宽度 3.高度
        headPopWin.setOutsideTouchable(true);//设置点击外部区域可以取消popupWindow

        heads=new ImageButton[9];
        for(int i=0;i<9;i++){
            heads[i]=(ImageButton)headView.findViewById(HeadC.HEAD_ID[i]);
        }
    }
    private void init(){
        et_account=(EditText)findViewById(R.id.editview_account_account);
        et_password=(EditText)findViewById(R.id.editview_account_password);
        String account= MyApp.preferences.getString("ACCOUNT","default");
        String password=MyApp.preferences.getString("PASSWORD","123");
        et_account.setText(account);
        et_password.setText(password);


        bt_head=(ImageButton)findViewById(R.id.imagebutton_accout_head);
        UserC.HEAD_ID=MyApp.preferences.getInt("HEAD",0);
        bt_head.setImageResource(HeadC.HEAD_P[UserC.HEAD_ID]);
        bt_update=(ImageButton)findViewById(R.id.imagebutton_account_update);
    }
}
