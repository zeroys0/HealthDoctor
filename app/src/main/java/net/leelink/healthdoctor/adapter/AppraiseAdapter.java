package net.leelink.healthdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.bean.AppraiseBean;
import net.leelink.healthdoctor.util.RatingBar;
import net.leelink.healthdoctor.util.Urls;
import net.leelink.healthdoctor.view.CircleImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppraiseAdapter extends RecyclerView.Adapter<AppraiseAdapter.ViewHolder> {
    List<AppraiseBean> list;
    Context context;

    public AppraiseAdapter(List<AppraiseBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appraise,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(Urls.getInstance().IMG_URL+list.get(position).getHeadImgPath()).into(holder.img_head);
        holder.tv_name.setText(list.get(position).getElderlyName());
        if(list.get(position).getSex()==1){
            holder.tv_sex.setText("女");
        }
        holder.tv_age.setText(list.get(position).getAge()+"岁");
        holder.rt_score.setSelectedNumber(list.get(position).getTotalStar());
        holder.tv_content.setText(list.get(position).getContent());
        holder.tv_time.setText(list.get(position).getUpdateTime());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_head;
        TextView tv_name,tv_sex,tv_age,tv_content,tv_time;
        RatingBar rt_score;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_head = itemView.findViewById(R.id.img_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_age = itemView.findViewById(R.id.tv_age);
            rt_score = itemView.findViewById(R.id.rt_score);
            rt_score.setUntouchable();
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
