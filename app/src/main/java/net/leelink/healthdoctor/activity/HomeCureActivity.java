package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.Pager2Adapter;
import net.leelink.healthdoctor.app.BaseActivity;

import java.util.List;

public class HomeCureActivity extends BaseActivity {
    RelativeLayout rl_back;
    private ViewPager2 view_pager;
    private List<Fragment> fragments;
    TabLayout tabLayout;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cure);
        context = this;
        init();
    }
    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("待接诊"));
        tabLayout.addTab(tabLayout.newTab().setText("接诊中"));
        tabLayout.addTab(tabLayout.newTab().setText("已结束"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

//                view_pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        view_pager = findViewById(R.id.view_pager);
//        fragments = new ArrayList<>();
//        fragments.add(new UnCureFragment());
//        fragments.add(new CureFragment());
//        fragments.add(new CureDoneFragment());

        view_pager.setAdapter(new Pager2Adapter(HomeCureActivity.this,fragments));
        view_pager.setCurrentItem(0);
        view_pager.setUserInputEnabled(false);
    }
}
