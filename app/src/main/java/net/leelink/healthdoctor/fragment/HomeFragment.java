package net.leelink.healthdoctor.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.Footer.LoadingView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.MainActivity;
import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.activity.CureListActivity;
import net.leelink.healthdoctor.activity.HomeCureActivity;
import net.leelink.healthdoctor.activity.HospitalCureActivity;
import net.leelink.healthdoctor.activity.PersonalInfoActivity;
import net.leelink.healthdoctor.activity.PhoneListActivity;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.adapter.OrderAdapter;
import net.leelink.healthdoctor.adapter.PatientAdapter;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.bean.OrderBean;
import net.leelink.healthdoctor.bean.UserInfo;
import net.leelink.healthdoctor.im.ChatActivity;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment  extends BaseFragment implements View.OnClickListener , OnOrderListener {
    RecyclerView quest_list;
    PatientAdapter patientAdapter;
    RelativeLayout rl_personal_info,rl_talk_cure,rl_phone,rl_home,rl_hospital;
    private ImageView img_head;
    private TextView tv_name,tv_professional,tv_subject,tv_hospital,tv_score,tv_count,tv_care_count;
    private int page = 1;
    Context context;
    private boolean hasNextPage;
    private TwinklingRefreshLayout refreshLayout;
    List<OrderBean> list = new ArrayList<>();
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = getContext();
        init(view);
        initData();
        initList();
        initRefreshLayout(view);
        return view;
    }

    public void init(View view){
        quest_list = view.findViewById(R.id.quest_list);
        rl_personal_info = view.findViewById(R.id.rl_personal_info);
        rl_personal_info.setOnClickListener(this);
        rl_talk_cure = view.findViewById(R.id.rl_talk_cure);
        rl_talk_cure.setOnClickListener(this);
        rl_phone = view.findViewById(R.id.rl_phone);
        rl_phone.setOnClickListener(this);
        img_head = view.findViewById(R.id.img_head);
        tv_name = view.findViewById(R.id.tv_name);
        tv_professional = view.findViewById(R.id.tv_professional);
        tv_subject = view.findViewById(R.id.tv_subject);
        tv_hospital = view.findViewById(R.id.tv_hospital);
        tv_score = view.findViewById(R.id.tv_score);
        tv_count = view.findViewById(R.id.tv_count);
        tv_care_count = view.findViewById(R.id.tv_care_count);
        rl_home = view.findViewById(R.id.rl_home);
        rl_home.setOnClickListener(this);
        rl_hospital = view.findViewById(R.id.rl_hospital);
        rl_hospital.setOnClickListener(this);

    }
    public void initData(){
        OkGo.<String>get(Urls.getInstance().USERINFO)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取个人信息", json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                MyApplication.userInfo = gson.fromJson(json.toString(), UserInfo.class);
                                Glide.with(context).load(Urls.getInstance().IMG_URL+ MyApplication.userInfo.getImgPath()).into(img_head);
                                tv_name.setText(MyApplication.userInfo.getName());
                                tv_professional.setText(MyApplication.userInfo.getDuties());
                                tv_subject.setText(MyApplication.userInfo.getDepartment());
                                tv_hospital.setText(MyApplication.userInfo.getHospital());
                                tv_score.setText(MyApplication.userInfo.getTotalScore());
                                tv_count.setText(MyApplication.userInfo.getTotalCount());
                                tv_care_count.setText(MyApplication.userInfo.getVisit());
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
                        Toast.makeText(context, "网络不给力呀!", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.rl_personal_info:
//                Intent intent = new Intent(getContext(), PersonalInfoActivity.class);
//                startActivity(intent);
//                break;
            case R.id.rl_talk_cure:
                Intent intent1 = new Intent(getContext(), CureListActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_phone:
                Intent intent2 = new Intent(getContext(), PhoneListActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_home:
                Intent intent3 = new Intent(getContext(), HomeCureActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_hospital:
                Intent intent4 = new Intent(getContext(), HospitalCureActivity.class);
                startActivity(intent4);
                break;
        }
    }

    public void initList(){
        OkGo.<String>get(Urls.getInstance().ORDER)
                .tag(this)
                .headers("token", MyApplication.token)
                .params("pageNum",page)
                .params("pageSize",10)
                .params("state",2)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("订单列表", json.toString());
                            if (json.getInt("status") == 200) {
                                json =  json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                hasNextPage = json.getBoolean("hasNextPage");
                                Gson gson = new Gson();
                                List<OrderBean> orderBeans = gson.fromJson(jsonArray.toString(),new TypeToken<List<OrderBean>>(){}.getType());
                                list.addAll(orderBeans);
                                patientAdapter = new PatientAdapter(list,context,HomeFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                                quest_list.setAdapter(patientAdapter);
                                quest_list.setLayoutManager(layoutManager);
                            } else if(json.getInt("status") == 505){
                                reLogin(context);
                            }else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void onItemClick(View view) {
        int position = quest_list.getChildLayoutPosition(view);
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("clientId",list.get(position).getClientId());
        intent.putExtra("receive_head",list.get(position).getHeadImg());
        intent.putExtra("remark",list.get(position).getRemark());
        intent.putExtra("state",0);
        intent.putExtra("orderId",list.get(position).getOrderId());
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {

    }

    public void initRefreshLayout(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        SinaRefreshView headerView = new SinaRefreshView(context);
        headerView.setTextColor(0xff745D5C);
//        refreshLayout.setHeaderView((new ProgressLayout(getActivity())));
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setBottomView(new LoadingView(context));
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                        list.clear();
                        page = 1;
                        initList();

                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                        if (hasNextPage) {
                            page++;
                            initList();
                        }
                    }
                }, 1000);
            }

        });
        // 是否允许开启越界回弹模式
        refreshLayout.setEnableOverScroll(false);
        //禁用掉加载更多效果，即上拉加载更多
        refreshLayout.setEnableLoadmore(true);
        // 是否允许越界时显示刷新控件
        refreshLayout.setOverScrollRefreshShow(true);


    }

}
