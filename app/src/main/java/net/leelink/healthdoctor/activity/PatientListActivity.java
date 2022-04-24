package net.leelink.healthdoctor.activity;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.adapter.PatientListAdapter;
import net.leelink.healthdoctor.adapter.TeamAdapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.bean.PatientTeam;
import net.leelink.healthdoctor.fragment.TeamFragment;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientListActivity extends BaseActivity implements OnOrderListener {
    private RelativeLayout rl_back;
    private RecyclerView patient_list;
    private PatientListAdapter patientListAdapter;
    private List<PatientTeam.CareOlderVoListBean> list = new ArrayList<>();
    private TextView text_title;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
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
        patient_list = findViewById(R.id.patient_list);
        text_title = findViewById(R.id.text_title);
    }

    public void initList(){

        OkGo.<String>get(Urls.getInstance().FAMILY_GROUP)
                .params("pageNum", 1)
                .params("pageSize", 100)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("查询分组列表", json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                List<PatientTeam> patientTeams = gson.fromJson(jsonArray.toString(),new TypeToken<List<PatientTeam>>(){}.getType());
                                for(int i=0;i<patientTeams.size();i++){
                                    if(patientTeams.get(i).getGroupId().equals(getIntent().getStringExtra("team"))) {
                                       list = patientTeams.get(i).getCareOlderVoList();
                                        patientListAdapter = new PatientListAdapter(list,context,PatientListActivity.this);
                                        text_title.setText(patientTeams.get(i).getGroupName());
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                                        patient_list.setLayoutManager(layoutManager);
                                        patient_list.setAdapter(patientListAdapter);

                                    }
                                }
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
    public void onItemClick(View view) {
        int position  = patient_list.getChildLayoutPosition(view);
        Intent intent = new Intent(this,PatientActivity2.class);
        intent.putExtra("patient",list.get(position));
        intent.putExtra("group",text_title.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
