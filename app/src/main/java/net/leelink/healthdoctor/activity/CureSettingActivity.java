package net.leelink.healthdoctor.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.ProvinceAdapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.bean.ProvinceBean;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CureSettingActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout rl_back,rl_price,rl_phone_price,rl_count,rl_time,rl_home_time,rl_hospital_time;
    AppCompatCheckBox cb_picture,cb_phone,cb_home,cb_hospital;
    public static int PICTURE = 3;
    public static int PHONE = 5;
    public static int COUNT = 7;
    private TextView tv_price,tv_phone_price,tv_count;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cure_setting);
        init();
        context = this;
        createProgressBar(context);
        initData();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        cb_picture = findViewById(R.id.cb_picture);
        cb_picture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setOption(1,isChecked);
            }
        });
        cb_phone = findViewById(R.id.cb_phone);
        cb_phone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setOption(2,isChecked);
            }
        });
        cb_home = findViewById(R.id.cb_home);
        cb_home.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setOption(3,isChecked);
            }
        });
        cb_hospital = findViewById(R.id.cb_hospital);
        cb_hospital.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setOption(4,isChecked);
            }
        });
        rl_price = findViewById(R.id.rl_price);
        rl_price.setOnClickListener(this);
        rl_phone_price =  findViewById(R.id.rl_phone_price);
        rl_phone_price.setOnClickListener(this);
        tv_phone_price = findViewById(R.id.tv_phone_price);
        tv_phone_price.setOnClickListener(this);
        tv_price = findViewById(R.id.tv_price);
        rl_count = findViewById(R.id.rl_count);
        rl_count.setOnClickListener(this);
        tv_count = findViewById(R.id.tv_count);
        rl_time = findViewById(R.id.rl_time);
        rl_time.setOnClickListener(this);
        rl_home_time = findViewById(R.id.rl_home_time);
        rl_home_time.setOnClickListener(this);
        rl_hospital_time = findViewById(R.id.rl_hospital_time);
        rl_hospital_time.setOnClickListener(this);

    }

    public void initData(){
        OkGo.<String>get(Urls.OPEN_OPTION)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取设置状态",json.toString());
                            if (json.getInt("status") == 200) {
                                JSONArray jsonArray = json.getJSONArray("data");
                                for(int i =0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if(jsonObject.getInt("typeId")==1) {
                                        if(jsonObject.getInt("state")==1) {
                                            cb_picture.setChecked(true);
                                        }else {
                                            cb_picture.setChecked(false);
                                        }
                                    }
                                    if(jsonObject.getInt("typeId")==2) {
                                        if(jsonObject.getInt("state")==1) {
                                            cb_phone.setChecked(true);
                                        }else {
                                            cb_phone.setChecked(false);
                                        }
                                    }
                                    if(jsonObject.getInt("typeId")==3) {
                                        if(jsonObject.getInt("state")==1) {
                                            cb_home.setChecked(true);
                                        }else {
                                            cb_home.setChecked(false);
                                        }
                                    }
                                    if(jsonObject.getInt("typeId")==4) {
                                        if(jsonObject.getInt("state")==1) {
                                            cb_hospital.setChecked(true);
                                        }else {
                                            cb_hospital.setChecked(false);
                                        }
                                    }
                                }

                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void setOption(int type,boolean state){
        int st;
        HttpParams httpParams =new HttpParams();
        if(state){
            httpParams.put("state",1);
            st = 1;
        }else {
            httpParams.put("state",0);
            st = 0;
        }
        httpParams.put("typeId",type);
        showProgressBar();
        OkGo.<String>post(Urls.OPEN_OPTION+"/"+type+"/"+st)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("设置状态",json.toString());
                            if (json.getInt("status") == 200) {

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
                        stopProgressBar();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_price:
                Intent intent = new Intent(this,PriceActivity.class);
                startActivityForResult(intent,PICTURE);
                break;
            case R.id.rl_phone_price:
                Intent intent1 = new Intent(this,PriceActivity.class);
                startActivityForResult(intent1,PHONE);
                break;
            case R.id.rl_count:
                Intent intent2 = new Intent(this,CureCountActivity.class);
                startActivityForResult(intent2,COUNT);
                break;
            case R.id.rl_time:
                Intent intent3 = new Intent(this,CureTimeActivity.class);
                intent3.putExtra("type",1);
                startActivity(intent3);
                break;
            case R.id.rl_home_time:
                Intent intent4 = new Intent(this,CureTimeActivity.class);
                intent4.putExtra("type",2);
                startActivity(intent4);
                break;
            case R.id.rl_hospital_time:
                Intent intent5 = new Intent(this,CureTimeActivity.class);
                intent5.putExtra("type",3);
                startActivity(intent5);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == PICTURE) {
            tv_price.setText( data.getStringExtra("price"));
            setPrice(PICTURE);
        } else if(resultCode == 3 && requestCode == PHONE) {
            tv_phone_price.setText(data.getStringExtra("price"));
            setPrice(PHONE);
        } else if(resultCode ==5 && requestCode == COUNT ) {
            tv_count.setText(data.getStringExtra("count"));
        }
    }

    public void setPrice(int type){
        int t;
        String price;
        if(type==PICTURE) {
            t= 1;
            price = tv_price.getText().toString().trim();
        } else {
            t =2;
            price = tv_phone_price.getText().toString().trim();
        }
        showProgressBar();
        OkGo.<String>post(Urls.SET_AMOUNT+"/"+t+"/"+price)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("设置价格",json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(context, "修改完成", Toast.LENGTH_LONG).show();
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
                        stopProgressBar();
                    }
                });
    }
}
