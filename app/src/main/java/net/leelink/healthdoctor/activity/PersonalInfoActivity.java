package net.leelink.healthdoctor.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.bean.HospitalBean;
import net.leelink.healthdoctor.util.BitmapCompress;
import net.leelink.healthdoctor.util.Urls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_back, rl_hospital, rl_subject;
    private TextView tv_hospital, tv_subject,tv_professional;
    private Button btn_confirm;
    private ImageView img_head, id_card, id_card_back, img_tag, img_physician, img_diploma, img_title;
    private File head_file, card_file, card_back_file, tag_file, physician_file, diploma_file, title_file;
    Context context;
    private Bitmap bitmap = null;
    private Bitmap card_bitmap = null;
    private Bitmap card_back_bitmap = null;
    private Bitmap tag_bitmap = null;
    private Bitmap physician_bitmap = null;
    private Bitmap diploma_bitmap = null;
    private Bitmap title_bitmap = null;
    private final static int HEAD_ALBUM = 1;
    private final static int HEAD_PHOTO = 2;
    private final static int CARD_ALBUM = 3;
    private final static int CARD_PHOTO = 4;
    private final static int CARD_BACK_ALBUM = 5;
    private final static int CARD_BACK_PHOTO = 6;
    private final static int TAG_ALBUM = 7;
    private final static int TAG_PHOTO = 8;
    private final static int PHYSICIAN_ALBUM = 9;
    private final static int PHYSICIAN_PHOTO = 10;
    private final static int DIPLOMA_ALBUM = 11;
    private final static int DIPLOMA_PHOTO = 12;
    private final static int TITLE_ALBUM = 13;
    private final static int TITLE_PHOTO = 14;
    String cardBackPath, cardPositivePath, diplomaImgPath, physicianImgPath, tagImgPath, titleImgPath, imgPath;
    private EditText ed_subject_phone,ed_skill,ed_name,ed_contact_name,ed_phone,ed_work_exp;

    String hospitalId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        init();
        createProgressBar(this);
        context = this;
        EventBus.getDefault().register(this);
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        rl_hospital = findViewById(R.id.rl_hospital);
        rl_hospital.setOnClickListener(this);
        tv_hospital = findViewById(R.id.tv_hospital);
        rl_subject = findViewById(R.id.rl_subject);
        rl_subject.setOnClickListener(this);
        tv_subject = findViewById(R.id.tv_subject);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        img_head = findViewById(R.id.img_head);
        img_head.setOnClickListener(this);
        id_card = findViewById(R.id.id_card);
        id_card.setOnClickListener(this);
        id_card_back = findViewById(R.id.id_card_back);
        id_card_back.setOnClickListener(this);
        img_tag = findViewById(R.id.img_tag);
        img_tag.setOnClickListener(this);
        img_physician = findViewById(R.id.img_physician);
        img_physician.setOnClickListener(this);
        img_diploma = findViewById(R.id.img_diploma);
        img_diploma.setOnClickListener(this);
        img_title = findViewById(R.id.img_title);
        img_title.setOnClickListener(this);
        ed_subject_phone = findViewById(R.id.ed_subject_phone);
        ed_skill = findViewById(R.id.ed_skill);
        ed_name = findViewById(R.id.ed_name);
        ed_contact_name = findViewById(R.id.ed_contact_name);
        ed_phone = findViewById(R.id.ed_phone);
        tv_professional = findViewById(R.id.tv_professional);
        tv_professional.setOnClickListener(this);
        ed_work_exp = findViewById(R.id.ed_work_exp);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HospitalBean hospitalBean) {
        tv_hospital.setText(hospitalBean.getName());
        hospitalId = hospitalBean.getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_hospital:
                Intent intent1 = new Intent(this, ChooseHospitalActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_subject:
                Intent intent = new Intent(this, ChooseSubjectActivity.class);
                startActivityForResult(intent, 33);
                break;
            case R.id.btn_confirm:
                submit();
                break;
            case R.id.img_head:
                showPopup(HEAD_ALBUM, HEAD_PHOTO);
                backgroundAlpha(0.5f);
                break;
            case R.id.id_card:
                showPopup(CARD_ALBUM, CARD_PHOTO);
                backgroundAlpha(0.5f);
                break;
            case R.id.id_card_back:
                showPopup(CARD_BACK_ALBUM, CARD_BACK_PHOTO);
                backgroundAlpha(0.5f);
                break;
            case R.id.img_tag:
                showPopup(TAG_ALBUM, TAG_PHOTO);
                backgroundAlpha(0.5f);
                break;
            case R.id.img_physician:
                showPopup(PHYSICIAN_ALBUM, PHYSICIAN_PHOTO);
                backgroundAlpha(0.5f);
                break;
            case R.id.img_diploma:
                showPopup(DIPLOMA_ALBUM, DIPLOMA_PHOTO);
                backgroundAlpha(0.5f);
                break;
            case R.id.img_title:
                showPopup(TITLE_ALBUM, TITLE_PHOTO);
                backgroundAlpha(0.5f);
                break;
            case R.id.tv_professional:
                showTitle();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            tv_subject.setText(data.getStringExtra("subject"));
        }
        if (data != null) {
            switch (requestCode) {
                case HEAD_ALBUM:
                    Uri uri = data.getData();
                    bitmap = BitmapCompress.decodeUriBitmap(context, uri);
                    img_head.setImageBitmap(bitmap);
                    head_file = BitmapCompress.compressImage(bitmap);
                    getPath(head_file,1);
                    break;
                case HEAD_PHOTO:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = (Bitmap) bundle.get("data");
                        img_head.setImageBitmap(bitmap);
                        head_file = BitmapCompress.compressImage(bitmap);
                        getPath(head_file,1);
                    }
                    break;
                case CARD_ALBUM:
                    Uri uri1 = data.getData();
                    card_bitmap = BitmapCompress.decodeUriBitmap(context, uri1);
                    id_card.setImageBitmap(card_bitmap);
                    card_file = BitmapCompress.compressImage(card_bitmap);
                    getPath(card_file,2);
                    break;
                case CARD_PHOTO:
                    Bundle bundle1 = data.getExtras();
                    if (bundle1 != null) {
                        card_bitmap = (Bitmap) bundle1.get("data");
                        id_card.setImageBitmap(card_bitmap);
                        card_file = BitmapCompress.compressImage(card_bitmap);
                        getPath(card_file,2);
                    }
                    break;
                case CARD_BACK_ALBUM:
                    Uri uri2 = data.getData();
                    card_back_bitmap = BitmapCompress.decodeUriBitmap(context, uri2);
                    id_card_back.setImageBitmap(card_back_bitmap);
                    card_back_file = BitmapCompress.compressImage(card_back_bitmap);
                    getPath(card_back_file,3);
                    break;
                case CARD_BACK_PHOTO:
                    Bundle bundle2 = data.getExtras();
                    if (bundle2 != null) {
                        card_back_bitmap = (Bitmap) bundle2.get("data");
                        id_card_back.setImageBitmap(card_back_bitmap);
                        card_back_file = BitmapCompress.compressImage(card_back_bitmap);
                        getPath(card_back_file,3);
                    }
                    break;
                case TAG_ALBUM:
                    Uri uri3 = data.getData();
                    tag_bitmap = BitmapCompress.decodeUriBitmap(context, uri3);
                    img_tag.setImageBitmap(tag_bitmap);
                    tag_file = BitmapCompress.compressImage(tag_bitmap);
                    getPath(tag_file,4);
                    break;
                case TAG_PHOTO:
                    Bundle bundle3 = data.getExtras();
                    if (bundle3 != null) {
                        tag_bitmap = (Bitmap) bundle3.get("data");
                        img_tag.setImageBitmap(tag_bitmap);
                        tag_file = BitmapCompress.compressImage(tag_bitmap);
                        getPath(tag_file,4);
                    }
                    break;
                case PHYSICIAN_ALBUM:
                    Uri uri4 = data.getData();
                    physician_bitmap = BitmapCompress.decodeUriBitmap(context, uri4);
                    img_physician.setImageBitmap(physician_bitmap);
                    physician_file = BitmapCompress.compressImage(physician_bitmap);
                    getPath(physician_file,5);
                    break;
                case PHYSICIAN_PHOTO:
                    Bundle bundle4 = data.getExtras();
                    if (bundle4 != null) {
                        physician_bitmap = (Bitmap) bundle4.get("data");
                        img_physician.setImageBitmap(physician_bitmap);
                        physician_file = BitmapCompress.compressImage(physician_bitmap);
                        getPath(physician_file,5);
                    }
                    break;
                case DIPLOMA_ALBUM:
                    Uri uri5 = data.getData();
                    diploma_bitmap = BitmapCompress.decodeUriBitmap(context, uri5);
                    img_diploma.setImageBitmap(diploma_bitmap);
                    diploma_file = BitmapCompress.compressImage(diploma_bitmap);
                    getPath(diploma_file,6);
                    break;
                case DIPLOMA_PHOTO:
                    Bundle bundle5 = data.getExtras();
                    if (bundle5 != null) {
                        diploma_bitmap = (Bitmap) bundle5.get("data");
                        img_diploma.setImageBitmap(diploma_bitmap);
                        diploma_file = BitmapCompress.compressImage(diploma_bitmap);
                        getPath(diploma_file,6);
                    }
                    break;
                case TITLE_ALBUM:
                    Uri uri6 = data.getData();
                    title_bitmap = BitmapCompress.decodeUriBitmap(context, uri6);
                    img_title.setImageBitmap(title_bitmap);
                    title_file = BitmapCompress.compressImage(title_bitmap);
                    getPath(title_file,7);
                    break;
                case TITLE_PHOTO:
                    Bundle bundle6 = data.getExtras();
                    if (bundle6 != null) {
                        title_bitmap = (Bitmap) bundle6.get("data");
                        img_title.setImageBitmap(title_bitmap);
                        title_file = BitmapCompress.compressImage(title_bitmap);
                        getPath(title_file,7);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void submit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cardBackPath", cardBackPath);
            jsonObject.put("cardPositivePath",cardPositivePath);
            jsonObject.put("department",tv_subject.getText().toString().trim());
            jsonObject.put("departmentPhone",ed_subject_phone.getText().toString().trim());
            jsonObject.put("diplomaImgPath",diplomaImgPath);
            jsonObject.put("expertise",ed_skill.getText().toString().trim());
            jsonObject.put("hospitalId",hospitalId);
            jsonObject.put("imgPath",imgPath);
            jsonObject.put("physicianImgPath",physicianImgPath);
            jsonObject.put("realName",ed_name.getText().toString().trim());
            jsonObject.put("remark",ed_contact_name.getText().toString().trim());
            jsonObject.put("tagImgPath",tagImgPath);
            jsonObject.put("telephone",ed_phone.getText().toString().trim());
            jsonObject.put("title",tv_professional.getText().toString().trim());
            jsonObject.put("titleImgPath",titleImgPath);
            jsonObject.put("workHistory",ed_work_exp.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e( "submit: ",jsonObject.toString() );

        OkGo.<String>put(Urls.getInstance().REGIST)
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("完善信息", json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(PersonalInfoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(PersonalInfoActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public String getPath(File file, final int type) {
        showProgressBar();
        final String[] s = {""};
        OkGo.<String>post(Urls.getInstance().PHOTO)
                .tag(this)
                .params("multipartFile", file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取地址 ", json.toString());
                            if (json.getInt("status") == 200) {
                                switch (type){
                                    case 1:
                                        imgPath = json.getString("data");
                                        break;
                                    case 2:
                                        cardPositivePath = json.getString("data");
                                        break;
                                    case 3:
                                        cardBackPath = json.getString("data");
                                        break;
                                    case 4:
                                        tagImgPath = json.getString("data");
                                        break;
                                    case 5:
                                        physicianImgPath = json.getString("data");
                                        break;
                                    case 6:
                                        diplomaImgPath = json.getString("data");
                                        break;
                                    case 7:
                                        titleImgPath = json.getString("data");
                                        break;
                                }


                            } else {
                                Toast.makeText(PersonalInfoActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        stopProgressBar();
                    }
                });
        return s[0];
    }

    //弹出机构列表
    public void showTitle() {
        final List<String> titles = new ArrayList<>();
        titles.add("医师");
        titles.add("主治医师");
        titles.add("副主任医师");
        titles.add("主任医师");

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tv_professional.setText(titles.get(options1));
            }
        })
                .setDividerColor(Color.parseColor("#A0A0A0"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setContentTextSize(18)//设置滚轮文字大小
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        pvOptions.setPicker(titles);
        pvOptions.show();
    }

    @SuppressLint("WrongConstant")
    public void showPopup(final int albumCode, final int photoCode) {
        View popview = LayoutInflater.from(PersonalInfoActivity.this).inflate(R.layout.popu_head, null);
        Button btn_album = (Button) popview.findViewById(R.id.btn_album);

        Button btn_photograph = (Button) popview.findViewById(R.id.btn_photograph);

        btn_album.setOnClickListener(this);
        btn_photograph.setOnClickListener(this);
        final PopupWindow popuPhoneW = new PopupWindow(popview,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popuPhoneW.setFocusable(true);
        popuPhoneW.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popuPhoneW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popuPhoneW.setOutsideTouchable(true);
        popuPhoneW.setBackgroundDrawable(new BitmapDrawable());
        popuPhoneW.setOnDismissListener(new poponDismissListener());
        popuPhoneW.showAtLocation(img_head, Gravity.CENTER, 0, 0);
        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuPhoneW.dismiss();
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, albumCode);
            }
        });
        btn_photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuPhoneW.dismiss();
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //当拒绝了授权后，为提升用户体验，可以以弹窗的方式引导用户到设置中去进行设置
                    new AlertDialog.Builder(context)
                            .setMessage("需要开启权限才能使用此功能")
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户到设置中去进行设置
                                    Intent intent = new Intent();
                                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                    startActivity(intent);

                                }
                            }).setNegativeButton("取消", null).create().show();
                } else {
                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent2, photoCode);
                }
            }
        });
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            // Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
