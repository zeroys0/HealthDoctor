package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.Pager2Adapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.fragment.CureDoneFragment;
import net.leelink.healthdoctor.fragment.CureFragment;
import net.leelink.healthdoctor.fragment.PictureHistoryFragment;
import net.leelink.healthdoctor.fragment.UnCureFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAvtivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_date;
    private RelativeLayout rl_back;
    private ViewPager2 view_pager;
    private List<Fragment> fragments;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_avtivity);
        init();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("图文问诊"));
        tabLayout.addTab(tabLayout.newTab().setText("电话问诊"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                view_pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        view_pager = findViewById(R.id.view_pager);
        fragments = new ArrayList<>();
        fragments.add(new PictureHistoryFragment());
        fragments.add(new PictureHistoryFragment());

        view_pager.setAdapter(new Pager2Adapter(OrderHistoryAvtivity.this,fragments));
        view_pager.setCurrentItem(0);
        view_pager.setUserInputEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
        }
    }
}
