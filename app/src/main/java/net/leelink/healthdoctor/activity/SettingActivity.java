package net.leelink.healthdoctor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.BaseActivity;


public class SettingActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout rl_back,rl_unlogin,rl_xieyi,rl_private,rl_about_us,rl_change_phone,rl_change_password,rl_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
}

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        rl_unlogin = findViewById(R.id.rl_unlogin);
        rl_unlogin.setOnClickListener(this);
        rl_xieyi = findViewById(R.id.rl_xieyi);
        rl_xieyi.setOnClickListener(this);
        rl_private = findViewById(R.id.rl_private);
        rl_private.setOnClickListener(this);
        rl_about_us = findViewById(R.id.rl_about_us);
        rl_about_us.setOnClickListener(this);
        rl_change_phone = findViewById(R.id.rl_change_phone);
        rl_change_phone.setOnClickListener(this);
        rl_change_password = findViewById(R.id.rl_change_password);
        rl_change_password.setOnClickListener(this);
        rl_user = findViewById(R.id.rl_user);
        rl_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_unlogin:
                unlogin();
                break;
            case R.id.rl_xieyi:
                Intent intent = new Intent(this,WebActivity.class);
                intent.putExtra("type","distribution");
                intent.putExtra("url","https://www.llky.net.cn/doctor/protocol.html");
                startActivity(intent);
                break;
            case R.id.rl_private:
                Intent intent1 = new Intent(this,WebActivity.class);
                intent1.putExtra("type","distribution");
                intent1.putExtra("url","https://www.llky.net.cn/doctor/privacyPolicy.html");
                startActivity(intent1);
                break;
            case R.id.rl_about_us:
                Intent intent2 = new Intent(this,WebActivity.class);
                intent2.putExtra("type","distribution");
                intent2.putExtra("url","http://api.llky.net.cn/aboutus.html");
                startActivity(intent2);
                break;
            case R.id.rl_change_phone:
                Intent intent3 = new Intent(this,ChangePhoneActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_change_password:
                Intent intent4 = new Intent(this,ChangePasswordActivity.class);
                startActivity(intent4);
                break;
            case R.id.rl_user:
                Intent intent5 = new Intent(this,NowUserActivity.class);
                startActivity(intent5);
                break;

        }
    }

    public void unlogin(){
        Intent intent4 = new Intent(SettingActivity.this,LoginActivity.class);
        SharedPreferences sp = getSharedPreferences("sp",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("secretKey");
        editor.remove("telephone");
        editor.remove("clientId");
        editor.apply();
        intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent4);
        finish();
    }
}
