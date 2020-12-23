package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.bean.IncomeBean;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ConfirmWithdrawActivity extends BaseActivity {
    RelativeLayout rl_back;
    private EditText ed_amount;
    private Button btn_confirm;
    Context context;
    private TextView tv_allin,tv_name;
    String all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_withdraw);
        init();
        context = this;
        initData();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_confirm = findViewById(R.id.btn_confirm);
        ed_amount = findViewById(R.id.ed_amount);
        ed_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        ed_amount.setText(s);
                        ed_amount.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    ed_amount.setText(s);
                    ed_amount.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        ed_amount.setText(s.subSequence(0, 1));
                        ed_amount.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withDraw();
            }
        });
        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(getIntent().getStringExtra("bank_name"));
        tv_allin = findViewById(R.id.tv_allin);
        tv_allin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_amount.setText(all);
            }
        });

    }

    public void initData(){
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
                                all = json.getString("wallet");
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

    public void withDraw(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("amount",ed_amount.getText().toString());
            jsonObject.put("bankId",getIntent().getStringExtra("id'"));
            jsonObject.put("card",getIntent().getStringExtra("card"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<String>post(Urls.getInstance().DOCTER_TX)
                .tag(this)
                .headers("token", MyApplication.token)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("提现", json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(context, "提现成功", Toast.LENGTH_SHORT).show();
                                finish();
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
}
