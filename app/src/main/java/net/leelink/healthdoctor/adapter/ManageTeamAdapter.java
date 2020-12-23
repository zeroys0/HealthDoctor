package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.bean.PatientTeam;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ManageTeamAdapter extends RecyclerView.Adapter<ManageTeamAdapter.ViewHolder> {
    List<PatientTeam> list;
    Context context;
    OnSignListener onSignListener;

    public ManageTeamAdapter(List<PatientTeam> list, Context context, OnSignListener onSignListener) {
        this.list = list;
        this.context = context;
        this.onSignListener = onSignListener;
    }

    @NonNull
    @Override
    public ManageTeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manageteam,parent,false);
        ManageTeamAdapter.ViewHolder viewHolder = new ManageTeamAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManageTeamAdapter.ViewHolder holder, final int position) {
        holder.tv_name.setText(list.get(position).getGroupName());
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignListener.onButtonCancel(v,position);
            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignListener.onButtonClick(v,position);
            }
        });
        if(position==0){
            holder.img_edit.setVisibility(View.GONE);
            holder.img_delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list ==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageButton img_edit,img_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_edit = itemView.findViewById(R.id.img_edit);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
