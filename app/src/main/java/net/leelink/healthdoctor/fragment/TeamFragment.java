package net.leelink.healthdoctor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.recyclerview.widget.RecyclerView;

public class TeamFragment extends BaseFragment {
    Context context;
    RecyclerView team_list;
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view  =inflater.inflate(R.layout.fragment_message, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = getContext();
        init(view);
        initData();
        return view;
    }

    public void init(View view) {
        team_list = view.findViewById(R.id.team_list);

    }

    public void initData() {
        OkGo.<String>get(Urls.FAMILY_GROUP)
                .params("pageNum", 1)
                .params("pageSize", 100)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("查询分组列表", json.toString());
                            if (json.getInt("status") == 200) {

                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
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
    }
}
