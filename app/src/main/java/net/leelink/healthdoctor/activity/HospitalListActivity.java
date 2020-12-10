package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import net.leelink.healthdoctor.adapter.HospitalAdapter;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.adapter.ProvinceAdapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.bean.HospitalBean;
import net.leelink.healthdoctor.bean.ProvinceBean;
import net.leelink.healthdoctor.util.Urls;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HospitalListActivity extends BaseActivity implements OnOrderListener {
    RecyclerView hospital_list;
    RelativeLayout rl_back;
    HospitalAdapter hospitalAdapter;
    List<HospitalBean> list = new ArrayList<>();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);
        init();
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
        hospital_list = findViewById(R.id.hospital_list);

    }

    public void initList(){
        OkGo.<String>get(Urls.HOSPITAL)
                .tag(this)
                .params("areaNo",getIntent().getStringExtra("local_id"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取医院列表",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("data");
                                list  = gson.fromJson(jsonArray.toString(),new TypeToken<List<HospitalBean>>(){}.getType());
                                hospitalAdapter = new HospitalAdapter(list,HospitalListActivity.this,context);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                                hospital_list.setAdapter(hospitalAdapter);
                                hospital_list.setLayoutManager(layoutManager);
                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view) {
        int position = hospital_list.getChildLayoutPosition(view);
        EventBus.getDefault().post(list.get(position));
        finish();
        ChooseHospitalActivity.instance.finish();
    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
