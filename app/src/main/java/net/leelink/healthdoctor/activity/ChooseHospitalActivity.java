package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.OnLocalListener;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.adapter.ProvinceAdapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.bean.ProvinceBean;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChooseHospitalActivity extends BaseActivity implements OnLocalListener {
    RelativeLayout rl_back;
    RecyclerView province_list,city_list,local_list;
    Context context;
    ProvinceAdapter provinceAdapter,cityAdapter,localAdapter;
    List<ProvinceBean> provinceBeans = new ArrayList<>();
    List<ProvinceBean> cityBeans = new ArrayList<>();
    List<ProvinceBean> localBeans = new ArrayList<>();
    public static Activity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_hospital);
        init();
        instance = this;
        context = this;
        initList();

    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        province_list = findViewById(R.id.province_list);
        city_list = findViewById(R.id.city_list);
        local_list = findViewById(R.id.local_list);
    }

    public void initList(){
        OkGo.<String>get(Urls.getInstance().GETPROVINCE)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取省列表",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("data");
                                provinceBeans = gson.fromJson(jsonArray.toString(),new TypeToken<List<ProvinceBean>>(){}.getType());
                                provinceAdapter = new ProvinceAdapter(provinceBeans,ChooseHospitalActivity.this,context,1);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                                province_list.setLayoutManager(layoutManager);
                                province_list.setAdapter(provinceAdapter);
                            } else {

                            }
                            Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void onItemClick(View view, int type) {
        int position = province_list.getChildLayoutPosition(view);
        if(type ==1) {
            provinceAdapter.setChecked(position);
            initCityList(provinceBeans.get(position).getId());
        }
        if(type ==2) {
            cityAdapter.setChecked(position);
            initLocalList(cityBeans.get(position).getId());
        }
        if(type ==3) {
            Intent intent =new Intent(this,HospitalListActivity.class);
            intent.putExtra("local_id",localBeans.get(position).getId());
            startActivity(intent);
        }
    }

    @Override
    public void onButtonClick(View view, int position) {

    }

    public void initLocalList(String id){
        OkGo.<String>get(Urls.getInstance().GETCOUNTY)
                .tag(this)
                .params("id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取区县列表",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("data");
                                localBeans = gson.fromJson(jsonArray.toString(),new TypeToken<List<ProvinceBean>>(){}.getType());
                                localAdapter = new ProvinceAdapter(localBeans,ChooseHospitalActivity.this,context,3);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                                local_list.setLayoutManager(layoutManager);
                                local_list.setAdapter(localAdapter);
                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initCityList(String id){
        OkGo.<String>get(Urls.getInstance().GETCITY)
                .tag(this)
                .params("id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取市列表",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("data");
                                cityBeans = gson.fromJson(jsonArray.toString(),new TypeToken<List<ProvinceBean>>(){}.getType());
                                cityAdapter = new ProvinceAdapter(cityBeans,ChooseHospitalActivity.this,context,2);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                                city_list.setLayoutManager(layoutManager);
                                city_list.setAdapter(cityAdapter);
                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
