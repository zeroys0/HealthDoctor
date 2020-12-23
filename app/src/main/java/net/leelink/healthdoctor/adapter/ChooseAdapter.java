package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.bean.PatientTeam;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {
    List<PatientTeam> list;
    Context context;
    OnOrderListener onOrderListener;
    int checked = -1;

    public ChooseAdapter(List<PatientTeam> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onButtonClick(v,position);
            }
        });
        if(position ==checked){
            holder.cb_check.setChecked(true);
        }else {
            holder.cb_check.setChecked(false);
        }
        holder.cb_check.setClickable(false);
        holder.tv_name.setText(list.get(position).getGroupName());
        holder.tv_count.setText(list.get(position).getGroupCount()+"人");

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public void setChecked(int checked) {
       this.checked = checked;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_item;
        CheckBox cb_check;
        TextView tv_name,tv_count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_item = itemView.findViewById(R.id.rl_item);
            cb_check = itemView.findViewById(R.id.cb_check);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_count = itemView.findViewById(R.id.tv_count);
        }
    }
}
