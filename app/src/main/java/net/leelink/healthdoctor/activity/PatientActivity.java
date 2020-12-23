package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.CureHistoryAdapter;
import net.leelink.healthdoctor.adapter.PatientListAdapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.bean.HistoryOrder;
import net.leelink.healthdoctor.bean.PatientTeam;
import net.leelink.healthdoctor.util.Urls;
import net.leelink.healthdoctor.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PatientActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout rl_back,rl_group;
    TextView text_title;
    Context context;
    private CircleImageView img_head;
    private TextView tv_name,tv_sex,tv_age,tv_group_name;
    List<HistoryOrder> list;
    CureHistoryAdapter cureHistoryAdapter;
    RecyclerView cure_history;
    String elderlyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        init();
        context = this;
        initData();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        text_title = findViewById(R.id.text_title);
        tv_name = findViewById(R.id.tv_name);
        img_head = findViewById(R.id.img_head);
        tv_sex = findViewById(R.id.tv_sex);
        tv_age = findViewById(R.id.tv_age);
        tv_group_name = findViewById(R.id.tv_group_name);
        rl_group = findViewById(R.id.rl_group);
        rl_group.setOnClickListener(this);
        cure_history = findViewById(R.id.cure_history);


    }

    public void initData(){
        PatientTeam.CareOlderVoListBean patientBean = getIntent().getParcelableExtra("patient");
        text_title.setText(patientBean.getElderlyName());
        Glide.with(context).load(Urls.getInstance().IMG_URL+patientBean.getHeadImgPath()).into(img_head);
        elderlyId=  patientBean.getElderlyId();
        tv_name.setText(patientBean.getElderlyName());
        if(patientBean.getSex()==1) {
            tv_sex.setText("女");
        }
        tv_age.setText(patientBean.getAge()+"岁");
        tv_group_name.setText(getIntent().getStringExtra("group"));
        initList(patientBean.getElderlyId());
    }

    public void initList(String id){
        OkGo.<String>get(Urls.getInstance().HEALTH_ORDER)
                .params("pageNum", 1)
                .params("pageSize", 100)
                .params("elderlyId",id)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("查询问诊记录", json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<HistoryOrder>>(){}.getType());
                                cureHistoryAdapter = new CureHistoryAdapter(list,context);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                                cure_history.setAdapter(cureHistoryAdapter);
                                cure_history.setLayoutManager(layoutManager);

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

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_group:
                Intent intent = new Intent(this,ChooseGroupActivity.class);
                intent.putExtra("elderlyId",elderlyId);
                startActivity(intent);
                break;
        }
    }
}
