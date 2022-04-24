package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.CureHistoryAdapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.bean.HistoryOrder;
import net.leelink.healthdoctor.bean.PatientTeam;
import net.leelink.healthdoctor.im.ChatActivity;
import net.leelink.healthdoctor.util.Urls;
import net.leelink.healthdoctor.view.CircleImageView;

import java.util.List;

public class PatientActivity2 extends BaseActivity implements View.OnClickListener {
    RelativeLayout rl_back,rl_group,rl_chat,rl_info,rl_data,rl_cure_history,rl_check_data,rl_health_data,rl_abnormal;
    TextView text_title;
    Context context;
    private CircleImageView img_head;
    private TextView tv_name,tv_sex,tv_age;
    List<HistoryOrder> list;
    String elderlyId,clientId,name,head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient2);
        init();
        context = this;
        initData();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        text_title = findViewById(R.id.text_title);
        tv_name = findViewById(R.id.tv_name);
        img_head = findViewById(R.id.img_head);
        tv_sex = findViewById(R.id.tv_sex);
        tv_age = findViewById(R.id.tv_age);
        rl_group = findViewById(R.id.rl_group);
        rl_group.setOnClickListener(this);
        rl_chat = findViewById(R.id.rl_chat);
        rl_chat.setOnClickListener(this);
        rl_info = findViewById(R.id.rl_info);
        rl_info.setOnClickListener(this);
        rl_data = findViewById(R.id.rl_data);
        rl_data.setOnClickListener(this);
        rl_cure_history = findViewById(R.id.rl_cure_history);
        rl_cure_history.setOnClickListener(this);
        rl_check_data = findViewById(R.id.rl_check_data);
        rl_check_data.setOnClickListener(this);
        rl_health_data = findViewById(R.id.rl_health_data);
        rl_health_data.setOnClickListener(this);
        rl_abnormal = findViewById(R.id.rl_abnormal);
        rl_abnormal.setOnClickListener(this);

    }

    public void initData(){
        PatientTeam.CareOlderVoListBean patientBean = getIntent().getParcelableExtra("patient");
        text_title.setText(patientBean.getElderlyName());
        Glide.with(context).load(Urls.getInstance().IMG_URL+patientBean.getHeadImgPath()).into(img_head);
        elderlyId=  patientBean.getElderlyId();
        clientId = patientBean.getClientId();
        head = patientBean.getHeadImgPath();
        tv_name.setText(patientBean.getElderlyName());
        if(patientBean.getSex()==1) {
            tv_sex.setText("女");
        }
        tv_age.setText(patientBean.getAge()+"岁");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_group:
                Intent intent = new Intent(this,ChooseGroupActivity.class);
                intent.putExtra("elderlyId",elderlyId);
                startActivity(intent);
                break;
            case R.id.rl_chat:
                Intent intent1= new Intent(this, ChatActivity.class);
                intent1.putExtra("name",text_title.getText().toString());
                intent1.putExtra("receive_head",head);
                intent1.putExtra("clientId",clientId);
                intent1.putExtra("state",1);
                startActivity(intent1);
                break;
            case R.id.rl_info:
                Intent intent2= new Intent(this,PatientInfoActivity.class);
                intent2.putExtra("elderlyId",elderlyId);
                startActivity(intent2);
                break;
            case R.id.rl_data:      //健康数据录入
                Intent intent3 = new Intent(this,WriteDataActivity.class);
                intent3.putExtra("elderlyId",elderlyId);
                startActivity(intent3);
                break;
            case R.id.rl_cure_history:      //问诊记录
                Intent intent4 = new Intent(this,CureHistoryActivity.class);
                intent4.putExtra("elderlyId",elderlyId);
                startActivity(intent4);
                break;
            case R.id.rl_check_data:    //体检数据
                Intent intent5 = new Intent(this, WebActivity.class);
                intent5.putExtra("url",Urls.getInstance().WEB+"/hsRecord/"+elderlyId+"/"+ MyApplication.token);
                intent5.putExtra("title","体检数据");
                startActivity(intent5);
                break;
            case R.id.rl_health_data:   //健康数据
                Intent intent6 = new Intent(this,HealthDataActivity.class);
                intent6.putExtra("elderlyId",elderlyId);
                startActivity(intent6);
                break;
            case R.id.rl_abnormal:  //异常数据
                Intent intent7 = new Intent(this,WebActivity.class);
                intent7.putExtra("title","异常数据");
                intent7.putExtra("url",Urls.getInstance().WEB+"/dataAbort/"+elderlyId+"/"+MyApplication.token);
                startActivity(intent7);
                break;
        }
    }
}
