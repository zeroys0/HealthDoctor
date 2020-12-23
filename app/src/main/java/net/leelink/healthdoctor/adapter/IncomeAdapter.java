package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.bean.IncomeBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
    List<IncomeBean> list;
    Context context;

    public IncomeAdapter(List<IncomeBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_month.setText(list.get(position).getUpdateTime().substring(0,4)+"年");
        holder.tv_days.setText(list.get(position).getUpdateTime().substring(5,7)+"月");
        holder.tv_get_time.setText("+￥"+list.get(position).getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_days,tv_month,tv_get_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_days = itemView.findViewById(R.id.tv_days);
            tv_month = itemView.findViewById(R.id.tv_month);
            tv_get_time = itemView.findViewById(R.id.tv_get_time);


        }
    }
}
