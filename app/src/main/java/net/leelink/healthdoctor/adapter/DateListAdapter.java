package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.leelink.healthdoctor.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DateListAdapter extends RecyclerView.Adapter<DateListAdapter.ViewHolder> {
    List<String> list;
    OnOrderListener onOrderListener;
    int checkposition = -1;
    Context context;

    public DateListAdapter(List<String> list, OnOrderListener onOrderListener, Context context) {
        this.list = list;
        this.onOrderListener = onOrderListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date,parent,false);
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
        String date = list.get(position).substring(8);
        holder.tv_date.setText(date);
        if(position == checkposition){
            holder.tv_date.setBackgroundColor(context.getResources().getColor(R.color.bg));
        } else {
            holder.tv_date.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    public void setChcek(int position){
        checkposition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
