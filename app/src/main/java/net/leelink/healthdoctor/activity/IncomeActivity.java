package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.IncomeAdapter;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.adapter.OrderAdapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.bean.IncomeBean;
import net.leelink.healthdoctor.bean.OrderBean;
import net.leelink.healthdoctor.fragment.CureFragment;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.chrono.ChronoLocalDate;
import java.util.List;

public class IncomeActivity extends BaseActivity implements View.OnClickListener, OnOrderListener {
    RelativeLayout rl_back;
    RecyclerView income_list;
    Context context;
    private TextView tv_detail,tv_ex_time,tv_num,tv_total_income;
    IncomeAdapter incomeAdapter;
    List<IncomeBean> list;
    private Button btn_draw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        init();
        initList();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        income_list = findViewById(R.id.income_list);
        context = this;
        tv_detail = findViewById(R.id.tv_detail);
        tv_detail.setOnClickListener(this);
        tv_ex_time = findViewById(R.id.tv_ex_time);
        tv_num = findViewById(R.id.tv_num);
        tv_total_income = findViewById(R.id.tv_total_income);
        btn_draw = findViewById(R.id.btn_draw);
        btn_draw.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_detail:
                Intent intent = new Intent(this,OrderHistoryAvtivity.class);
                startActivity(intent);
                break;
            case R.id.btn_draw:
                Intent intent1 = new Intent(this,CardListActivity.class);
                startActivity(intent1);
                break;
        }
    }

    public void initList(){
        OkGo.<String>get(Urls.getInstance().ACCOUNT)
                .tag(this)
                .headers("token", MyApplication.token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("查询收入", json.toString());
                            if (json.getInt("status") == 200) {
                                json =  json.getJSONObject("data");
                                tv_ex_time.setText(json.getString("wallet"));
                                JSONObject jb = json.getJSONObject("cumulativeData");
                                tv_num.setText(jb.getString("orderCount"));
                                tv_total_income.setText(jb.getString("orderTotalAmount"));
                                JSONArray jsonArray = json.getJSONArray("accountResultList");
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<IncomeBean>>(){}.getType());

                                incomeAdapter = new IncomeAdapter(list,context);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                                income_list.setLayoutManager(layoutManager);
                                income_list.setAdapter(incomeAdapter);
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

    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
