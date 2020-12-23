package net.leelink.healthdoctor.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;


import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_back,rl_top;
    private EditText ed_phone,ed_code;
    private TextView getmsmpass_TX;
    private Button btn_complete;
    private int time = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        init();
        createProgressBar(this);
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        ed_code = findViewById(R.id.ed_code);
        ed_phone = findViewById(R.id.ed_phone);
        rl_top = findViewById(R.id.rl_top);
        btn_complete = findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(this);
        getmsmpass_TX = findViewById(R.id.getmsmpass_TX);
        getmsmpass_TX.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_complete: //修改电话
                changePhone();
                break;
            case R.id.getmsmpass_TX:
                sendSmsCode();
                break;
                default:
                    break;
        }
    }

    public void changePhone(){
        Log.e( "新电话: ",  ed_phone.getText().toString().trim());
        Log.e( "验证码: ",  ed_code.getText().toString().trim());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("smsCode",ed_code.getText().toString().trim());
            jsonObject.put("telephone",ed_phone.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<String>post(Urls.getInstance().PHONE_CHANGE)
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("修改手机号",json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(ChangePhoneActivity.this, "修改手机号成功,请重新登录", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangePhoneActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(ChangePhoneActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //发送短信验证码
    public void sendSmsCode() {
        if (!ed_phone.getText().toString().trim().equals("")) {
            showProgressBar();
            OkGo.<String>post(Urls.getInstance().SEND+"?telephone="+ed_phone.getText().toString().trim())
                    .tag(this)
//                    .params("telephone", ed_phone.getText().toString().trim())
//                    .params("used", 1)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                JSONObject json =  new JSONObject(body);
                                Log.d("获取验证码", json.toString());
                                if (json.getInt("status") == 200) {
                                    stopProgressBar();
                                    if (time == 60) {
                                        new Thread(new ChangePhoneActivity.TimeRun()).start();
                                    } else {
                                        getmsmpass_TX.setEnabled(false);
                                    }
                                } else {
                                    Toast.makeText(ChangePhoneActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Toast.makeText(ChangePhoneActivity.this, "系统繁忙", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
        }
    }
    private class TimeRun implements Runnable {
        @Override
        public void run() {
            while (true) {
                mHandler.sendEmptyMessage(0);
                if (time == 0) {
                    getmsmpass_TX.setOnClickListener(ChangePhoneActivity.this);
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }

        @SuppressLint("HandlerLeak")
        private Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (time == 0) {
                    getmsmpass_TX.setText("获取验证码");
                    time = 60;
                } else {
                    getmsmpass_TX.setText((--time) + "秒");
                }
            }
        };
    }
}
