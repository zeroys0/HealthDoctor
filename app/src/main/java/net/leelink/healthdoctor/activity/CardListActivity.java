package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.CardListAdapter;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.bean.CardBean;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardListActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {
    RelativeLayout rl_back;
    private SwipeRecyclerView card_list;
    private CardListAdapter cardListAdapter;
    private List<CardBean> list=  new ArrayList<>();
    Context context;
    private LinearLayout ll_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        init();
        context = this;
        initData();
        initSlide();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        card_list = findViewById(R.id.card_list);
        ll_add = findViewById(R.id.ll_add);
        ll_add.setOnClickListener(this);
    }

    public void initData(){

        OkGo.<String>get(Urls.getInstance().BIND_BANK)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json =  new JSONObject(body);
                            Log.d("绑定银行卡列表", json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("data");
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<CardBean>>(){}.getType());
                                cardListAdapter = new CardListAdapter(context,list,CardListActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CardListActivity.this,RecyclerView.VERTICAL,false);
                                card_list.setLayoutManager(layoutManager);
                                card_list.setAdapter(cardListAdapter);

                            } else  if(json.getInt("status") == 505){

                                reLogin(context);
                            }else {
                                Toast.makeText(CardListActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_add:
                Intent intent = new Intent(this,BindCardActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {

    }

    private void initSlide() {

        // srvWarn.setItemViewSwipeEnabled(true);// 开启滑动删除。默认关闭。
        card_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // 创建菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
           /* SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
            // 各种文字和图标属性设置。
            leftMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。*/
                // 2 删除
                SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                deleteItem.setText("删除")
                        .setBackgroundColor(getResources().getColor(R.color.red))
                        .setTextColor(Color.WHITE) // 文字颜色。
                        .setTextSize(15) // 文字大小。
                        .setWidth(200)
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

                rightMenu.addMenuItem(deleteItem);

                // 注意：哪边不想要菜单，那么不要添加即可。
            }
        };
        // 设置监听器。
        card_list.setSwipeMenuCreator(mSwipeMenuCreator);
        card_list.setOnItemMenuClickListener(new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();

                // 左侧还是右侧菜单：
                int direction = menuBridge.getDirection();
                // 菜单在Item中的Position：
                int menuPosition = menuBridge.getPosition();
                if (menuPosition == 0) {
//                    int id = list.get(position).getStoreId();
                    delete(position);
                    Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
                }
            }
        });
        card_list.setOnItemClickListener(new com.yanzhenjie.recyclerview.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                    Intent intent = new Intent(context, ConfirmWithdrawActivity.class);
                    intent.putExtra("card_number", list.get(position).getCard());
                    intent.putExtra("bank_name",list.get(position).getBank());
                    intent.putExtra("id",list.get(position).getId());
                    startActivity(intent);
            }
        });


    }

    public void delete(int position){
        OkGo.<String>delete(Urls.getInstance().BINK_BANK)
                .tag(this)
                .params("bankId",list.get(position).getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json =  new JSONObject(body);
                            Log.d("删除银行卡", json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();
                                initData();
                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
