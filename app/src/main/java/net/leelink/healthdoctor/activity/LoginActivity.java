package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.MainActivity;
import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    ImageView img_see;
    EditText ed_telephone,ed_password,ed_sms_code;
    TextView tv_code_login,tv_get_code,tv_submit,tv_forget;
    RelativeLayout rl_password,rl_code;
    boolean visible = false;
    int login_type = 1;
    private int time = 60;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createProgressBar(this);
        init();
    }

    public void init(){
        img_see = findViewById(R.id.img_see);
        img_see.setOnClickListener(this);
        ed_telephone = findViewById(R.id.ed_telephone);
        ed_password = findViewById(R.id.ed_password);
        tv_code_login = findViewById(R.id.tv_code_login);
        tv_code_login.setOnClickListener(this);
        rl_password = findViewById(R.id.rl_password);
        rl_code = findViewById(R.id.rl_code);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_get_code = findViewById(R.id.tv_get_code);
        tv_get_code.setOnClickListener(this);
        ed_sms_code = findViewById(R.id.ed_sms_code);
        tv_submit = findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
        tv_forget = findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_see:      //切换密码状态
                if(visible){
                    ed_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    img_see.setImageResource(R.drawable.img_see_enable);
                    visible = false;
                } else {
                    ed_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    img_see.setImageResource(R.drawable.img_see_able);
                    visible = true;
                }

                break;
            case R.id.tv_code_login:        //更改登录方式
                if(rl_password.getVisibility() == View.VISIBLE) {
                    rl_password.setVisibility(View.GONE);
                    rl_code.setVisibility(View.VISIBLE);
                    tv_code_login.setText("密码登录");
                    login_type = 2;
                } else  {
                    rl_password.setVisibility(View.VISIBLE);
                    rl_code.setVisibility(View.GONE);
                    tv_code_login.setText("验证码登录");
                    login_type = 1;
                }

                break;
            case R.id.btn_login:        //登录
                if(login_type == 1) {
                    login();
                }else {
                    loginByCode();
                }
                break;
            case R.id.tv_get_code:      //获取验证码
                getSmsCode();
                break;
            case R.id.tv_submit:    //去注册
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_forget:
                Intent intent1 = new Intent(this,ForgetPasswordActivity.class);
                startActivity(intent1);
                break;
        }
    }

    //密码登录
    public void login(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("telephone", ed_telephone.getText().toString().trim());
            jsonObject.put("password", ed_password.getText().toString().trim());
            jsonObject.put("deviceToken", JPushInterface.getRegistrationID(LoginActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e( "login: ", JPushInterface.getRegistrationID(LoginActivity.this) );
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonObject));
        showProgressBar();
        OkGo.<String>post(Urls.LOGIN)
                .tag(this)
                .upRequestBody(requestBody)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("用户名密码登录", json.toString());
                            if (json.getInt("status") == 200) {
                                SharedPreferences sp = getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sp.edit();
                                json = json.getJSONObject("data");
                                editor.putString("secretKey",json.getString("token"));
                                MyApplication.token = json.getString("token");
                                editor.putString("telephone",ed_telephone.getText().toString().trim());
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //验证码登录
    public void loginByCode(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("telephone", ed_telephone.getText().toString().trim());
            jsonObject.put("smsCode",ed_sms_code.getText().toString().trim() );
            jsonObject.put("deviceToken", JPushInterface.getRegistrationID(LoginActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e( "login: ", JPushInterface.getRegistrationID(LoginActivity.this) );
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonObject));
        showProgressBar();
        OkGo.<String>post(Urls.LOGIN_SMS)
                .tag(this)
                .upRequestBody(requestBody)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("短信验证码登录", json.toString());
                            if (json.getInt("status") == 200) {
                                SharedPreferences sp = getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("secretKey",json.getString("data"));
                                MyApplication.token = json.getString("data");
                                editor.putString("telephone",ed_telephone.getText().toString().trim());
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //发送短信验证码
    public void getSmsCode(){
        showProgressBar();
        if (!ed_telephone.getText().toString().trim().equals("")) {
            OkGo.<String>post(Urls.SEND)
                    .tag(this)
                    .params("telephone", ed_telephone.getText().toString().trim())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            stopProgressBar();
                            try {
                                String body = response.body();
                                JSONObject json = new JSONObject(body);
                                Log.d("获取验证码", json.toString());
                                if (json.getInt("status") == 200) {
                                    if (time == 60) {
                                        new Thread(new LoginActivity.TimeRun()).start();
                                    } else {
                                        tv_get_code.setEnabled(false);
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                    tv_get_code.setOnClickListener(LoginActivity.this);
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
                    tv_get_code.setText("获取验证码");
                    time = 60;
                } else {
                    tv_get_code.setText((--time) + "秒");
                }
            }
        };
    }
}
