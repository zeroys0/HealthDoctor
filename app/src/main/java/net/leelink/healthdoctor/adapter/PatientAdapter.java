package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.app.MyApplication;
import net.leelink.healthdoctor.bean.OrderBean;
import net.leelink.healthdoctor.util.Urls;
import net.leelink.healthdoctor.view.CircleImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {
    List<OrderBean> list;
    Context context;
    OnOrderListener onOrderListener;

    public PatientAdapter(List<OrderBean> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(Urls.getInstance().IMG_URL+list.get(position).getHeadImg()).into(holder.img_head);
        holder.tv_name.setText(list.get(position).getElderlyName());
        if(list.get(position).getSex()==1) {
            holder.tv_sex.setText("女");
        }
        holder.tv_age.setText(list.get(position).getAge()+"岁");
        holder.tv_price.setText("￥"+list.get(position).getActPayPrice());
        holder.tv_content.setText(list.get(position).getRemark());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_head;
        TextView tv_name,tv_sex,tv_age,tv_type,tv_price,tv_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_head = itemView.findViewById(R.id.img_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_content = itemView.findViewById(R.id.tv_content);

        }
    }
}
