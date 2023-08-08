package net.leelink.healthdoctor.im;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyjingfish.openimagelib.OpenImage;
import com.flyjingfish.openimagelib.beans.OpenImageUrl;
import com.flyjingfish.openimagelib.enums.MediaType;
import com.flyjingfish.openimagelib.listener.SourceImageViewIdGet;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import net.leelink.healthdoctor.MainActivity;
import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.adapter.OnOrderListener;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.im.adapter.Adapter_ChatMessage;
import net.leelink.healthdoctor.im.adapter.ChatMessageAdapter;
import net.leelink.healthdoctor.im.adapter.OnMessageListener;
import net.leelink.healthdoctor.im.data.MessageBean;
import net.leelink.healthdoctor.im.data.MessageDataHelper;
import net.leelink.healthdoctor.im.data.MessageListHelper;
import net.leelink.healthdoctor.im.modle.ChatMessage;
import net.leelink.healthdoctor.im.util.Util;
import net.leelink.healthdoctor.im.view.AudioRecorderButton;
import net.leelink.healthdoctor.im.websocket.JWebSocketClient;
import net.leelink.healthdoctor.im.websocket.JWebSocketClientService;
import net.leelink.healthdoctor.util.BitmapCompress;
import net.leelink.healthdoctor.util.Logger;
import net.leelink.healthdoctor.util.Urls;
import net.leelink.healthdoctor.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener, OnMessageListener {
    private Context mContext;
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private EditText et_content;
    private RecyclerView listView;
    private Button btn_send, btn_refuse, btn_confirm;
    private ImageView btn_multimedia, btn_voice_or_text;
    private List<ChatMessage> chatMessageList = new ArrayList<>();//消息列表
    private Adapter_ChatMessage adapter_chatMessage;
    private ChatMessageReceiver chatMessageReceiver;
    private Bitmap bitmap = null;
    private File img_file;
    private ImageButton iv_return;
    private AudioRecorderButton id_recorder_button;
    private int type = 1;
    private RelativeLayout rl_input, rl_back, rl_bottom_btn;
    private View chat_layout;
    MessageDataHelper messageDataHelper;
    MessageListHelper messageListHelper;
    String clientId;
    private TextView text_title;
    ChatMessageAdapter chatMessageAdapter;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "服务与活动成功断开");
        }
    };

    @Override
    public void onItemClick(View view) {


    }

    /**
     * 点击播放音频
     *
     * @param view
     * @param position
     */
    @Override
    public void onButtonClick(View view, int position) {
        MediaManager.playSound(Urls.getInstance().IMG_URL + chatMessageList.get(position).getContent(), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }

    @Override
    public void onMessageClick(View view, int position) {
        String url = Urls.getInstance().IMG_URL + chatMessageList.get(position).getContent();
        /**
         *  OpenImage.with(this)
         *                 //点击ImageView所在的RecyclerView（也支持设置setClickViewPager2，setClickViewPager，setClickGridView，setClickListView，setClickImageView，setNoneClickView）
         *                 .setClickRecyclerView(listView,new SourceImageViewIdGet() {
         *                     @Override
         *                     public int getImageViewId(OpenImageUrl data, int position) {
         *                         return R.id.img_picture;//点击的ImageView的Id
         *                     }
         *                 })
         *                 //点击的ImageView的ScaleType类型（如果设置不对，打开的动画效果将是错误的）
         *                 .setSrcImageViewScaleType(ImageView.ScaleType.CENTER_CROP,true)
         *                 //RecyclerView的数据
         *                 .setImageUrl(url, MediaType.IMAGE)
         *                 //点击的ImageView所在数据的位置
         *                 .setClickPosition(0)
         *                 //开始展示大图
         *                 .show();
         */

        OpenImage.with(this)
                .setClickImageView((ImageView) view)    //设置展示的view
                .setSrcImageViewScaleType(ImageView.ScaleType.CENTER_CROP, true)    //点击的缩放类型
                .setImageUrl(url, MediaType.IMAGE)  //设置要显示的数据
                .show();    //显示大图

    }

    /**
     * 接收消息广播
     */
    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.has("messageType")) {
                    if (jsonObject.getInt("messageType") == 4) {
                        message = jsonObject.getString("textMessage");
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setContent(message);
                        chatMessage.setIsMeSend(0);
                        chatMessage.setIsRead(1);
                        chatMessage.setType(jsonObject.getInt("type"));
                        chatMessage.setTime(System.currentTimeMillis() + "");
                        chatMessageList.add(chatMessage);
                        initChatMsgListView();
                    }
                } else if (jsonObject.getInt("status") == 201) {
                    Toast.makeText(getApplicationContext(), "您的次数已达限制!", Toast.LENGTH_SHORT).show();
                } else if (jsonObject.getInt("status") == 202) {
                    Toast.makeText(getApplicationContext(), "发送失败!未进行下单", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_chat);
        mContext = ChatActivity.this;
        SharedPreferences sp = getSharedPreferences("sp", 0);
        clientId = sp.getString("clientId", "");
        messageDataHelper = new MessageDataHelper(this);
        messageListHelper = new MessageListHelper(this);
        //启动服务
        startJWebSClientService();
//        //绑定服务
        bindService();
//        EventBus.getDefault().register(this);
        //注册广播
        doRegisterReceiver();
        //检测通知是否开启
        checkNotification(mContext);
        findViewById();
        initView();
        initList();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageBean messageBean) {
//        Toast.makeText(mContext, messageBean.message, Toast.LENGTH_SHORT).show();
//    }


    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(mContext, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(mContext, JWebSocketClientService.class);
        startService(intent);
    }

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        registerReceiver(chatMessageReceiver, filter);
    }


    private void findViewById() {
        listView = findViewById(R.id.chatmsg_listView);
        btn_send = findViewById(R.id.btn_send);
        et_content = findViewById(R.id.et_content);
        btn_send.setOnClickListener(this);
        btn_multimedia = findViewById(R.id.btn_multimedia);
        btn_multimedia.setOnClickListener(this);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        btn_voice_or_text = findViewById(R.id.btn_voice_or_text);
        btn_voice_or_text.setOnClickListener(this);
        id_recorder_button = findViewById(R.id.id_recorder_button);
        rl_input = findViewById(R.id.rl_input);
        rl_bottom_btn = findViewById(R.id.rl_bottom_btn);
        chat_layout = findViewById(R.id.chat_layout);
        btn_refuse = findViewById(R.id.btn_refuse);
        btn_refuse.setOnClickListener(this);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        id_recorder_button.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {

                File file = new File(filePath);
                sendRecorder(file, seconds);

            }
        });
        text_title = findViewById(R.id.text_title);
        text_title.setText(getIntent().getStringExtra("name"));
    }

    private void initView() {
        Utils.setStatusBarColor(this, R.color.white);
        if (getIntent().getIntExtra("state", 0) == 0) {
            chat_layout.setVisibility(View.GONE);
            rl_bottom_btn.setVisibility(View.VISIBLE);
        } else if (getIntent().getIntExtra("state", 0) == 1) {
            chat_layout.setVisibility(View.VISIBLE);
            rl_bottom_btn.setVisibility(View.GONE);
        } else if (getIntent().getIntExtra("state", 0) == 2) {
            chat_layout.setVisibility(View.GONE);
            rl_bottom_btn.setVisibility(View.GONE);
        }
        //监听输入框的变化
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_content.getText().toString().length() > 0) {
                    btn_send.setVisibility(View.VISIBLE);
                } else {
//                    btn_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void initList() {
        SQLiteDatabase db = messageDataHelper.getReadableDatabase();
        String sql = "select content,time,isMeSend,isRead,type,RecorderTime from MessageDataBase where sendId=? and receiveId=?";
        Cursor c = db.rawQuery(sql, new String[]{clientId, getIntent().getStringExtra("clientId")});
        String content;
        String time;
        int isMeSend;
        int isRead;
        int type;
        float RecorderTime;
        while (c.moveToNext()) {
            content = c.getString(0);
            time = c.getString(1);
            isMeSend = c.getInt(2);
            isRead = c.getInt(3);
            type = c.getInt(4);
            RecorderTime = c.getFloat(5);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setContent(content);
            chatMessage.setTime(time);
            chatMessage.setIsMeSend(isMeSend);
            chatMessage.setIsRead(isRead);
            chatMessage.setType(type);
            chatMessage.setRecorderTime(RecorderTime);
            chatMessageList.add(chatMessage);
        }
        db.close();
        c.close();
//        adapter_chatMessage = new Adapter_ChatMessage(mContext, chatMessageList,MyApplication.userInfo.getImgPath(),getIntent().getStringExtra("receive_head"));
//
//        listView.setAdapter(adapter_chatMessage);
//        listView.setSelection(chatMessageList.size());
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //播放音频  完成后改回原来的background
//                MediaManager.playSound(Urls.getInstance().IMG_URL+chatMessageList.get(position).getContent(), new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//
//                    }
//                });
//            }
//        });

        chatMessageAdapter = new ChatMessageAdapter(mContext, chatMessageList, MyApplication.userInfo.getImgPath(), getIntent().getStringExtra("receive_head"), this);
        if (getIntent().getStringExtra("name") != null) {
            chatMessageAdapter.setName(getIntent().getStringExtra("name"));
        }
        listView.setAdapter(chatMessageAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
        listView.scrollToPosition(chatMessageList.size() - 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                String content = et_content.getText().toString();
                if (content.length() <= 0) {
                    Util.showToast(mContext, "消息不能为空哟");
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("message", content);
                    jsonObject.put("userId", clientId);
                    jsonObject.put("to", getIntent().getStringExtra("clientId"));
                    jsonObject.put("type", 1);
                    jsonObject.put("Id", System.currentTimeMillis());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (client != null && client.isOpen()) {
                    SQLiteDatabase db = messageDataHelper.getReadableDatabase();
                    SQLiteDatabase db_list = messageListHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("content", content);
                    cv.put("time", System.currentTimeMillis() + "");
                    cv.put("isMeSend", 1);
                    cv.put("isRead", 1);
                    cv.put("sendId", clientId);
                    cv.put("receiveId", getIntent().getStringExtra("clientId"));
                    cv.put("type", 1);
                    cv.put("RecorderTime", 0);
                    db.insert("MessageDataBase", null, cv);
                    db_list.replace("MessageListDB", null, cv);
                    jWebSClientService.sendMsg(jsonObject.toString());
                    //暂时将发送的消息加入消息列表，实际以发送成功为准（也就是服务器返回你发的消息时）
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setContent(content);
                    chatMessage.setIsMeSend(1);
                    chatMessage.setIsRead(1);
                    chatMessage.setTime(System.currentTimeMillis() + "");
                    chatMessage.setType(1);
                    chatMessageList.add(chatMessage);
//                    chatMessageAdapter.notifyDataSetChanged();
                    initChatMsgListView();
                    et_content.setText("");
                    db.close();
                    db_list.close();

                } else {
                    Util.showToast(mContext, "连接已断开，请稍等或重启App哟");
                }
                break;
            case R.id.btn_multimedia:

                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, 4);
                break;
            case R.id.btn_voice_or_text:
                requestPermissions();
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_refuse:       //拒接
                refuse();
                break;
            case R.id.btn_confirm:      //接单
                confirm();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 4) {
                Uri uri = data.getData();
                bitmap = BitmapCompress.decodeUriBitmap(mContext, uri);
                img_file = BitmapCompress.compressImage(bitmap, mContext);
                getPath(img_file);

            }
        }
    }

    private void initChatMsgListView() {
//        adapter_chatMessage = new Adapter_ChatMessage(mContext, chatMessageList,MyApplication.userInfo.getImgPath(),getIntent().getStringExtra("receive_head"));
//        listView.setAdapter(adapter_chatMessage);
//        listView.setSelection(chatMessageList.size());
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //播放音频  完成后改回原来的background
//                MediaManager.playSound(Urls.getInstance().IMG_URL+chatMessageList.get(position).getContent(), new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//
//                    }
//                });
//            }
//        });

        chatMessageAdapter = new ChatMessageAdapter(mContext, chatMessageList, MyApplication.userInfo.getImgPath(), getIntent().getStringExtra("receive_head"), this);
        if (getIntent().getStringExtra("name") != null) {
            chatMessageAdapter.setName(getIntent().getStringExtra("name"));
        }
        listView.setAdapter(chatMessageAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
        listView.scrollToPosition(chatMessageList.size() - 1);
    }

    /**
     * 上传图片获取图片url加载
     *
     * @param file
     * @return
     */
    public String getPath(File file) {
        final String[] s = {""};
        OkGo.<String>post(Urls.getInstance().PHOTO)
                .tag(this)
                .params("multipartFile", file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取地址 ", json.toString());
                            if (json.getInt("status") == 200) {
                                String content = json.getString("data");
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("message", content);
                                    jsonObject.put("userId", clientId);
                                    jsonObject.put("to", getIntent().getStringExtra("clientId"));
                                    jsonObject.put("type", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (client != null && client.isOpen()) {
                                    SQLiteDatabase db = messageDataHelper.getReadableDatabase();
                                    ContentValues cv = new ContentValues();
                                    cv.put("content", content);
                                    cv.put("time", System.currentTimeMillis() + "");
                                    cv.put("isMeSend", 1);
                                    cv.put("isRead", 1);
                                    cv.put("sendId", clientId);
                                    cv.put("receiveId", getIntent().getStringExtra("clientId"));
                                    cv.put("type", 2);
                                    cv.put("RecorderTime", 0);
                                    db.insert("MessageDataBase", null, cv);
                                    jWebSClientService.sendMsg(jsonObject.toString());
                                    //暂时将发送的消息加入消息列表，实际以发送成功为准（也就是服务器返回你发的消息时）
                                    ChatMessage chatMessage = new ChatMessage();
                                    chatMessage.setContent(content);
                                    chatMessage.setIsMeSend(1);
                                    chatMessage.setIsRead(1);
                                    chatMessage.setType(2);
                                    chatMessage.setTime(System.currentTimeMillis() + "");
                                    chatMessageList.add(chatMessage);
                                    initChatMsgListView();
                                    et_content.setText("");
                                    db.close();
                                } else {
                                    Util.showToast(mContext, "连接已断开，请稍等或重启App哟");
                                }
                            } else {
                                Toast.makeText(mContext, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
        return s[0];
    }

    /**
     * 上传音频文件获取音频地址
     *
     * @param file
     * @param seconds
     * @return
     */
    public String sendRecorder(File file, final float seconds) {
        final String[] s = {""};
        OkGo.<String>post(Urls.getInstance().MP3)
                .tag(this)
                .params("multipartFile", file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取地址 ", json.toString());
                            if (json.getInt("status") == 200) {
                                String content = json.getString("data");
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("message", content);
                                    jsonObject.put("userId", clientId);
                                    jsonObject.put("to", getIntent().getStringExtra("clientId"));
                                    jsonObject.put("type", 3);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (client != null && client.isOpen()) {
                                    SQLiteDatabase db = messageDataHelper.getReadableDatabase();
                                    ContentValues cv = new ContentValues();
                                    cv.put("content", content);
                                    cv.put("time", System.currentTimeMillis() + "");
                                    cv.put("isMeSend", 1);
                                    cv.put("isRead", 1);
                                    cv.put("sendId", clientId);
                                    cv.put("receiveId", getIntent().getStringExtra("clientId"));
                                    cv.put("type", 3);
                                    cv.put("RecorderTime", seconds);
                                    db.insert("MessageDataBase", null, cv);
                                    jWebSClientService.sendMsg(jsonObject.toString());
                                    //暂时将发送的消息加入消息列表，实际以发送成功为准（也就是服务器返回你发的消息时）
                                    ChatMessage chatMessage = new ChatMessage();
                                    chatMessage.setContent(content);
                                    chatMessage.setIsMeSend(1);
                                    chatMessage.setIsRead(1);
                                    chatMessage.setTime(System.currentTimeMillis() + "");
                                    chatMessage.setType(3);
                                    chatMessage.setRecorderTime(seconds);
                                    chatMessageList.add(chatMessage);
                                    initChatMsgListView();
                                    et_content.setText("");

                                } else {
                                    Util.showToast(mContext, "连接已断开，请稍等或重启App哟");
                                }
                            } else {
                                Toast.makeText(mContext, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
        return s[0];
    }

    /**
     * 医生接诊
     */
    public void confirm() {
        OkGo.<String>post(Urls.getInstance().CONFIRM + "/" + getIntent().getStringExtra("orderId"))
                .tag(this)
                .headers("token", MyApplication.token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("接单", json.toString());
                            if (json.getInt("status") == 200) {
                                rl_bottom_btn.setVisibility(View.GONE);
                                chat_layout.setVisibility(View.VISIBLE);
                            } else if (json.getInt("status") == 505) {
//                                reLogin(mContext);
                            } else {
                                Toast.makeText(mContext, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 拒接
     */
    public void refuse() {
        OkGo.<String>post(Urls.getInstance().NO_CONFIRM + "/" + getIntent().getStringExtra("orderId") + "/" + "reason")
                .tag(this)
                .headers("token", MyApplication.token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("拒接订单", json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(mContext, "已拒绝", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (json.getInt("status") == 505) {
//                                reLogin(mContext);
                            } else {
                                Toast.makeText(mContext, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 检测是否开启通知
     *
     * @param context
     */
    private void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }

    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    private void setNotification(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

    /**
     * 获取通知权限,监测是否开启了系统通知
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("CheckResult")
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(ChatActivity.this);
        rxPermission.requestEach(
                android.Manifest.permission.RECORD_AUDIO)//麦克风
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(com.tbruyelle.rxpermissions2.Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Logger.i("用户已经同意该权限", permission.name + " is granted.");
                            if (type == 1) {
                                rl_input.setVisibility(View.INVISIBLE);
                                id_recorder_button.setVisibility(View.VISIBLE);
                                type = 2;
                            } else if (type == 2) {
                                rl_input.setVisibility(View.VISIBLE);
                                id_recorder_button.setVisibility(View.INVISIBLE);
                                type = 1;
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Logger.i("用户拒绝了该权限,没有选中『不再询问』", permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Logger.i("用户拒绝了该权限,并且选中『不再询问』", permission.name + " is denied.");
                        }

                    }
                });

    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

}
