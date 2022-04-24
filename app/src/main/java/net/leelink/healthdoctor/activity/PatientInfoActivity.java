package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class PatientInfoActivity extends BaseActivity {
    private RelativeLayout rl_back;
    private TextView tv_organize,tv_info_name,tv_info_sex,tv_nation,tv_phone,tv_card,tv_educate,tv_province,tv_city,tv_local,tv_address,tv_tall,tv_weight,tv_contact;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        init();
        context = this;
        createProgressBar(context);
        initData();

    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_organize = findViewById(R.id.tv_organize);
        tv_info_name = findViewById(R.id.tv_info_name);
        tv_info_sex = findViewById(R.id.tv_info_sex);
        tv_nation = findViewById(R.id.tv_nation);
        tv_phone = findViewById(R.id.tv_phone);
        tv_card = findViewById(R.id.tv_card);
        tv_educate = findViewById(R.id.tv_educate);
        tv_province = findViewById(R.id.tv_province);
        tv_city = findViewById(R.id.tv_city);
        tv_local = findViewById(R.id.tv_local);
        tv_address = findViewById(R.id.tv_address);
        tv_tall = findViewById(R.id.tv_tall);
        tv_weight = findViewById(R.id.tv_weight);
        tv_contact = findViewById(R.id.tv_contact);
    }

    public void initData(){
        showProgressBar();
        OkGo.<String>get(Urls.getInstance().ELDERLY_INFO+"/"+getIntent().getStringExtra("elderlyId"))
                .tag(this)
                .headers("token", MyApplication.token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("老人信息", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");

                                tv_info_name.setText(json.getString("elderlyName"));
                                tv_organize.setText(json.getString("organName"));
                                tv_nation.setText(json.getString("nation"));
                                if (json.has("sex")) {
                                    switch (json.getInt("sex")) {
                                        case 0:
                                            tv_info_sex.setText("男");
                                            break;
                                        case 1:
                                            tv_info_sex.setText("女");
                                            break;
                                    }
                                }

                                tv_card.setText(json.getString("idNumber"));
                                tv_phone.setText(json.getString("loginName"));
                                String[] ed = new String[]{"文盲", "半文盲", "小学", "初中", "高中", "技工学校", "中专/中技", "大专", "本科", "硕士", "博士"};
                                if (!json.getString("education").equals("null")) {
                                    tv_educate.setText(ed[json.getInt("education")-1]);
                                }
                                tv_province.setText(json.getString("userProvince"));
                                tv_city.setText(json.getString("userCity"));
                                tv_local.setText(json.getString("userArea"));
                                String address = json.getString("address");
                                JSONObject j = new JSONObject(address);
                                tv_address.setText(j.getString("fullAddress"));
                                tv_tall.setText(json.getString("height")+"cm");
                                tv_weight.setText(json.getString("weight")+"kg");
                                tv_contact.setText(json.getString("urgentPhone"));
                            }  else if (json.getInt("status") == 505) {
                                reLogin(context);
                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(context, "网络不给力呀", Toast.LENGTH_SHORT).show();
                        stopProgressBar();
                    }
                });
    }
}
