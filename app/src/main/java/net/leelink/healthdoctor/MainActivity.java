package net.leelink.healthdoctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.activity.LoginActivity;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.bean.UserInfo;
import net.leelink.healthdoctor.fragment.HomeFragment;
import net.leelink.healthdoctor.fragment.MessageFragment;
import net.leelink.healthdoctor.fragment.MineFragment;
import net.leelink.healthdoctor.util.Urls;
import net.leelink.healthdoctor.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    BottomNavigationBar nv_bottom;
    FragmentManager fm;
    HomeFragment homeFragment;
    MessageFragment messageFragment;
    Context context;
    MineFragment mineFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        context = this;
        initData();
    }

    public  void init(){
        nv_bottom = findViewById(R.id.nv_bottom);
        setBottomNavigationItem(nv_bottom, 15, 26, 10);
        nv_bottom.setTabSelectedListener(MainActivity.this);
        nv_bottom.setMode(BottomNavigationBar.MODE_FIXED);
        nv_bottom.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        nv_bottom.setBarBackgroundColor(R.color.white);
        nv_bottom
                .addItem(new BottomNavigationItem(R.drawable.home_selected, "首页").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.drawable.message_selected, "患者").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.drawable.mine_selected, "我的").setActiveColorResource(R.color.blue))
                .setFirstSelectedPosition(0)
                .initialise();
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        homeFragment = (HomeFragment) fm.findFragmentByTag("home");
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        ft.add(R.id.fragment_view, homeFragment, "home");
        ft.commit();
    }

    public void initData(){
        OkGo.<String>get(Urls.USERINFO)
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
                            } else {
                                Toast.makeText(MainActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(MainActivity.this, "网络不给力呀!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar, int space, int imgLen, int textSize) {
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mTabContainer")) {
                try {
                    //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
                        //获取到容器内的各个Tab
                        View view = mTabContainer.getChildAt(j);
                        //获取到Tab内的各个显示控件
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(56));
                        FrameLayout container = (FrameLayout) view.findViewById(R.id.fixed_bottom_navigation_container);
                        container.setLayoutParams(params);
                        container.setPadding(dip2px(12), dip2px(0), dip2px(12), dip2px(0));

                        //获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                        labelView.setIncludeFontPadding(false);
                        labelView.setPadding(0, 0, 0, dip2px(20 - textSize - space / 2));

                        //获取到Tab内的图像控件
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                        params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
                        params.setMargins(0, 0, 0, space / 2);
                        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onTabSelected(int position) {
        FragmentTransaction ft = getFragmentTransaction();
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    ft.add(R.id.fragment_view, new HomeFragment(), "home");
                } else {
                    ft.show(homeFragment);
                }
                Utils.setStatusTextColor(true, MainActivity.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case 1:
                if (messageFragment == null) {
                    ft.add(R.id.fragment_view, new MessageFragment(), "device");
                } else {
                    ft.show(messageFragment);
                }
                Utils.setStatusTextColor(true, MainActivity.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                if(messageFragment!= null) {
                    messageFragment.onResume();
                }
                break;
            case 2:
                if (mineFragment == null) {
                    ft.add(R.id.fragment_view, new MineFragment(), "mine");
                } else {
                    ft.show(mineFragment);
                }
                Utils.setStatusTextColor(true, MainActivity.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    protected FragmentTransaction getFragmentTransaction() {
        // TODO Auto-generated method stub
        FragmentManager fm = getSupportFragmentManager();
        homeFragment = (HomeFragment) fm.findFragmentByTag("home");
        messageFragment = (MessageFragment) fm.findFragmentByTag("device");
        mineFragment = (MineFragment) fm.findFragmentByTag("mine");
        FragmentTransaction ft = fm.beginTransaction();
        /** 如果存在hide掉 */
        if (homeFragment != null)
            ft.hide(homeFragment);
        if (messageFragment != null)
            ft.hide(messageFragment);
        if (mineFragment != null)
            ft.hide(mineFragment);
        return ft;
    }
}
