package net.leelink.healthdoctor.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.activity.AppraiseListActivity;
import net.leelink.healthdoctor.activity.CardListActivity;
import net.leelink.healthdoctor.activity.CureSettingActivity;
import net.leelink.healthdoctor.activity.FeedBackActivity;
import net.leelink.healthdoctor.activity.IncomeActivity;
import net.leelink.healthdoctor.activity.PersonalInfoActivity;
import net.leelink.healthdoctor.activity.SettingActivity;
import net.leelink.healthdoctor.activity.SignActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.util.Urls;

public class MineFragment extends BaseFragment implements View.OnClickListener {
    RelativeLayout rl_personal_info,rl_income,rl_account,rl_appraise,rl_cure_setting,rl_oldman,rl_feedback,rl_setting;
    Context context;
    private TextView tv_name,tv_professional,tv_subject,tv_hospital;
    private ImageView img_head;
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = getContext();
        init(view);
        initData();

        return view;
    }
    public void init(View view){
        rl_personal_info = view.findViewById(R.id.rl_personal_info);
        rl_personal_info.setOnClickListener(this);
        rl_income = view.findViewById(R.id.rl_income);
        rl_income.setOnClickListener(this);
        rl_account = view.findViewById(R.id.rl_account);
        rl_account.setOnClickListener(this);
        rl_appraise = view.findViewById(R.id.rl_appraise);
        rl_appraise.setOnClickListener(this);
        rl_cure_setting = view.findViewById(R.id.rl_cure_setting);
        rl_cure_setting.setOnClickListener(this);
        rl_oldman = view.findViewById(R.id.rl_oldman);
        rl_oldman.setOnClickListener(this);
        rl_feedback = view.findViewById(R.id.rl_feedback);
        rl_feedback.setOnClickListener(this);
        img_head = view.findViewById(R.id.img_head);
        tv_name = view.findViewById(R.id.tv_name);
        tv_professional = view.findViewById(R.id.tv_professional);
        tv_subject = view.findViewById(R.id.tv_subject);
        tv_hospital = view.findViewById(R.id.tv_hospital);
        rl_setting = view.findViewById(R.id.rl_setting);
        rl_setting.setOnClickListener(this);
    }

    public void initData(){
        Glide.with(context).load(Urls.getInstance().IMG_URL+ MyApplication.userInfo.getImgPath()).into(img_head);
        tv_name.setText(MyApplication.userInfo.getName());
        tv_professional.setText(MyApplication.userInfo.getDuties());
        tv_subject.setText(MyApplication.userInfo.getDepartment());
        tv_hospital.setText(MyApplication.userInfo.getHospital());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_personal_info:
                Intent intent = new Intent(getContext(), PersonalInfoActivity.class);

                startActivity(intent);
                break;
            case R.id.rl_income:
                Intent intent1 = new Intent(getContext(), IncomeActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_account:
                Intent intent2 = new Intent(getContext(), CardListActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_appraise:
                Intent intent3 = new Intent(getContext(), AppraiseListActivity.class);
                intent3.putExtra("score",MyApplication.userInfo.getTotalScore());
                intent3.putExtra("count",MyApplication.userInfo.getTotalCount());
                startActivity(intent3);
                break;
            case R.id.rl_cure_setting:
                Intent intent4 = new Intent(getContext(), CureSettingActivity.class);
                startActivity(intent4);
                break;
            case R.id.rl_oldman:
                Intent intent5 = new Intent(getContext(), SignActivity.class);
                startActivity(intent5);
                break;
            case R.id.rl_feedback:
                Intent intent6 = new Intent(getContext(), FeedBackActivity.class);
                startActivity(intent6);
                break;
            case R.id.rl_setting:
                Intent intent7 = new Intent(getContext(), SettingActivity.class);
                startActivity(intent7);
                break;

        }
    }
}
