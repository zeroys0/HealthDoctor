package net.leelink.healthdoctor.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.Footer.LoadingView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.activity.OrderDetailActivity;
import net.leelink.healthdoctor.adapter.HistoryOrderAdapter;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.adapter.SignListAdapter;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.bean.HistoryOrderBean;
import net.leelink.healthdoctor.bean.SignBean;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PictureHistoryFragment extends BaseFragment implements OnOrderListener {
    Context context;
    private TwinklingRefreshLayout refreshLayout;
    int page = 1;
    boolean hasNextPage;
    List<HistoryOrderBean> list = new ArrayList<>();
    HistoryOrderAdapter historyOrderAdapter;
    RecyclerView order_list;
    private TextView tv_date;
    private TimePickerView pvTime;
    private SimpleDateFormat sdf;
    String myDate = "";
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_history, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = getContext();
        init(view);
        initRefreshLayout(view);
        initPickerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        initList();
    }

    public void init(View view){
        tv_date = view.findViewById(R.id.tv_date);
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
        order_list = view.findViewById(R.id.order_list);
    }

    public void initList(){

        OkGo.<String>get(Urls.getInstance().ORDER)
                .tag(this)
                .headers("token", MyApplication.token)
                .params("pageNum",page)
                .params("pageSize",10)
                .params("time",myDate)
                .params("state",4)
                .params("type",2)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("医单列表", json.toString());
                            if (json.getInt("status") == 200) {
                                json =  json.getJSONObject("data");
                                hasNextPage = json.getBoolean("hasNextPage");
                                JSONArray jsonArray = json.getJSONArray("list");
                                Gson  gson = new Gson();
                                List<HistoryOrderBean> historyOrderBeans = gson.fromJson(jsonArray.toString(),new TypeToken<List<HistoryOrderBean>>(){}.getType());
                                list.addAll(historyOrderBeans);
                                historyOrderAdapter = new HistoryOrderAdapter(list,context,PictureHistoryFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                                order_list.setAdapter(historyOrderAdapter);
                                order_list.setLayoutManager(layoutManager);
                            } else if(json.getInt("status") == 505){
                                reLogin(context);
                            }else {
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
                    }
                });
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

    private void initPickerView(){
        sdf = new SimpleDateFormat("yyyy-MM");
        boolean[] type = {true, true, false, false, false, false};
        pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tv_date.setText(sdf.format(date));
                myDate = sdf.format(date);
                initList();
            }
        }).setType(type).build();
    }


    @Override
    public void onItemClick(View view) {
        int position = order_list.getChildLayoutPosition(view);
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("order",list.get(position));
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
