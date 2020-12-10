package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.bean.ProvinceBean;

import org.json.JSONException;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {
    List<ProvinceBean> list;
    OnLocalListener onLocalListener;
    Context context;
    int checkposition = -1;
    int type;

    public ProvinceAdapter(List<ProvinceBean> list, OnLocalListener onLocalListener, Context context,int type) {
        this.list = list;
        this.onLocalListener = onLocalListener;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province,parent,false);
        ProvinceAdapter.ViewHolder viewHolder = new ProvinceAdapter.ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLocalListener.onItemClick(v,type);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == checkposition) {
            holder.tv_type_name.setTextColor(context.getResources().getColor(R.color.blue));
            holder.label.setVisibility(View.VISIBLE);
        } else {
            holder.tv_type_name.setTextColor(context.getResources().getColor(R.color.text_black));
            holder.label.setVisibility(View.INVISIBLE);
        }
            holder.tv_type_name.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public void setChecked(int position) {
        checkposition = position;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_type_name;
        View label;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            tv_type_name = itemView.findViewById(R.id.tv_type_name);
        }
    }
}
