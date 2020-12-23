package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.ManageTeamAdapter;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.adapter.OnSignListener;
import net.leelink.healthdoctor.adapter.TeamAdapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.bean.PatientTeam;
import net.leelink.healthdoctor.fragment.TeamFragment;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ManageTeamActivity extends BaseActivity implements OnSignListener {
    private RecyclerView team_list;
    private ManageTeamAdapter manageTeamAdapter;
    List<PatientTeam> list;
    Context context;
    Button btn_add;
    RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_team);
        init();
        context = this;
        createProgressBar(this);
        initList();
    }

    public void init(){
        team_list = findViewById(R.id.team_list);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundAlpha(0.5f);
                showPopup();
            }
        });
    }

    public void initList(){
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
                                manageTeamAdapter = new ManageTeamAdapter(list,context, ManageTeamActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL, false);
                                team_list.setAdapter(manageTeamAdapter);
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


    }

    @Override
    public void onButtonClick(View view, int position) {
        deleteTeam(position);
    }

    @Override
    public void onButtonCancel(View view, int position) {
        backgroundAlpha(0.5f);
        editPop(position);
    }

    @SuppressLint("WrongConstant")
    public void showPopup(){
        View popview = LayoutInflater.from(ManageTeamActivity.this).inflate(R.layout.pop_add_team, null);
        final EditText ed_name = popview.findViewById(R.id.ed_name);
        Button btn_confirm = popview.findViewById(R.id.btn_confirm);
        final PopupWindow popuPhoneW = new PopupWindow(popview,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popuPhoneW.setFocusable(true);
        popuPhoneW.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popuPhoneW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popuPhoneW.setOutsideTouchable(true);
        popuPhoneW.setBackgroundDrawable(new BitmapDrawable());
        popuPhoneW.setOnDismissListener(new ManageTeamActivity.poponDismissListener());
        popuPhoneW.showAtLocation(rl_back, Gravity.CENTER, 0, 0);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ed_name.getText().toString().equals("")) {
                    String s = ed_name.getText().toString().trim();
                    addTeam(s);
                    popuPhoneW.dismiss();
                }
            }
        });
    }
    @SuppressLint("WrongConstant")
    public void editPop(final int position){
        View popview = LayoutInflater.from(ManageTeamActivity.this).inflate(R.layout.pop_add_team, null);
        TextView tv_title = popview.findViewById(R.id.tv_title);
        tv_title.setText("编辑分组");
        final EditText ed_name = popview.findViewById(R.id.ed_name);
        Button btn_confirm = popview.findViewById(R.id.btn_confirm);
        final PopupWindow popuPhoneW = new PopupWindow(popview,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popuPhoneW.setFocusable(true);
        popuPhoneW.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popuPhoneW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popuPhoneW.setOutsideTouchable(true);
        popuPhoneW.setBackgroundDrawable(new BitmapDrawable());
        popuPhoneW.setOnDismissListener(new ManageTeamActivity.poponDismissListener());
        popuPhoneW.showAtLocation(rl_back, Gravity.CENTER, 0, 0);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ed_name.getText().toString().equals("")) {
                    String s = ed_name.getText().toString().trim();
                    editTeam(list.get(position).getGroupId(),s);
                    popuPhoneW.dismiss();
                }
            }
        });
    }

    public void addTeam(String name){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", MyApplication.userInfo.getId());
            jsonObject.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showProgressBar();
        OkGo.<String>post(Urls.getInstance().FAMILY_GROUP)
                .headers("token",MyApplication.token)
                .upJson(jsonObject)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("添加分组", json.toString());
                            if (json.getInt("status") == 200) {
                               initList();
                                Toast.makeText(context, "添加分组成功", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "网络不给力啊", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void editTeam(String id ,String name){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e( "editTeam: ",jsonObject.toString() );
        OkGo.<String>put(Urls.getInstance().GROUP_NAME)
                .headers("token",MyApplication.token)
                .upJson(jsonObject)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("编辑分组", json.toString());
                            if (json.getInt("status") == 200) {
                                initList();
                                Toast.makeText(context, "编辑成功", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "网络不给力啊", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteTeam(int position){

        showProgressBar();
        OkGo.<String>delete(Urls.getInstance().FAMILY_GROUP+"/"+list.get(position).getGroupId())
                .headers("token",MyApplication.token)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("删除分组", json.toString());
                            if (json.getInt("status") == 200) {
                                initList();
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "网络不给力啊", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            // Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }
    }
}
