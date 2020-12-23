package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.bean.PatientTeam;
import net.leelink.healthdoctor.util.Urls;
import net.leelink.healthdoctor.view.CircleImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {
    List<PatientTeam.CareOlderVoListBean> list;
    Context context;
    OnOrderListener onOrderListener;

    public PatientListAdapter(List<PatientTeam.CareOlderVoListBean> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient_list,parent,false);
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
        Glide.with(context).load(Urls.getInstance().IMG_URL+list.get(position).getHeadImgPath()).into(holder.img_head);
        holder.tv_name.setText(list.get(position).getElderlyName());
        if(list.get(position).getSex()==1) {
            holder.tv_sex.setText("女");
        }
        holder.tv_age.setText(list.get(position).getAge()+"岁");
        holder.tv_time.setText(list.get(position).getUpdateTime());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_head;
        TextView tv_name,tv_sex,tv_age,tv_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_head = itemView.findViewById(R.id.img_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_time = itemView.findViewById(R.id.tv_time);

        }
    }
}
