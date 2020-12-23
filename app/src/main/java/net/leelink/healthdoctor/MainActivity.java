package net.leelink.healthdoctor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
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
import net.leelink.healthdoctor.im.data.MessageDataHelper;
import net.leelink.healthdoctor.im.websocket.JWebSocketClient;
import net.leelink.healthdoctor.im.websocket.JWebSocketClientService;
import net.leelink.healthdoctor.util.Urls;
import net.leelink.healthdoctor.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    BottomNavigationBar nv_bottom;
    FragmentManager fm;
    HomeFragment homeFragment;
    MessageFragment messageFragment;
    Context context;
    MineFragment mineFragment;
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        context = this;
        SharedPreferences sp = getSharedPreferences("sp",0);

        startJWebSClientService();
        bindService();
        get_offLine_chat();
        checkNotification(context);
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

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(context, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(context, JWebSocketClientService.class);
        startService(intent);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "服务与活动成功断开");
        }
    };

    public void get_offLine_chat(){
        SharedPreferences sp = getSharedPreferences("sp",0);
        MessageDataHelper messageDataHelper = new MessageDataHelper(this);
        final SQLiteDatabase db = messageDataHelper.getReadableDatabase();
        final String clientId = sp.getString("clientId","");
        Log.e( "get_offLine_chat: ",Urls.getInstance().HISTORY+"/"+clientId+"/0" );
        OkGo.<String>get(Urls.getInstance().HISTORY+"/"+clientId+"/0")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取未读消息", json.toString());
                            if (json.getInt("status") == 200) {
                                JSONArray jsonArray = json.getJSONArray("data");
                                JSONObject jsonObject;
                                for(int i=0;i<jsonArray.length();i++){
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String s = jsonObject.getString("content");
                                    jsonObject = new JSONObject(s);
                                    if(jsonObject.getInt("messageType")==4) {
                                        ContentValues cv = new ContentValues();
                                        cv.put("content", jsonObject.getString("textMessage"));
                                        cv.put("time", System.currentTimeMillis() + "");
                                        cv.put("isMeSend", 0);
                                        cv.put("isRead", 1);
                                        cv.put("sendId", clientId);
                                        cv.put("receiveId", jsonObject.getString("fromuserId"));
                                        cv.put("type", jsonObject.getInt("type"));
                                        cv.put("RecorderTime", 0);
                                        db.insert("MessageDataBase", null, cv);
                                        db.close();
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    /**
     * 检测是否开启通知
     *
     * @param context
     */
    private void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }

    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    private void setNotification(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

    /**
     * 获取通知权限,监测是否开启了系统通知
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
