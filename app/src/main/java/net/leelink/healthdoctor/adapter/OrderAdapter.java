package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.bean.OrderBean;
import net.leelink.healthdoctor.util.Urls;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    List<OrderBean> list;
    Context context;
    OnOrderListener onOrderListener;

    public OrderAdapter(List<OrderBean> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order,parent,false);
        OrderAdapter.ViewHolder viewHolder = new OrderAdapter.ViewHolder(view);
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
        Glide.with(context).load(Urls.getInstance().IMG_URL +list.get(position).getHeadImg()).into(holder.img_head);
        holder.tv_name.setText(list.get(position).getElderlyName());
        if(list.get(position).getSex()==0) {
            holder.tv_sex.setText("男");
        } else  {
            holder.tv_sex.setText("女");
        }
        if(list.get(position).getState()==3) {
            holder.tv_state.setText("接诊中");
        }
        holder.tv_age.setText(list.get(position).getAge()+"岁");
        holder.tv_price.setText("￥"+list.get(position).getActPayPrice());
        holder.tv_content.setText(list.get(position).getRemark());
        holder.tv_time.setText(list.get(position).getCreateTime());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_head;
        TextView tv_name,tv_sex,tv_age,tv_price,tv_content,tv_time,tv_state;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_head = itemView.findViewById(R.id.img_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_state = itemView.findViewById(R.id.tv_state);
        }
    }
}
