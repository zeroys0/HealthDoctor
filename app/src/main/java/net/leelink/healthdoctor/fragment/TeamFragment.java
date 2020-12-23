package net.leelink.healthdoctor.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.activity.ManageTeamActivity;
import net.leelink.healthdoctor.activity.PatientListActivity;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.adapter.PatientAdapter;
import net.leelink.healthdoctor.adapter.TeamAdapter;
import net.leelink.healthdoctor.bean.PatientTeam;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TeamFragment extends BaseFragment implements OnOrderListener {
    Context context;
    RecyclerView team_list;
    TeamAdapter teamAdapter;
    List<PatientTeam> list;
    private ImageView img_manage;
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view  =inflater.inflate(R.layout.fragment_message, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = getContext();
        init(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    public void init(View view) {
        team_list = view.findViewById(R.id.team_list);
        img_manage = view.findViewById(R.id.img_manage);
        img_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ManageTeamActivity.class);

                startActivity(intent);
            }
        });
    }

    public void initData() {
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
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<PatientTeam>>(){}.getType());
                                teamAdapter = new TeamAdapter(list,context,TeamFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL, false);
                                team_list.setAdapter(teamAdapter);
                                team_list.setLayoutManager(layoutManager);
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
        int position = team_list.getChildLayoutPosition(view);
        Intent intent = new Intent(getContext(), PatientListActivity.class);
        intent.putExtra("team",list.get(position).getGroupId());
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
