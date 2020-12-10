package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.BaseActivity;

public class CureCountActivity extends BaseActivity {
    RelativeLayout rl_back;
    EditText ed_count;
    ImageView img_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cure_count);
        init();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ed_count = findViewById(R.id.ed_count);
        img_submit = findViewById(R.id.img_submit);
        img_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("count",ed_count.getText().toString().trim());
                setResult(5,intent);
                finish();
            }
        });
    }
}
