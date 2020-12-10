package net.leelink.healthdoctor.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_text, tv_get_code, tv_done;
    private RelativeLayout rl_back;
    private EditText ed_telephone, ed_code, ed_password, ed_confirm;
    private int time = 60;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        createProgressBar(this);
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        ed_telephone = findViewById(R.id.ed_telephone);
        tv_get_code = findViewById(R.id.tv_get_code);
        tv_get_code.setOnClickListener(this);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        ed_code = findViewById(R.id.ed_sms_code);
        ed_password = findViewById(R.id.ed_password);
        ed_confirm = findViewById(R.id.ed_confirm);
        tv_text = findViewById(R.id.tv_text);
        SpannableString spannableString = new SpannableString("注册代表您已阅读并同意<<用户协议>>及<<隐私政策>>");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegisterActivity.this, WebActivity.class);
                intent.putExtra("url", "http://api.iprecare.com:6280/h5/ambProtocol.html");
                intent.putExtra("title", "用户协议");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue)); //设置颜色
            }
        }, 11, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent = new Intent(RegisterActivity.this, WebActivity.class);
                intent.putExtra("url", "http://api.iprecare.com:6280/h5/ambPrivacyPolicy.html");
                intent.putExtra("title", "隐私政策");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue)); //设置颜色
            }
        }, 20, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_text.append(spannableString);
        tv_text.setMovementMethod(LinkMovementMethod.getInstance());  //很重要，点击无效就是由于没有设置这个引起
        tv_done = findViewById(R.id.tv_done);
        tv_done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_done:
            case R.id.back:
                finish();
                break;
            case R.id.tv_get_code:
                getSmsCode();
                break;
            case R.id.btn_submit:
                submit();
                break;
            default:
                break;
        }
    }

    public void submit() {
        if (ed_telephone.getText().toString().equals("")) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_code.getText().toString().equals("")) {
            Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_password.getText().toString().equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_confirm.getText().toString().equals("") || !ed_password.getText().toString().equals(ed_confirm.getText().toString())) {
            Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("telephone", ed_telephone.getText().toString());
            jsonObject.put("code", ed_code.getText().toString());
            jsonObject.put("password", ed_password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Urls.REGIST)
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("医生注册", json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    //发送短信验证码
    public void getSmsCode() {
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
                                        new Thread(new RegisterActivity.TimeRun()).start();
                                    } else {
                                        tv_get_code.setEnabled(false);
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
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
                    tv_get_code.setOnClickListener(RegisterActivity.this);
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
