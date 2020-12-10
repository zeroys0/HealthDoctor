package net.leelink.healthdoctor.activity;


import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.BaseActivity;


public class WebActivity extends BaseActivity {
    private WebView webview;
    private RelativeLayout rl_back,rl_top;
    LinearLayout ll1;
    AgentWeb agentweb;
    TextView text_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        init();
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_top = findViewById(R.id.rl_top);
        ll1 = findViewById(R.id.ll_1);
        setWeb(getIntent().getStringExtra("url"));
        text_title = findViewById(R.id.text_title);
        text_title.setText(getIntent().getStringExtra("title"));

    }
    void setWeb(String url) {


        if (agentweb == null) {

            agentweb = AgentWeb.with(WebActivity.this)
                    .setAgentWebParent(ll1, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(url);
        } else {
            ll1.setVisibility(View.GONE);
            agentweb.getWebCreator().getWebView().loadUrl(url);

            ll1.setVisibility(View.VISIBLE);
        }

    }
}
