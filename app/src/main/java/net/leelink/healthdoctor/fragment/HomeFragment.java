package net.leelink.healthdoctor.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.activity.CureListActivity;
import net.leelink.healthdoctor.activity.PersonalInfoActivity;
import net.leelink.healthdoctor.adapter.PatientAdapter;
import net.leelink.healthdoctor.im.ChatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment  extends BaseFragment implements View.OnClickListener {
    RecyclerView quest_list;
    PatientAdapter patientAdapter;
    RelativeLayout rl_personal_info,rl_talk_cure,rl_phone;

    Context context;
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = getContext();
        init(view);
        initData();

        return view;
    }

    public void init(View view){
        quest_list = view.findViewById(R.id.quest_list);
        rl_personal_info = view.findViewById(R.id.rl_personal_info);
        rl_personal_info.setOnClickListener(this);
        rl_talk_cure = view.findViewById(R.id.rl_talk_cure);
        rl_talk_cure.setOnClickListener(this);
        rl_phone = view.findViewById(R.id.rl_phone);
        rl_phone.setOnClickListener(this);
    }
    public void initData(){
        patientAdapter = new PatientAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        quest_list.setAdapter(patientAdapter);
        quest_list.setLayoutManager(layoutManager);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_personal_info:
                Intent intent = new Intent(getContext(), PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_talk_cure:
                Intent intent1 = new Intent(getContext(), CureListActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_phone:
                Intent intent2 = new Intent(getContext(), ChatActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
