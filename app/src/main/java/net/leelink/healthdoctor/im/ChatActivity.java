package net.leelink.healthdoctor.im;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.activity.PersonalInfoActivity;
import net.leelink.healthdoctor.app.BaseActivity;
import net.leelink.healthdoctor.im.adapter.Adapter_ChatMessage;
import net.leelink.healthdoctor.im.modle.ChatMessage;
import net.leelink.healthdoctor.im.util.Util;
import net.leelink.healthdoctor.im.view.AudioRecorderButton;
import net.leelink.healthdoctor.im.websocket.JWebSocketClient;
import net.leelink.healthdoctor.im.websocket.JWebSocketClientService;
import net.leelink.healthdoctor.util.BitmapCompress;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private EditText et_content;
    private ListView listView;
    private Button btn_send;
    private ImageView btn_multimedia,btn_voice_or_text;
    private List<ChatMessage> chatMessageList = new ArrayList<>();//消息列表
    private Adapter_ChatMessage adapter_chatMessage;
    private ChatMessageReceiver chatMessageReceiver;
    private Bitmap bitmap = null;
    private File img_file;
    private ImageButton iv_return;
    private AudioRecorderButton id_recorder_button;
    private int type = 1;
    private RelativeLayout rl_input;

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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        mContext = ChatActivity.this;
        //启动服务
        startJWebSClientService();
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
        //检测通知是否开启
        checkNotification(mContext);
        findViewById();
        initView();
    }

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
        iv_return = findViewById(R.id.iv_return);
        iv_return.setOnClickListener(this);
        btn_voice_or_text = findViewById(R.id.btn_voice_or_text);
        btn_voice_or_text.setOnClickListener(this);
        id_recorder_button = findViewById(R.id.id_recorder_button);
        rl_input = findViewById(R.id.rl_input);

        id_recorder_button.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                File file = new File(filePath);
                sendRecorder(file,seconds);

            }
        });
    }

    private void initView() {
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
                    btn_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
                    jsonObject.put("userId", 5);
                    jsonObject.put("to", 4);
                    jsonObject.put("type", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (client != null && client.isOpen()) {
                    jWebSClientService.sendMsg(jsonObject.toString());
                    //暂时将发送的消息加入消息列表，实际以发送成功为准（也就是服务器返回你发的消息时）
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setContent(content);
                    chatMessage.setIsMeSend(1);
                    chatMessage.setIsRead(1);
                    chatMessage.setTime(System.currentTimeMillis() + "");
                    chatMessage.setType(1);
                    chatMessageList.add(chatMessage);
                    initChatMsgListView();
                    et_content.setText("");
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
                if(type ==1) {
                    rl_input.setVisibility(View.INVISIBLE);
                    id_recorder_button.setVisibility(View.VISIBLE);
                    type = 2;
                } else if(type ==2 ) {
                    rl_input.setVisibility(View.VISIBLE);
                    id_recorder_button.setVisibility(View.INVISIBLE);
                    type = 1;
                }
                break;
            case R.id.iv_return:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!= null){
            if(requestCode ==4){
                Uri uri = data.getData();
                bitmap = BitmapCompress.decodeUriBitmap(mContext, uri);
                img_file = BitmapCompress.compressImage(bitmap);
                getPath(img_file);

            }
        }
    }

    private void initChatMsgListView() {
        adapter_chatMessage = new Adapter_ChatMessage(mContext, chatMessageList);
        listView.setAdapter(adapter_chatMessage);
        listView.setSelection(chatMessageList.size());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //播放音频  完成后改回原来的background
                MediaManager.playSound(Urls.IMG_URL+chatMessageList.get(position).getContent(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                    }
                });
            }
        });
    }

    /**
     * 上传图片获取图片url加载
     * @param file
     * @return
     */
    public String getPath(File file) {
        final String[] s = {""};
        OkGo.<String>post(Urls.PHOTO)
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
                                    jsonObject.put("userId", 5);
                                    jsonObject.put("to", 4);
                                    jsonObject.put("type", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (client != null && client.isOpen()) {
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

    public String sendRecorder(File file, final float seconds){
        final String[] s = {""};
        OkGo.<String>post(Urls.MP3)
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
                                    jsonObject.put("userId", 5);
                                    jsonObject.put("to", 4);
                                    jsonObject.put("type", 3);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (client != null && client.isOpen()) {
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


    //数据类
    class Recorder{

        float time;
        String filePath;

        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Recorder(float time, String filePath) {
            super();
            this.time = time;
            this.filePath = filePath;
        }
    }



}
