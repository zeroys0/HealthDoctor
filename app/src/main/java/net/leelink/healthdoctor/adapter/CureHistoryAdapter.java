package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.bean.HistoryOrder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CureHistoryAdapter extends RecyclerView.Adapter<CureHistoryAdapter.ViewHolder> {
    List<HistoryOrder> list;
    Context context;

    public CureHistoryAdapter(List<HistoryOrder> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cure_history,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_time.setText(list.get(position).getCreateTime());
        switch (list.get(position).getState()){
            case 1:
                holder.tv_state.setText("代付款");
                break;
            case 2:
                holder.tv_state.setText("待接诊");
                break;
            case 3:
                holder.tv_state.setText("接诊中");
                break;
            case 4:
            case 5:
                holder.tv_state.setText("已完成");
                break;
        }
        switch (list.get(position).getState()) {
            case 1:
                holder.tv_type.setText("电话问诊");
                break;
            case 2:
                holder.tv_type.setText("图文问诊");
                break;
            case 3:
                holder.tv_type.setText("上门问诊");
                break;
            case 4:
                holder.tv_type.setText("医院就诊");
                break;
        }
        holder.tv_remark.setText("病情描述:"+list.get(position).getRemark());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time,tv_state,tv_type,tv_remark;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_remark = itemView.findViewById(R.id.tv_remark);
        }
    }
}
