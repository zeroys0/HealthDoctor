package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.BaseActivity;

public class ExamineActivity extends BaseActivity {
    private Context context;
    private RelativeLayout rl_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine);
        context = this;
        init();
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("type")==null){
                    finish();
                }
                 else if (getIntent().getStringExtra("type").equals("reExamine")) {
                    Intent intent4 = new Intent(context, LoginActivity.class);
                    intent4.putExtra("type", "reExamine");
                    SharedPreferences sp = getSharedPreferences("sp", 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove("secretKey");
                    editor.remove("telephone");
                    editor.remove("clientId");
                    editor.apply();
                    intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent4);
                }
                finish();
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(getIntent().getStringExtra("type")==null){
                finish();
            }
            else if (getIntent().getStringExtra("type").equals("reExamine")) {
                Intent intent4 = new Intent(context, LoginActivity.class);
                intent4.putExtra("type", "reExamine");
                SharedPreferences sp = getSharedPreferences("sp", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("secretKey");
                editor.remove("telephone");
                editor.remove("clientId");
                editor.apply();
                intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent4);
            }
            finish();
        }
        return false;
    }
}