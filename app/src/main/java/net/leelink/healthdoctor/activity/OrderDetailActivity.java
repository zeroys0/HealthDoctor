package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.bean.HistoryOrderBean;

public class OrderDetailActivity extends BaseActivity {
    private RelativeLayout rl_back;
    private TextView tv_name,tv_sex,tv_age,tv_phone,tv_type,tv_department,tv_pay_price,tv_act_pay,tv_order_no,tv_create_time,tv_pay_time,tv_pay_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
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
        tv_name = findViewById(R.id.tv_name);
        tv_sex = findViewById(R.id.tv_sex);
        tv_age = findViewById(R.id.tv_age);
        tv_phone = findViewById(R.id.tv_phone);
        tv_type = findViewById(R.id.tv_type);
        tv_department = findViewById(R.id.tv_department);
        tv_pay_price = findViewById(R.id.tv_pay_price);
        tv_act_pay = findViewById(R.id.tv_act_pay);
        tv_order_no = findViewById(R.id.tv_order_no);
        tv_create_time = findViewById(R.id.tv_create_time);
        tv_pay_time = findViewById(R.id.tv_pay_time);
        tv_pay_type = findViewById(R.id.tv_pay_type);
    }

    public void initData(){

        HistoryOrderBean orderBean = getIntent().getParcelableExtra("order");
        tv_name.setText(orderBean.getElderlyName());
        if(orderBean.getSex()==0){
            tv_sex.setText("男");
        } else  {
            tv_sex.setText("女");
        }
        tv_age.setText(orderBean.getAge()+"岁");
//        tv_phone.setText(orderBean.get);
        switch (orderBean.getOrderType()) {
            case 1:
                break;
            case 2:
                tv_type.setText("图文问诊");
                break;
            case 3:
                break;
            case 4:
                break;
        }
        tv_department.setText(orderBean.getDepartment());
        tv_pay_price.setText("￥"+orderBean.getPayPrice());
        tv_act_pay.setText("￥"+orderBean.getActPayPrice());
        tv_order_no.setText(orderBean.getOrderNo());
        tv_create_time.setText(orderBean.getCreateTime());
        tv_pay_time.setText(orderBean.getPayTime());
        switch (orderBean.getPayMethod()){
            case 4:
                tv_pay_type.setText("余额支付");
                break;
            case 9:
                tv_pay_type.setText("微信支付");
                break;
            case 10:
                tv_pay_type.setText("支付宝支付");
                break;
        }
    }
}
