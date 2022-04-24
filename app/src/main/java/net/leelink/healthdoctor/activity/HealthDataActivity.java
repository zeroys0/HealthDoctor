package net.leelink.healthdoctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.just.agentweb.AgentWeb;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.util.Urls;


public class HealthDataActivity extends BaseActivity {
    private TabLayout tabLayout;
    AgentWeb agentweb;
    LinearLayout ll_data;
    RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_data);
        init();
        initView();
    }

    public void init(){
        String elderlyId = getIntent().getStringExtra("elderlyId");
        ll_data = findViewById(R.id.ll_data);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("血压"));
        tabLayout.addTab(tabLayout.newTab().setText("心率"));
        tabLayout.addTab(tabLayout.newTab().setText("血氧"));
        tabLayout.addTab(tabLayout.newTab().setText("血糖"));
        tabLayout.addTab(tabLayout.newTab().setText("步数"));
        tabLayout.addTab(tabLayout.newTab().setText("血脂四项"));
        tabLayout.addTab(tabLayout.newTab().setText("血尿酸"));
        tabLayout.addTab(tabLayout.newTab().setText("体温"));
        tabLayout.addTab(tabLayout.newTab().setText("体脂"));
        tabLayout.addTab(tabLayout.newTab().setText("肌肉率"));
        tabLayout.addTab(tabLayout.newTab().setText("水分率"));
        tabLayout.addTab(tabLayout.newTab().setText("睡眠指数"));
        tabLayout.addTab(tabLayout.newTab().setText("压力指数"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        setWeb(Urls.getInstance().WEB+"/bloodPressureData/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 1:
                        setWeb(Urls.getInstance().WEB+"/heartRate/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 2:
                        setWeb(Urls.getInstance().WEB+"/bloodOxygen/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 3:
                        setWeb(Urls.getInstance().WEB+"/bloodSugar/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 4:
                        setWeb(Urls.getInstance().WEB+"/stepNumber/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 5:
                        setWeb(Urls.getInstance().WEB+"/bloodFat/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 6:
                        setWeb(Urls.getInstance().WEB+"/bloodUric/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 7:
                        setWeb(Urls.getInstance().WEB+"/temperature/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 8:
                        setWeb(Urls.getInstance().WEB+"/bodyFat/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 9:
                        setWeb(Urls.getInstance().WEB+"/muscleRatio/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 10:
                        setWeb(Urls.getInstance().WEB+"/waterRate/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 11:
                        setWeb(Urls.getInstance().WEB+"/sleepRate/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    case 12:
                        setWeb(Urls.getInstance().WEB+"/pressureRate/"+ elderlyId+"/"+MyApplication.token);
                        break;
                    default:
                        break;
                }
//                initData(type,page);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setWeb(Urls.getInstance().WEB+"/bloodPressureData/"+ elderlyId+"/"+MyApplication.token);


    }

    public void initView(){
        int type = getIntent().getIntExtra("type",0);
        //延时滑动过去
        tabLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                tabLayout.getTabAt(type).select();
            }
        }, 100);
    }


    void setWeb(String url) {
        if (agentweb == null) {
            agentweb = AgentWeb.with(HealthDataActivity.this)
                    .setAgentWebParent(ll_data, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(url);
        } else {
            ll_data.setVisibility(View.GONE);
            agentweb.getWebCreator().getWebView().loadUrl(url);

            ll_data.setVisibility(View.VISIBLE);
        }

    }
}
