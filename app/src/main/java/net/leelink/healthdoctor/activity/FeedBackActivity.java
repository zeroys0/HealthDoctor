package net.leelink.healthdoctor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.JoinAdapter;
import net.leelink.healthdoctor.adapter.OnItemJoinClickListener;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.util.Urls;
import net.leelink.healthdoctor.view.CustomLinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener, OnItemJoinClickListener {
    RecyclerView recyclerview;
    RelativeLayout rl_back;
    JoinAdapter joinAdapter;
    public static final int IMAGE_ITEM_ADD = -1;
    private List<ImageItem> list = new ArrayList<>();
    public static final int maxImgCount = 4;
    private List<ImageItem> images = new ArrayList<>();
    Context context;
    private Button btn_confirm;
    private EditText ed_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        init();
        context = this;
        initData();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        recyclerview = findViewById(R.id.recyclerview);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        ed_content = findViewById(R.id.ed_content);

    }

    public void initData() {
        joinAdapter = new JoinAdapter(list, this, this, 4);
        CustomLinearLayoutManager customLinearLayoutManager = new CustomLinearLayoutManager(this, 4);
        recyclerview.setAdapter(joinAdapter);
        recyclerview.setLayoutManager(customLinearLayoutManager);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_confirm:
                submit();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    list.addAll(images);
                    joinAdapter.setImages(list);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == 101) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    list.clear();
                    list.addAll(images);
                    joinAdapter.setImages(list);
                }
            }
        }
    }

    public void submit(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("advice",ed_content);
            if(images.size()>1){
                File file1 = new File(images.get(0).path);
                jsonObject.put("file1",file1);
            }
            if(images.size()>2){
                File file2 = new File(images.get(1).path);
                jsonObject.put("file2",file2);
            }
            if(images.size()>3){
                File file3 = new File(images.get(2).path);
                jsonObject.put("file3",file3);
            }
            if(images.size()>4){
                File file4 = new File(images.get(3).path);
                jsonObject.put("file4",file4);
            }
            jsonObject.put("id", MyApplication.userInfo.getId());
            jsonObject.put("telephone",MyApplication.userInfo.getTelephone());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Urls.getInstance().ADVICE)
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("意见反馈",json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(context, "您的反馈我们已收到", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemAdd(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FeedBackActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);

                } else {
//                    Intent intent = new Intent(this, ImageGridActivity.class);
//                    intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
//                    startActivityForResult(intent, 1);
                    //打开选择,本次允许选择的数量
                    ImagePicker.getInstance().setSelectLimit(maxImgCount - list.size());
                    Intent intent = new Intent(this, ImageGridActivity.class);
                    startActivityForResult(intent, 100);
                }
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) joinAdapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, 101);
                break;
        }
    }
}
