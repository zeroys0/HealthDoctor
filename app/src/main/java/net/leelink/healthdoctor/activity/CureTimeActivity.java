package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.DateListAdapter;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class CureTimeActivity extends BaseActivity implements View.OnClickListener, OnOrderListener {
    TextView tv_confirm, tv_1, tv_2, tv_3, tv_4, tv_5, tv_6, tv_7;
    AppCompatCheckBox time_9, time_10, time_11, time_12, time_14, time_15, time_16, time_17, time_18, time_19, time_20, time_21, time_22, time_23;
    RelativeLayout rl_back;
    private static String mYear; // 当前年
    private static String mMonth; // 月
    private static String mDay;
    private static String mWay;
    RecyclerView data_list;
    DateListAdapter dateListAdapter;
    Context context;
    JSONObject time_json = new JSONObject();
    JSONArray time_array = new JSONArray();
    Map<Integer, AppCompatCheckBox> map = new HashMap<>();
    List<String> dates = get7date(14);
    private String c_day;
    List<Integer> hour = new ArrayList<>();
    boolean first = false;
    private String time_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cure_time);
        init();
        context = this;
        initView();
    }

    public void init() {
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(this);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        tv_4 = findViewById(R.id.tv_4);
        tv_5 = findViewById(R.id.tv_5);
        tv_6 = findViewById(R.id.tv_6);
        tv_7 = findViewById(R.id.tv_7);
        data_list = findViewById(R.id.data_list);
        time_9 = findViewById(R.id.time_9);
        map.put(9, time_9);
        time_9.setOnClickListener(this);
        time_10 = findViewById(R.id.time_10);
        map.put(10, time_10);
        time_10.setOnClickListener(this);
        time_11 = findViewById(R.id.time_11);
        map.put(11, time_11);
        time_11.setOnClickListener(this);
        time_12 = findViewById(R.id.time_12);
        map.put(12, time_12);
        time_12.setOnClickListener(this);
        time_14 = findViewById(R.id.time_14);
        map.put(14, time_14);
        time_14.setOnClickListener(this);
        time_15 = findViewById(R.id.time_15);
        map.put(15, time_15);
        time_15.setOnClickListener(this);
        time_16 = findViewById(R.id.time_16);
        map.put(16, time_16);
        time_16.setOnClickListener(this);
        time_17 = findViewById(R.id.time_17);
        map.put(17, time_17);
        time_17.setOnClickListener(this);
        time_18 = findViewById(R.id.time_18);
        map.put(18, time_18);
        time_18.setOnClickListener(this);
        time_19 = findViewById(R.id.time_19);
        map.put(19, time_19);
        time_19.setOnClickListener(this);
        time_20 = findViewById(R.id.time_20);
        map.put(20, time_20);
        time_20.setOnClickListener(this);
        time_21 = findViewById(R.id.time_21);
        map.put(21, time_21);
        time_21.setOnClickListener(this);
        time_22 = findViewById(R.id.time_22);
        map.put(22, time_22);
        time_22.setOnClickListener(this);
        time_23 = findViewById(R.id.time_23);
        map.put(23, time_23);
        time_23.setOnClickListener(this);
    }

    public void initView() {
        List<String> list = get7week();
        tv_1.setText(list.get(1));
        tv_2.setText(list.get(2));
        tv_3.setText(list.get(3));
        tv_4.setText(list.get(4));
        tv_5.setText(list.get(5));
        tv_6.setText(list.get(6));
        tv_7.setText(list.get(7));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 7, RecyclerView.VERTICAL, false);

        dates.remove(0);
        dateListAdapter = new DateListAdapter(dates, CureTimeActivity.this, context);
        data_list.setLayoutManager(layoutManager);
        data_list.setAdapter(dateListAdapter);

        OkGo.<String>get(Urls.RECEPTION + "/" + MyApplication.userInfo.getId() + "/" + getIntent().getIntExtra("type", 0))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取排班时间", json.toString());
                            if (json.getInt("status") == 200) {
                                if(json.getString("data").equals("null")) {
                                    first= true;
                                } else {
                                    json = json.getJSONObject("data");
                                    time_id = json.getString("id");
                                    JSONObject jsonObject = new JSONObject(json.getString("times"));
                                    time_array = jsonObject.getJSONArray("timeVo");
                                }
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
                        stopProgressBar();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                confirm();
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.time_9:
                if (time_9.isChecked()) {
                    setTime(9);
                } else {
                    deleteTime(9);
                }
                break;
            case R.id.time_10:
                if (time_10.isChecked()) {
                    setTime(10);
                } else {
                    deleteTime(10);
                }
                break;
            case R.id.time_11:
                if (time_11.isChecked()) {
                    setTime(11);
                } else {
                    deleteTime(11);
                }
                break;
            case R.id.time_12:
                if (time_12.isChecked()) {
                    setTime(12);
                } else {
                    deleteTime(12);
                }
                break;
            case R.id.time_14:
                if (time_14.isChecked()) {
                    setTime(14);
                } else {
                    deleteTime(14);
                }
                break;
            case R.id.time_15:
                if (time_15.isChecked()) {
                    setTime(15);
                } else {
                    deleteTime(15);
                }
                break;
            case R.id.time_16:
                if (time_16.isChecked()) {
                    setTime(16);
                } else {
                    deleteTime(16);
                }
                break;
            case R.id.time_17:
                if (time_17.isChecked()) {
                    setTime(17);
                } else {
                    deleteTime(17);
                }
                break;
            case R.id.time_18:
                if (time_18.isChecked()) {
                    setTime(18);
                } else {
                    deleteTime(18);
                }
                break;
            case R.id.time_19:
                if (time_19.isChecked()) {
                    setTime(19);
                } else {
                    deleteTime(19);
                }
                break;
            case R.id.time_20:
                if (time_20.isChecked()) {
                    setTime(20);
                } else {
                    deleteTime(20);
                }
                break;
            case R.id.time_21:
                if (time_21.isChecked()) {
                    setTime(21);
                } else {
                    deleteTime(21);
                }
                break;
            case R.id.time_22:
                if (time_22.isChecked()) {
                    setTime(22);
                } else {
                    deleteTime(22);
                }
                break;
            case R.id.time_23:
                if (time_23.isChecked()) {
                    setTime(23);
                } else {
                    deleteTime(23);
                }
                break;
        }
    }

    public void confirm() {
        JSONObject jsonObject = new JSONObject();

        try {
            time_json.put("timeVo", time_array);
            jsonObject.put("type", getIntent().getIntExtra("type", 0));
            if(!first) {
                jsonObject.put("id", time_id);
            }
            jsonObject.put("times", time_json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("confirm: ", jsonObject.toString());
        OkGo.<String>post(Urls.RECEPTION)
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("设置排班时间", json.toString());
                            if (json.getInt("status") == 200) {
                                finish();
                                Toast.makeText(context, "设置完成", Toast.LENGTH_LONG).show();
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
                        stopProgressBar();
                    }
                });
    }


    //选中时间
    public void setTime(int t) {
        JSONObject json;
        hour.add(t);
        String time = getTime(hour);
        boolean has = false;

        for (int i = 0; i < time_array.length(); i++) {
            try {
                if (time_array.getJSONObject(i).getString("day").equals(c_day)) {
                    has = true;
                    json = time_array.getJSONObject(i);
                    json.put("day", c_day);
                    json.put("time", time);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (has) {

        } else {
            json = new JSONObject();
            try {
                json.put("day", c_day);
                json.put("time", time);
                time_array.put(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //删除时间
    public void deleteTime(int t) {
        JSONObject json;
        for (int i = 0; i < hour.size(); i++) {
            if (hour.get(i) == t) {
                hour.remove(i);
            }
        }
        String time = "";
        if(hour.size()>0) {
            time = getTime(hour);
        }
        boolean has = false;

        for (int i = 0; i < time_array.length(); i++) {
            try {
                if (time_array.getJSONObject(i).getString("day").equals(c_day)) {
                    has = true;
                    json = time_array.getJSONObject(i);
                    json.put("day", c_day);
                    json.put("time", time);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (has) {

        } else {
            json = new JSONObject();
            try {
                json.put("day", c_day);
                json.put("time", time);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public String getTime(List<Integer> list) {
        String s = "";
        for (Integer i : list) {
            s = s + i + ",";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }

    /**
     * 获取当前年月日
     *
     * @return
     */
    public static String StringData() {


        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if (Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth)))) {
            mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth))));
        }
        return mYear + "-" + (mMonth.length() == 1 ? "0" + mMonth : mMonth) + "-" + (mDay.length() == 1 ? "0" + mDay : mDay);
    }

    /**
     * 得到当年当月的最大日期
     **/
    public static int MaxDayFromDay_OF_MONTH(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        return day;
    }

    /**
     * 根据当前日期获得是星期几
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {


            c.setTime(format.parse(time));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }
        return Week;
    }

    /**
     * 获取今天往后一周的集合
     */
    public static List<String> get7week() {
        String week = "";
        List<String> weeksList = new ArrayList<String>();
        List<String> dateList = get7date(7);
        for (String s : dateList) {
            if (s.equals(StringData())) {
                week = "今天";
            } else {
                week = getWeek(s);
            }
            weeksList.add(week);
        }
        return weeksList;
    }

    /**
     * 获取未来n天的日期
     */
    public static List<String> get7date(int d) {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < d; i++) {
            c.add(java.util.Calendar.DAY_OF_MONTH, 1);
            date = sim.format(c.getTime());
            dates.add(date);
        }
        return dates;
    }

    @Override
    public void onItemClick(View view) {
        int position = data_list.getChildLayoutPosition(view);
        dateListAdapter.setChcek(position);
        c_day = dates.get(position);
        hour.clear();
        setView(dates.get(position));
    }

    @Override
    public void onButtonClick(View view, int position) {

    }

    public void setView(String time){
        JSONObject json;
        clear();
        for(int i=0;i<time_array.length();i++){
            try {
                json = time_array.getJSONObject(i);
                if(time.equals(json.getString("day"))){
                    String s = json.getString("time");

                    String[] t = s.split(",");
                    for(int a=0;a<t.length;a++){
                        hour.add(Integer.valueOf(t[a]));
                        map.get(Integer.valueOf(t[a])).setChecked(true);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void clear(){
        map.get(9).setChecked(false);
        map.get(10).setChecked(false);
        map.get(11).setChecked(false);
        map.get(12).setChecked(false);
        map.get(14).setChecked(false);
        map.get(15).setChecked(false);
        map.get(16).setChecked(false);
        map.get(17).setChecked(false);
        map.get(18).setChecked(false);
        map.get(19).setChecked(false);
        map.get(20).setChecked(false);
        map.get(21).setChecked(false);
        map.get(22).setChecked(false);
        map.get(23).setChecked(false);
    }
}
