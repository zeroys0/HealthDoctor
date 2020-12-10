package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.OnLocalListener;
import net.leelink.healthdoctor.adapter.SubjectAdapter;
import net.leelink.healthdoctor.app.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ChooseSubjectActivity extends BaseActivity implements OnLocalListener {
    RecyclerView type_list,subject_list;
    private List<String> typeList = new ArrayList<>();
    private List<String> subjectList = new ArrayList<>();
    RelativeLayout rl_back;
    SubjectAdapter typeAdapter,subjectAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_subject);
        init();
        initList();
    }

    public void init(){
        String[] strings = getResources().getStringArray(R.array.subject_type);
        typeList.addAll(Arrays.asList(strings));
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        type_list = findViewById(R.id.type_list);
        subject_list = findViewById(R.id.subject_list);
    }

    public void initList(){
        typeAdapter = new SubjectAdapter(typeList,this,1,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        type_list.setLayoutManager(layoutManager);
        type_list.setAdapter(typeAdapter);
    }

    @Override
    public void onItemClick(View view, int type) {

        if(type==1){
            int position = type_list.getChildLayoutPosition(view);
            initSubjectList(position);
            typeAdapter.setChecked(position);
        }
        if(type ==2) {
            int position = subject_list.getChildLayoutPosition(view);
            Intent intent = new Intent();
            intent.putExtra("subject",subjectList.get(position));
            setResult(2,intent);
            finish();
        }
    }

    @Override
    public void onButtonClick(View view, int position) {

    }

    public void initSubjectList(int position){
        subjectList.clear();
        String[] s = new String[]{};
        switch (position){
            case 0:
                s = getResources().getStringArray(R.array.often_subject);
                break;
            case 1:
                s = getResources().getStringArray(R.array.in_subject);
                break;
            case 2:
                s = getResources().getStringArray(R.array.out_subject);
                break;
            case 3:
                s = getResources().getStringArray(R.array.other_subject);
                break;
        }
        subjectList.addAll(Arrays.asList(s));
        subjectAdapter = new SubjectAdapter(subjectList,this,2,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        subject_list.setLayoutManager(layoutManager);
        subject_list.setAdapter(subjectAdapter);
    }
}
