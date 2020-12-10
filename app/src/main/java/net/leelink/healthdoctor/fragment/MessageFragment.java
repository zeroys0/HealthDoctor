package net.leelink.healthdoctor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.healthdoctor.MainActivity;
import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.util.Urls;
import net.leelink.healthdoctor.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class MessageFragment extends BaseFragment {
    Context context;
    FrameLayout frame_layout;
    ExamineFragment examineFragment;
    TeamFragment teamFragment;
    VerifyFragment verifyFragment;
    FragmentManager fm;

    @Override
    public void handleCallBack(Message msg) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_team, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = getContext();
        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getState();
    }

    public void init(View view) {
        frame_layout = view.findViewById(R.id.frame_layout);
    }

    public void setFragment(int state ){
        fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = getFragmentTransaction();
        if(state == 201 ||state ==2) {
            examineFragment = (ExamineFragment) fm.findFragmentByTag("examine");
            if (examineFragment == null) {
                examineFragment = new ExamineFragment();
            }
            ft.add(R.id.frame_layout, examineFragment, "examine");
            ft.commit();
        } else if(state ==0) {
            if (verifyFragment == null) {
                ft.add(R.id.frame_layout, new VerifyFragment(), "verify");
            } else {
                ft.show(verifyFragment);
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else if(state ==1) {
            if (teamFragment == null) {
                ft.add(R.id.frame_layout, new TeamFragment(), "team");
            } else {
                ft.show(teamFragment);
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    public void getState(){
        OkGo.<String>get(Urls.IN_DOCKER)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("是否成为家庭医生",json.toString());
                            if (json.getInt("status") == 200) {
                                setFragment(json.getInt("data"));
                            } else if(json.getInt("status")==201){
                                setFragment(201);
                            }else {
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

    protected FragmentTransaction getFragmentTransaction() {
        // TODO Auto-generated method stub
        FragmentManager fm = getActivity().getSupportFragmentManager();
        examineFragment = (ExamineFragment) fm.findFragmentByTag("examine");
        verifyFragment = (VerifyFragment) fm.findFragmentByTag("verify");
        teamFragment = (TeamFragment) fm.findFragmentByTag("team");
        FragmentTransaction ft = fm.beginTransaction();
        /** 如果存在hide掉 */
        if (examineFragment != null)
            ft.hide(examineFragment);
        if (verifyFragment != null)
            ft.hide(verifyFragment);
        if (teamFragment != null)
            ft.hide(teamFragment);
        return ft;
    }

}
