package net.leelink.healthdoctor.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.bean.HistoryOrderBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder > {
    List<HistoryOrderBean> list;
    Context context;
    OnOrderListener onOrderListener;

    public HistoryOrderAdapter(List<HistoryOrderBean> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_time.setText(list.get(position).getCreateTime());
        holder.tv_state.setText("已完成");
        holder.tv_name.setText(list.get(position).getElderlyName());
        if(list.get(position).getSex()==0) {
            holder.tv_sex.setText("男");
        } else  {
            holder.tv_sex.setText("女");
        }
        holder.tv_age.setText(list.get(position).getAge()+"岁");
        holder.tv_price.setText("￥"+list.get(position).getActPayPrice());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time,tv_state,tv_name,tv_sex,tv_age,tv_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
