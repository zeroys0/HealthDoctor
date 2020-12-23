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
import net.leelink.healthdoctor.adapter.ChooseAdapter;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.adapter.TeamAdapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.bean.PatientTeam;
import net.leelink.healthdoctor.fragment.TeamFragment;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ChooseGroupActivity extends BaseActivity implements OnOrderListener {
    RelativeLayout rl_back, rl_submit;
    RecyclerView group_list;
    ChooseAdapter chooseAdapter;
    List<PatientTeam> list;
    Context context;
    String groupId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);
        init();
        context = this;
        initList();
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        group_list = findViewById(R.id.group_list);
        rl_submit = findViewById(R.id.rl_submit);
        rl_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(groupId);
            }
        });
    }

    public void initList() {
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
                                list = gson.fromJson(jsonArray.toString(), new TypeToken<List<PatientTeam>>() {
                                }.getType());
                                chooseAdapter = new ChooseAdapter(list, context, ChooseGroupActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                group_list.setAdapter(chooseAdapter);
                                group_list.setLayoutManager(layoutManager);
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

    public void submit(String groupId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("elderlyId", getIntent().getStringExtra("elderlyId"));
            jsonObject.put("groupId", groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>put(Urls.getInstance().FAMILY_GROUP)
                .headers("token", MyApplication.token)
                .upJson(jsonObject)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("修改分组", json.toString());
                            if (json.getInt("status") == 200) {
                                finish();
                                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void onButtonClick(View view, int position) {
        chooseAdapter.setChecked(position);
        chooseAdapter.notifyDataSetChanged();
        groupId = list.get(position).getGroupId();
    }
}
