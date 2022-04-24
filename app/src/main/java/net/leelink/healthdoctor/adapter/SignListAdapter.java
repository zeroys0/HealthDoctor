package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.bean.SignBean;
import net.leelink.healthdoctor.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SignListAdapter extends RecyclerView.Adapter<SignListAdapter.ViewHolder> {
    List<SignBean> list;
    Context context;
    OnSignListener onSignListener;
    int type;

    public SignListAdapter(List<SignBean> list, Context context, OnSignListener onSignListener,int type) {
        this.list = list;
        this.context = context;
        this.onSignListener = onSignListener;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sign,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(Urls.getInstance().IMG_URL+list.get(position).getHeadImgPath()).into(holder.img_head);
        holder.tv_name.setText(list.get(position).getElderlyName());
        holder.tv_age.setText(list.get(position).getAge()+"岁");
        if(list.get(position).getSex() ==0) {
            holder.tv_sex.setText("男");
        } else {
            holder.tv_sex.setText("女");
        }
        String address = list.get(position).getAddress();
        try {
            JSONObject jsonObject = new JSONObject(address);
            holder.tv_address.setText("地址:"+jsonObject.getString("fullAddress"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.tv_time.setText("签约时间:"+list.get(position).getApplyTime()
        );
        if(type ==2){
            holder.btn_cancel.setVisibility(View.GONE);
            holder.btn_confirm.setText("解除签约");
        }
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignListener.onButtonCancel(v,position);
            }
        });
        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignListener.onButtonClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_head;
        TextView tv_name,tv_sex,tv_address,tv_time,tv_age;
        Button btn_cancel,btn_confirm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_head = itemView.findViewById(R.id.img_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_time = itemView.findViewById(R.id.tv_time);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            btn_confirm = itemView.findViewById(R.id.btn_confirm);
            tv_age = itemView.findViewById(R.id.tv_age);
        }
    }
}
